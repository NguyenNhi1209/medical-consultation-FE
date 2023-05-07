package com.example.DNFrontEnd.Controller;

import com.example.DNFrontEnd.Model.AuthMessageCode;
import com.example.DNFrontEnd.Model.BaseResponse;
import com.example.DNFrontEnd.Model.DTO.DepartmentDTO;
import com.example.DNFrontEnd.Model.DTO.DoctorDTO;
import com.example.DNFrontEnd.Model.DTO.UserDTO;
import com.example.DNFrontEnd.Model.request.AddUserRequest;
import com.example.DNFrontEnd.Model.request.DetailDoctorScheduleRequest;
import com.example.DNFrontEnd.Model.request.ListDoctorRequest;
import com.example.DNFrontEnd.Model.request.LoginRequest;
import com.example.DNFrontEnd.Model.response.*;
import com.example.DNFrontEnd.Service.AdminService;
import com.example.DNFrontEnd.Service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping({"/admin"})
public class AdminController {
    @Autowired
    AuthService authService;

    @Autowired
    AdminService adminService;

    ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    @GetMapping()
    public String home(Model model, HttpSession session){
        if (session.getAttribute("token") == null || StringUtils.isEmpty(session.getAttribute("token").toString())
            || StringUtils.isEmpty(session.getAttribute("userType").toString())
                || !session.getAttribute("userType").toString().equalsIgnoreCase("ADMIN")) {
            model.addAttribute("loginRequest", new LoginRequest());
            return "loginAdmin";
        }
        return "homeAdmin";
    }
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String success, Model model,
                        @ModelAttribute("loginRequest") LoginRequest loginRequest, @ModelAttribute(name = "error") String error) {
//        model.addAttribute("loginRequest", new LoginRequest());
//        return "login";

        if (error != "" && error != null) {
            model.addAttribute("loginRequest", loginRequest);
            model.addAttribute("error", error);
        } else {
            model.addAttribute("loginRequest", new LoginRequest());
            model.addAttribute("error", "");
        }

        return "loginAdmin";
    }

    @PostMapping("/login")
    public String login(Model model, HttpServletRequest request, RedirectAttributes redirectAttrs, @ModelAttribute("loginRequest") LoginRequest loginRequest,
                        HttpSession session) {

        if (StringUtils.isEmpty(loginRequest.getEmail())) {
            redirectAttrs.addFlashAttribute("error", "Nhập email");
            redirectAttrs.addFlashAttribute("loginRequest", loginRequest);
            return "redirect:/admin/login";
        }
        if (!loginRequest.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            redirectAttrs.addFlashAttribute("error", "Email không hợp lệ");
            redirectAttrs.addFlashAttribute("loginRequest", loginRequest);
            return "redirect:/admin/login";
        }
        if (StringUtils.isEmpty(loginRequest.getPassword())) {
            redirectAttrs.addFlashAttribute("error", "Nhập mật khẩu");
            redirectAttrs.addFlashAttribute("loginRequest", loginRequest);
            return "redirect:/admin/login";
        }
        System.out.println("user.toString()");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        User user = new User();
//        user.setUsername(request.getParameter("username"));
//        user.setPassword(request.getParameter("password"));
        LoginRequest user = new LoginRequest();
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));

        try {
            BaseResponse baseResponse = authService.login(user,true);
            if (baseResponse != null) {
                if (baseResponse.getCode() == 1) {
                    LoginResponse loginResponse = objectMapper.readValue(objectMapper.writeValueAsString(baseResponse.getData()).toString(), LoginResponse.class);
                    if (loginResponse != null) {
                        request.getSession().setAttribute("Authorization", loginResponse.getToken());
                        BaseResponse userProfileResponseBaseResponse = authService.getProfileUser(loginResponse.getToken());
                        session.setAttribute("token", loginResponse.getToken());
//                        Contanst.token = loginResponse.getToken();
                        System.out.println(userProfileResponseBaseResponse);
                        UserProfileResponse userProfileResponse = objectMapper.readValue(objectMapper.writeValueAsString(userProfileResponseBaseResponse.getData()).toString(), UserProfileResponse.class);

//                        Contanst.userProfileResponse = userProfileResponse;
                        redirectAttrs.addFlashAttribute("name", userProfileResponse.getName());
//                        Contanst.name = userProfileResponse.getName();
                        session.setAttribute("name",userProfileResponse.getName());
                        redirectAttrs.addFlashAttribute("userType", userProfileResponse.getType());
//                        Contanst.userType = userProfileResponse.getType();
                        session.setAttribute("userType",userProfileResponse.getType());
                        session.setAttribute("phone",userProfileResponse.getPhoneNumber());
                        session.setAttribute("id",userProfileResponse.getId());
                    }
                } else {
                    System.out.println(baseResponse.getMessage());

                    AuthMessageCode authMessageCode = AuthMessageCode.from(baseResponse.getMessageCode());
                    if (authMessageCode != null && !authMessageCode.getCode().equalsIgnoreCase(AuthMessageCode.UNKNOWN.getCode())) {
                        model.addAttribute("error", authMessageCode.getMessage());
                    } else {
                        model.addAttribute("error", baseResponse.getMessage());
                    }
                    return "loginAdmin";
                }
            }

//            request.getSession().setAttribute("jwtResponse", (BaseResponse) baseResponse.getBody());

        } catch (HttpClientErrorException ex) {
            ex.printStackTrace();
            System.out.println("catch roi");
            return "homeAdmin";
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "redirect:/admin";
    }


    //department

    //doctor
    @GetMapping("doctors")
    public String getListDoctor(Model model,HttpSession session, @ModelAttribute(name = "page") String page,
                                @ModelAttribute(name = "departmentId") String departmentId) throws JsonProcessingException {
        if (session.getAttribute("token") == null || StringUtils.isEmpty(session.getAttribute("token").toString())
                || StringUtils.isEmpty(session.getAttribute("userType").toString())
                || !session.getAttribute("userType").toString().equalsIgnoreCase("ADMIN")) {
            model.addAttribute("loginRequest", new LoginRequest());
            return "redirect:/admin";
        }
        ListDoctorRequest listDoctorRequest = new ListDoctorRequest();
        if(!StringUtils.isEmpty(departmentId)){
            model.addAttribute("departmentId", departmentId);
            listDoctorRequest.setDepartmentId(Long.parseLong(departmentId));
        }else{
            departmentId = "";
            model.addAttribute("departmentId", departmentId);
            listDoctorRequest.setDepartmentId(null);
        }
        page = StringUtils.isEmpty(page) ? "0" : String.valueOf(Integer.parseInt(page) - 1);
        model.addAttribute("page", String.valueOf(Integer.parseInt(page) + 1));
        BasePaginationResponse basePaginationResponse = adminService.getDoctors(listDoctorRequest,session.getAttribute("token").toString(),page);
        List<DoctorResponse> doctorResponseList = new ArrayList<>();
        doctorResponseList = objectMapper.readValue(objectMapper.writeValueAsString(basePaginationResponse.getData()).toString(), List.class);
        System.out.println(doctorResponseList);
        List<String> pageList = new ArrayList<>();
        if(basePaginationResponse.getTotalPages() > 1){
            for (int i = 1; i <= basePaginationResponse.getTotalPages(); i++){
                pageList.add(String.valueOf(i));
            }
        }
        model.addAttribute("pageList", pageList );
        model.addAttribute("doctorResponseList", doctorResponseList );
        return "listDoctor";
    }
    @PostMapping("doctors")
    public String getListDoctor(HttpServletRequest request, RedirectAttributes redirectAttrs, HttpSession session,
                                @ModelAttribute(name = "departmentId") String departmentId){
        departmentId = request.getParameter("departmentId");
        String page = request.getParameter("page");
        redirectAttrs.addFlashAttribute("departmentId", departmentId);
        redirectAttrs.addFlashAttribute("page", page);
        return "redirect:/admin/doctors";
    }

    @GetMapping("doctor")
    public String getDoctor(Model model,HttpSession session,
                            @ModelAttribute(name = "doctorId") String doctorId,
                            @ModelAttribute(name = "message") String message,
                            @ModelAttribute(name = "doctorResponse") DoctorResponse doctorResponse){
        if (session.getAttribute("token") == null || StringUtils.isEmpty(session.getAttribute("token").toString())
                || StringUtils.isEmpty(session.getAttribute("userType").toString())
                || !session.getAttribute("userType").toString().equalsIgnoreCase("ADMIN")) {
            model.addAttribute("loginRequest", new LoginRequest());
            return "redirect:/admin";
        }
        if(!StringUtils.isEmpty(message)){
            model.addAttribute("message", message);
        }else{
            model.addAttribute("message", "");
        }
        if(doctorResponse == null || doctorResponse.getDoctor() == null || doctorResponse.getUser() == null){
            doctorResponse = new DoctorResponse();
            DoctorDTO doctorDTO = new DoctorDTO();
            doctorDTO.setDepartment(new DepartmentDTO());
            doctorResponse.setDoctor(doctorDTO);
            doctorResponse.setUser(new UserDTO());
            if(!StringUtils.isEmpty(doctorId)){
                doctorResponse = adminService.getDoctor(doctorId,session.getAttribute("token").toString());
            }
        }
        model.addAttribute("doctorResponse", doctorResponse);
        return "doctorDetail";
    }
    @PostMapping("doctor")
    public String getDoctor(HttpServletRequest request, RedirectAttributes redirectAttrs, HttpSession session,
                            @ModelAttribute(name = "doctorId") String doctorId){
        if (session.getAttribute("token") == null || StringUtils.isEmpty(session.getAttribute("token").toString())
                || StringUtils.isEmpty(session.getAttribute("userType").toString())
                || !session.getAttribute("userType").toString().equalsIgnoreCase("ADMIN")) {
            redirectAttrs.addFlashAttribute("loginRequest", new LoginRequest());
            return "redirect:/admin";
        }
        redirectAttrs.addFlashAttribute("doctorId", doctorId );
        return "redirect:/admin/doctor";
    }

    @PostMapping("doctor/add")
    public String addDoctor(HttpServletRequest request, RedirectAttributes redirectAttrs, HttpSession session,
                 @ModelAttribute(name = "doctorResponse") DoctorResponse doctorResponse) throws JsonProcessingException {
        if (session.getAttribute("token") == null || StringUtils.isEmpty(session.getAttribute("token").toString())
                || StringUtils.isEmpty(session.getAttribute("userType").toString())
                || !session.getAttribute("userType").toString().equalsIgnoreCase("ADMIN")) {
            redirectAttrs.addFlashAttribute("loginRequest", new LoginRequest());
            return "redirect:/admin";
        }
        AddUserRequest addUserRequest = new AddUserRequest();
        String departmentId = request.getParameter("departmentId");
//        String name = request.getParameter("fullName");
//        String sex = request.getParameter("sex");
//        String phoneNumber = request.getParameter("phoneNumber");
//        String identityNumber = request.getParameter("identityNumber");
//        String email = request.getParameter("email");

        addUserRequest.setName(doctorResponse.getDoctor().getFullName());
        addUserRequest.setSex(doctorResponse.getDoctor().getSex());
        addUserRequest.setPhoneNumber(doctorResponse.getDoctor().getPhoneNumber());
        addUserRequest.setIdentityNumber(doctorResponse.getDoctor().getIdentityNumber());
        addUserRequest.setEmail(doctorResponse.getUser().getEmail());
        addUserRequest.setDepartmentId(Long.parseLong(departmentId));
        addUserRequest.setType(2);
        BaseResponse baseResponse = adminService.addUser(addUserRequest,session.getAttribute("token").toString());
        DoctorResponse doctorResponse1 = objectMapper.readValue(objectMapper.writeValueAsString(baseResponse.getData()).toString(), DoctorResponse.class);
        if(doctorResponse1 != null){
            redirectAttrs.addFlashAttribute("message", "Tạo bác sĩ thành công");
            redirectAttrs.addFlashAttribute("doctorId", doctorResponse1.getDoctor().getId() );
            return "redirect:/admin/doctor";
        }else{
            redirectAttrs.addFlashAttribute("message", baseResponse.getMessage());
            DepartmentDTO departmentDTO = new DepartmentDTO();
            departmentDTO.setId(Long.parseLong(departmentId));
            doctorResponse.getDoctor().setDepartment(departmentDTO);
            redirectAttrs.addFlashAttribute("doctorResponse", doctorResponse);
        }
        return "redirect:/admin/doctor";
    }

}
