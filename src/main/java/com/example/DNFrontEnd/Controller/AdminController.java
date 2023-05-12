package com.example.DNFrontEnd.Controller;

import com.example.DNFrontEnd.Model.AuthMessageCode;
import com.example.DNFrontEnd.Model.BaseResponse;
import com.example.DNFrontEnd.Model.DTO.DepartmentDTO;
import com.example.DNFrontEnd.Model.DTO.DoctorDTO;
import com.example.DNFrontEnd.Model.DTO.PatientDTO;
import com.example.DNFrontEnd.Model.DTO.UserDTO;
import com.example.DNFrontEnd.Model.ERROR;
import com.example.DNFrontEnd.Model.request.*;
import com.example.DNFrontEnd.Model.response.*;
import com.example.DNFrontEnd.Service.AdminService;
import com.example.DNFrontEnd.Service.AuthService;
import com.example.DNFrontEnd.Service.DepartmentService;
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
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping({"/admin"})
public class AdminController {
    @Autowired
    AuthService authService;

    @Autowired
    AdminService adminService;

    @Autowired
    DepartmentService departmentService;

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
                                @ModelAttribute(name = "departmentId") String departmentId,
                                @ModelAttribute(name = "email") String email,
                                @ModelAttribute(name = "message") String message) throws JsonProcessingException {
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
        if(!StringUtils.isEmpty(email)){
            model.addAttribute("email", email);
            listDoctorRequest.setEmail(email);
        }else{
            email="";
            model.addAttribute("email", email);
            listDoctorRequest.setEmail(null);
        }
        if(!StringUtils.isEmpty(message)){
            model.addAttribute("message", message);
        }else{
            model.addAttribute("message", "");
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
                                @ModelAttribute(name = "departmentId") String departmentId,
                                @ModelAttribute(name = "email") String email){
        departmentId = request.getParameter("departmentId");
        email = request.getParameter("email");
        String page = request.getParameter("page");
        redirectAttrs.addFlashAttribute("departmentId", departmentId);
        redirectAttrs.addFlashAttribute("email", email);
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
        if (doctorResponse.getDoctor().getId() != null){
            UpdateDoctorRequest updateDoctorRequest = new UpdateDoctorRequest();
            updateDoctorRequest.setId(doctorResponse.getDoctor().getId());
            updateDoctorRequest.setSex(doctorResponse.getDoctor().getSex());
            updateDoctorRequest.setName(doctorResponse.getDoctor().getFullName());
            updateDoctorRequest.setIdentityNumber(doctorResponse.getDoctor().getIdentityNumber());
            updateDoctorRequest.setPhoneNumber(doctorResponse.getDoctor().getPhoneNumber());
            BaseResponse baseResponse = adminService.updateDoctor(updateDoctorRequest,session.getAttribute("token").toString());
            DoctorResponse doctorResponse2 = objectMapper.readValue(objectMapper.writeValueAsString(baseResponse.getData()).toString(), DoctorResponse.class);
            if(doctorResponse2 != null){
                redirectAttrs.addFlashAttribute("message", "Chỉnh sửa thành công");
                redirectAttrs.addFlashAttribute("doctorId", doctorResponse2.getDoctor().getId() );
                return "redirect:/admin/doctor";
            }else{
                redirectAttrs.addFlashAttribute("message", baseResponse.getMessage());
                redirectAttrs.addFlashAttribute("doctorResponse", doctorResponse);
            }
        }else{
            AddUserRequest addUserRequest = new AddUserRequest();
            String departmentId = request.getParameter("departmentId");
            if(StringUtils.isEmpty(departmentId)){
                DepartmentDTO departmentDTO = new DepartmentDTO();
                doctorResponse.getDoctor().setDepartment(departmentDTO);
                redirectAttrs.addFlashAttribute("doctorResponse", doctorResponse);
                redirectAttrs.addFlashAttribute("message", "Phải chọn khoa");
                return "redirect:/admin/doctor";
            }
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
        }
        return "redirect:/admin/doctor";
    }

    @PostMapping("/doctor/activate")
    public String activateDoctor(HttpServletRequest request, RedirectAttributes redirectAttrs, HttpSession session,
                                @ModelAttribute(name = "departmentId") String departmentId,
                                @ModelAttribute(name = "email") String email){
        if (session.getAttribute("token") == null || StringUtils.isEmpty(session.getAttribute("token").toString())
                || StringUtils.isEmpty(session.getAttribute("userType").toString())
                || !session.getAttribute("userType").toString().equalsIgnoreCase("ADMIN")) {
            redirectAttrs.addFlashAttribute("loginRequest", new LoginRequest());
            return "redirect:/admin";
        }
        departmentId = request.getParameter("departmentId");
        email = request.getParameter("email");
        String page = request.getParameter("page");
        String userId = request.getParameter("userId");
        String isActive = request.getParameter("isActive");
        String message = "";
        ActivateUserRequest activateUserRequest = new ActivateUserRequest();
        if(!StringUtils.isEmpty(isActive) && !StringUtils.isEmpty(userId)){
            if(isActive.equalsIgnoreCase("true")){
                activateUserRequest.setUserId(Long.parseLong(userId));
                activateUserRequest.setIsActive(false);
                message = "Vô hiệu hóa thành công";
            }
            if(isActive.equalsIgnoreCase("false")){
                activateUserRequest.setUserId(Long.parseLong(userId));
                activateUserRequest.setIsActive(true);
                message = "Kích hoạt thành công";
            }
            if(activateUserRequest.getUserId() != null){
                BaseResponse baseResponse = adminService.activate(activateUserRequest,session.getAttribute("token").toString());
                if(baseResponse != null && baseResponse.getCode() == ERROR.SUCCESS.getCode()
                        && baseResponse.getMessage().equalsIgnoreCase(ERROR.SUCCESS.getMessage())){
                    redirectAttrs.addFlashAttribute("message", message);
                }else{
                    redirectAttrs.addFlashAttribute("message", "Thất bại");
                }
            }
        }
        redirectAttrs.addFlashAttribute("departmentId", departmentId);
        redirectAttrs.addFlashAttribute("email", email);
        redirectAttrs.addFlashAttribute("page", page);
        return "redirect:/admin/doctors";
    }
    @PostMapping("/patient/activate")
    public String activatePatient(HttpServletRequest request, RedirectAttributes redirectAttrs, HttpSession session,
                           @ModelAttribute(name = "email") String email){
        if (session.getAttribute("token") == null || StringUtils.isEmpty(session.getAttribute("token").toString())
                || StringUtils.isEmpty(session.getAttribute("userType").toString())
                || !session.getAttribute("userType").toString().equalsIgnoreCase("ADMIN")) {
            redirectAttrs.addFlashAttribute("loginRequest", new LoginRequest());
            return "redirect:/admin";
        }
        email = request.getParameter("email");
        String page = request.getParameter("page");
        String userId = request.getParameter("userId");
        String isActive = request.getParameter("isActive");
        String message = "";
        ActivateUserRequest activateUserRequest = new ActivateUserRequest();
        if(!StringUtils.isEmpty(isActive) && !StringUtils.isEmpty(userId)){
            if(isActive.equalsIgnoreCase("true")){
                activateUserRequest.setUserId(Long.parseLong(userId));
                activateUserRequest.setIsActive(false);
                message = "Vô hiệu hóa thành công";
            }
            if(isActive.equalsIgnoreCase("false")){
                activateUserRequest.setUserId(Long.parseLong(userId));
                activateUserRequest.setIsActive(true);
                message = "Kích hoạt thành công";
            }
            if(activateUserRequest.getUserId() != null){
                BaseResponse baseResponse = adminService.activate(activateUserRequest,session.getAttribute("token").toString());
                if(baseResponse != null && baseResponse.getCode() == ERROR.SUCCESS.getCode()
                        && baseResponse.getMessage().equalsIgnoreCase(ERROR.SUCCESS.getMessage())){
                    redirectAttrs.addFlashAttribute("message", message);
                }else{
                    redirectAttrs.addFlashAttribute("message", "Thất bại");
                }
            }
        }
        redirectAttrs.addFlashAttribute("email", email);
        redirectAttrs.addFlashAttribute("page", page);
        return "redirect:/admin/patients";
    }

    @GetMapping("patients")
    public String getListPatient(Model model,HttpSession session, @ModelAttribute(name = "page") String page,
                                @ModelAttribute(name = "email") String email,
                                @ModelAttribute(name = "message") String message) throws JsonProcessingException {
        if (session.getAttribute("token") == null || StringUtils.isEmpty(session.getAttribute("token").toString())
                || StringUtils.isEmpty(session.getAttribute("userType").toString())
                || !session.getAttribute("userType").toString().equalsIgnoreCase("ADMIN")) {
            model.addAttribute("loginRequest", new LoginRequest());
            return "redirect:/admin";
        }
        ListPatientRequest listPatientRequest = new ListPatientRequest();
        if(!StringUtils.isEmpty(email)){
            model.addAttribute("email", email);
            listPatientRequest.setEmail(email);
        }else{
            email="";
            model.addAttribute("email", email);
            listPatientRequest.setEmail(null);
        }
        if(!StringUtils.isEmpty(message)){
            model.addAttribute("message", message);
        }else{
            model.addAttribute("message", "");
        }
        page = StringUtils.isEmpty(page) ? "0" : String.valueOf(Integer.parseInt(page) - 1);
        model.addAttribute("page", String.valueOf(Integer.parseInt(page) + 1));
        BasePaginationResponse basePaginationResponse = adminService.getPatients(listPatientRequest,session.getAttribute("token").toString(),page);
        List<AdminPatientResponse> adminPatientResponseList = new ArrayList<>();
        adminPatientResponseList = objectMapper.readValue(objectMapper.writeValueAsString(basePaginationResponse.getData()).toString(), List.class);
        System.out.println(adminPatientResponseList);
        List<String> pageList = new ArrayList<>();
        if(basePaginationResponse.getTotalPages() > 1){
            for (int i = 1; i <= basePaginationResponse.getTotalPages(); i++){
                pageList.add(String.valueOf(i));
            }
        }
        model.addAttribute("pageList", pageList );
        model.addAttribute("adminPatientResponseList", adminPatientResponseList );
        return "listPatient";
    }
    @PostMapping("patients")
    public String getListPatient(HttpServletRequest request, RedirectAttributes redirectAttrs, HttpSession session,
                                @ModelAttribute(name = "email") String email){
        email = request.getParameter("email");
        String page = request.getParameter("page");
        redirectAttrs.addFlashAttribute("email", email);
        redirectAttrs.addFlashAttribute("page", page);
        return "redirect:/admin/patients";
    }

    @GetMapping("patient")
    public String getPatient(Model model,HttpSession session,
                            @ModelAttribute(name = "patientId") String patientId,
                            @ModelAttribute(name = "message") String message,
                            @ModelAttribute(name = "patientResponse") AdminPatientResponse patientResponse){
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
        if(patientResponse == null || patientResponse.getPatient() == null || patientResponse.getUser() == null){
            patientResponse = new AdminPatientResponse();
            PatientDTO patientDTO = new PatientDTO();
            patientResponse.setPatient(patientDTO);
            patientResponse.setUser(new UserDTO());
            if(!StringUtils.isEmpty(patientId)){
                patientResponse = adminService.getPatient(patientId,session.getAttribute("token").toString());
            }
        }
        model.addAttribute("patientResponse", patientResponse);
        model.addAttribute("patientProfiles",patientResponse.getPatientProfiles());
        return "patientDetail";
    }
    @PostMapping("patient")
    public String getPatient(HttpServletRequest request, RedirectAttributes redirectAttrs, HttpSession session,
                            @ModelAttribute(name = "patientId") String patientId){
        if (session.getAttribute("token") == null || StringUtils.isEmpty(session.getAttribute("token").toString())
                || StringUtils.isEmpty(session.getAttribute("userType").toString())
                || !session.getAttribute("userType").toString().equalsIgnoreCase("ADMIN")) {
            redirectAttrs.addFlashAttribute("loginRequest", new LoginRequest());
            return "redirect:/admin";
        }
        redirectAttrs.addFlashAttribute("patientId", patientId );
        return "redirect:/admin/patient";
    }

    @PostMapping("patient/add")
    public String addPatient(HttpServletRequest request, RedirectAttributes redirectAttrs, HttpSession session,
                            @ModelAttribute(name = "patientResponse") AdminPatientResponse patientResponse) throws JsonProcessingException {
        if (session.getAttribute("token") == null || StringUtils.isEmpty(session.getAttribute("token").toString())
                || StringUtils.isEmpty(session.getAttribute("userType").toString())
                || !session.getAttribute("userType").toString().equalsIgnoreCase("ADMIN")) {
            redirectAttrs.addFlashAttribute("loginRequest", new LoginRequest());
            return "redirect:/admin";
        }
        AddUserRequest addUserRequest = new AddUserRequest();
//        String name = request.getParameter("fullName");
//        String sex = request.getParameter("sex");
//        String phoneNumber = request.getParameter("phoneNumber");
//        String identityNumber = request.getParameter("identityNumber");
//        String email = request.getParameter("email");
        addUserRequest.setName(patientResponse.getPatient().getFullName());
        addUserRequest.setSex(patientResponse.getPatient().getSex());
        addUserRequest.setPhoneNumber(patientResponse.getPatient().getPhoneNumber());
        addUserRequest.setIdentityNumber(patientResponse.getPatient().getIdentityNumber());
        addUserRequest.setEmail(patientResponse.getUser().getEmail());
        addUserRequest.setType(3);
        BaseResponse baseResponse = adminService.addUser(addUserRequest,session.getAttribute("token").toString());
        AdminPatientResponse patientResponse1 = objectMapper.readValue(objectMapper.writeValueAsString(baseResponse.getData()).toString(), AdminPatientResponse.class);
        if(patientResponse1 != null){
            redirectAttrs.addFlashAttribute("message", "Tạo bệnh nhân thành công");
            redirectAttrs.addFlashAttribute("patientId", patientResponse1.getPatient().getId() );
            return "redirect:/admin/patient";
        }else{
            redirectAttrs.addFlashAttribute("message", baseResponse.getMessage());
            redirectAttrs.addFlashAttribute("patientResponse", patientResponse);
        }
        return "redirect:/admin/patient";
    }

    @GetMapping("departments")
    public String getListDepartment(Model model,HttpSession session, @ModelAttribute(name = "page") String page,
                                 @ModelAttribute(name = "name") String name,
                                 @ModelAttribute(name = "message") String message) throws JsonProcessingException {
        if (session.getAttribute("token") == null || StringUtils.isEmpty(session.getAttribute("token").toString())
                || StringUtils.isEmpty(session.getAttribute("userType").toString())
                || !session.getAttribute("userType").toString().equalsIgnoreCase("ADMIN")) {
            model.addAttribute("loginRequest", new LoginRequest());
            return "redirect:/admin";
        }
        ListDepartmentRequest listDepartmentRequest = new ListDepartmentRequest();
        if(!StringUtils.isEmpty(name)){
            model.addAttribute("name", name);
            listDepartmentRequest.setName(name);
        }else{
            name="";
            model.addAttribute("name", name);
            listDepartmentRequest.setName(null);
        }
        if(!StringUtils.isEmpty(message)){
            model.addAttribute("message", message);
        }else{
            model.addAttribute("message", "");
        }
        page = StringUtils.isEmpty(page) ? "0" : String.valueOf(Integer.parseInt(page) - 1);
        model.addAttribute("page", String.valueOf(Integer.parseInt(page) + 1));
        BasePaginationResponse basePaginationResponse = departmentService.getDepartments(listDepartmentRequest,session.getAttribute("token").toString(),page);
        List<DepartmentResponse> departmentResponseList = new ArrayList<>();
        departmentResponseList = objectMapper.readValue(objectMapper.writeValueAsString(basePaginationResponse.getData()).toString(), List.class);
        System.out.println(departmentResponseList);
        List<String> pageList = new ArrayList<>();
        if(basePaginationResponse.getTotalPages() > 1){
            for (int i = 1; i <= basePaginationResponse.getTotalPages(); i++){
                pageList.add(String.valueOf(i));
            }
        }
        model.addAttribute("pageList", pageList );
        model.addAttribute("departmentResponseList", departmentResponseList );
        return "listDepartment";
    }
    @PostMapping("departments")
    public String getListDepartment(HttpServletRequest request, RedirectAttributes redirectAttrs, HttpSession session,
                                 @ModelAttribute(name = "name") String name){
        name = request.getParameter("name");
        String page = request.getParameter("page");
        redirectAttrs.addFlashAttribute("name", name);
        redirectAttrs.addFlashAttribute("page", page);
        return "redirect:/admin/departments";
    }

    @GetMapping("stats")
    public String getStatsRevenue(Model model,HttpSession session,
                                  @ModelAttribute(name = "year") String year,
                                  @ModelAttribute(name = "monthS") String monthS,
                                  @ModelAttribute(name = "yearS") String yearS,
                                  @ModelAttribute(name = "message") String message) throws JsonProcessingException {
        if (session.getAttribute("token") == null || StringUtils.isEmpty(session.getAttribute("token").toString())
                || StringUtils.isEmpty(session.getAttribute("userType").toString())
                || !session.getAttribute("userType").toString().equalsIgnoreCase("ADMIN")) {
            model.addAttribute("loginRequest", new LoginRequest());
            return "redirect:/admin";
        }

        // for revenue
        StatsRevenueRequest statsRevenueRequest = new StatsRevenueRequest();
        if(!StringUtils.isEmpty(year)){
            statsRevenueRequest.setYear(Integer.parseInt(year));
        }else{
            year = String.valueOf(LocalDate.now().getYear());
            statsRevenueRequest.setYear(Integer.parseInt(year));
        }
        if(!StringUtils.isEmpty(message)){
            model.addAttribute("message", message);
        }else{
            model.addAttribute("message", "");
        }
        StatsRevenueResponse statsRevenueResponse = adminService.getStatsRevenue(statsRevenueRequest,session.getAttribute("token").toString());
        List<Double> listRevenue = new ArrayList<>();
        if(statsRevenueResponse != null && statsRevenueResponse.getStatsRevenues() != null
            && !statsRevenueResponse.getStatsRevenues().isEmpty()){
            year = String.valueOf(statsRevenueResponse.getYear());
            for (int i = 0 ; i < statsRevenueResponse.getStatsRevenues().size(); i++){
                listRevenue.add(statsRevenueResponse.getStatsRevenues().get(i).getRevenues());
            }
        }
        model.addAttribute("year", year );
        model.addAttribute("listRevenue", listRevenue );

        //for schedule
        StatsScheduleRequest statsScheduleRequest = new StatsScheduleRequest();
        String condition = "";
        if(!StringUtils.isEmpty(yearS)){
            condition = yearS;
        }else{
            yearS = String.valueOf(LocalDate.now().getYear());
            condition = String.valueOf(yearS);
        }
        if(!StringUtils.isEmpty(monthS)){
            monthS = Integer.parseInt(monthS) < 10 ? "0" + monthS : monthS;
            condition = condition + "-" + monthS;
        }else{
            monthS = String.valueOf(LocalDate.now().getMonthValue() < 10 ? "0" + LocalDate.now().getMonthValue() : LocalDate.now().getMonthValue());
            condition = condition + "-" + monthS;
        }
        statsScheduleRequest.setCondition(condition);
        StatsScheduleResponse statsScheduleResponse = adminService.getStatsSchedule(statsScheduleRequest,session.getAttribute("token").toString());
        List<String> listDepartment = new ArrayList<>();
        List<Long> listTotal = new ArrayList<>();
        List<Long> listDone = new ArrayList<>();
        List<Long> listNotDone = new ArrayList<>();
        List<Long> listPay = new ArrayList<>();
        List<Long> listNotPay = new ArrayList<>();
        if(statsScheduleResponse != null && statsScheduleResponse.getStatsSchedules() != null
                && !statsScheduleResponse.getStatsSchedules().isEmpty()){
            for (int i = 0 ; i < statsScheduleResponse.getStatsSchedules().size(); i++){
                listDepartment.add(statsScheduleResponse.getStatsSchedules().get(i).getDepartment().getSymbol());
                listTotal.add(statsScheduleResponse.getStatsSchedules().get(i).getScheduleRevenue().getTotalSchedule());
                listDone.add(statsScheduleResponse.getStatsSchedules().get(i).getScheduleRevenue().getTotalDone());
                listNotDone.add(statsScheduleResponse.getStatsSchedules().get(i).getScheduleRevenue().getTotalNotDone());
                listPay.add(statsScheduleResponse.getStatsSchedules().get(i).getScheduleRevenue().getTotalPay());
                listNotPay.add(statsScheduleResponse.getStatsSchedules().get(i).getScheduleRevenue().getTotalNotPay());
            }
        }
        model.addAttribute("yearS", yearS );
        model.addAttribute("monthS", monthS );
        model.addAttribute("listDepartment", listDepartment );
        model.addAttribute("listTotal", listTotal );
        model.addAttribute("listDone", listDone );
        model.addAttribute("listNotDone", listNotDone );
        model.addAttribute("listPay", listPay );
        model.addAttribute("listNotPay", listNotPay );
        return "stats";
    }
    @PostMapping("stats")
    public String getStatsRevenue(HttpServletRequest request, RedirectAttributes redirectAttrs, HttpSession session,
                                  @ModelAttribute(name = "year") String year,
                                  @ModelAttribute(name = "monthS") String monthS,
                                  @ModelAttribute(name = "yearS") String yearS){
        year = request.getParameter("year");
        redirectAttrs.addFlashAttribute("year", year);
        monthS = request.getParameter("monthS");
        redirectAttrs.addFlashAttribute("monthS", monthS);
        yearS = request.getParameter("yearS");
        redirectAttrs.addFlashAttribute("yearS", yearS);
        return "redirect:/admin/stats";
    }
}
