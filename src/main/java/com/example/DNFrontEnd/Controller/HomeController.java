package com.example.DNFrontEnd.Controller;

import com.example.DNFrontEnd.Model.BaseResponse;
import com.example.DNFrontEnd.Model.DTO.PaymentDTO;
import com.example.DNFrontEnd.Model.entity.ParentDetail;
import com.example.DNFrontEnd.Model.request.*;
import com.example.DNFrontEnd.Model.response.*;
import com.example.DNFrontEnd.Service.DoctorService;
import com.example.DNFrontEnd.Service.PatientService;
import com.example.DNFrontEnd.Service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping({"/", "/home"})
public class HomeController {
    @Autowired
    PatientService patientService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    PaymentService paymentService;

    ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

    @GetMapping
    public String home(Model model, @ModelAttribute(name = "name") String name,
                       @ModelAttribute(name = "userType") String userType, HttpSession session) {
        if (session.getAttribute("token") == null || StringUtils.isEmpty(session.getAttribute("token").toString())) {
            model.addAttribute("loginRequest", new LoginRequest());
            return "login";
        }
        return "home";
    }
    @GetMapping("/redirectBooking")
    public String redirectBooking(Model model, HttpServletRequest request, HttpSession session ,RedirectAttributes redirectAttrs, @ModelAttribute("patientProfileResponse")PatientProfileResponse patientProfileResponse,
                                  @RequestParam("symptom") String symptom, @RequestParam("scheduleDate") String scheduleDate) {
        if (session.getAttribute("token") == null || StringUtils.isEmpty(session.getAttribute("token").toString())) {
            model.addAttribute("loginRequest", new LoginRequest());
            return "login";
        }
        session.setAttribute("chooseTime",false);
         redirectAttrs.addFlashAttribute("scheduleDate",scheduleDate);
        redirectAttrs.addFlashAttribute("symptom",symptom);
        patientProfileResponse.setSymptom(symptom);
        redirectAttrs.addFlashAttribute("patientProfileResponse", patientProfileResponse);
        return "redirect:/booking";
    }
    @GetMapping("/booking")
    public String booking(Model model, HttpSession session ,@ModelAttribute("patientProfileResponse")PatientProfileResponse patientProfileResponse,
                          @ModelAttribute("listFreeSchedule") ListFreeSchedule listFreeSchedule,@ModelAttribute(name = "error") String error) {
        if (session.getAttribute("token") == null || StringUtils.isEmpty(session.getAttribute("token").toString())) {
            model.addAttribute("loginRequest", new LoginRequest());
            return "login";
        }
        if(listFreeSchedule != null && listFreeSchedule.getDepartmentId() == null){
            session.setAttribute("chooseTime",false);
        }
        model.addAttribute("patientProfileResponse",patientProfileResponse);
        model.addAttribute("listFreeSchedule",listFreeSchedule);
        DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate1 = LocalDate.now();//For reference
        model.addAttribute("toDate",localDate1.format(format1));
        return "booking";
    }
    @PostMapping("/booking")
    public String postBooking(Model model, HttpSession session,RedirectAttributes redirectAttrs, HttpServletRequest servletRequest,
                              @ModelAttribute("patientProfileResponse")PatientProfileResponse patientProfileResponse,
                              @ModelAttribute("listFreeSchedule") ListFreeSchedule listFreeSchedule ) throws JsonProcessingException {
        if (StringUtils.isEmpty(servletRequest.getParameter("symptom"))) {
            redirectAttrs.addFlashAttribute("error", "Nhập triệu chứng");
            redirectAttrs.addFlashAttribute("patientProfileResponse", patientProfileResponse);
            redirectAttrs.addFlashAttribute("listFreeSchedule", listFreeSchedule);
            return "redirect:/booking";
        }
        if (StringUtils.isEmpty(servletRequest.getParameter("scheduleDate"))) {
            redirectAttrs.addFlashAttribute("error", "Nhập ngày khám ");
            redirectAttrs.addFlashAttribute("patientProfileResponse", patientProfileResponse);
            redirectAttrs.addFlashAttribute("listFreeSchedule", listFreeSchedule);
            return "redirect:/booking";
        }
        String date = servletRequest.getParameter("scheduleDate");
        String symptom = servletRequest.getParameter("symptom");
        System.out.println(date + " " + symptom);

        patientProfileResponse.setSymptom(symptom);
        redirectAttrs.addFlashAttribute("patientProfileResponse", patientProfileResponse);
        FetchDepartmentRequest request1 = new FetchDepartmentRequest();
        request1.setMedicalDate(date);
        request1.setSymptom(symptom);
        System.out.println(request1);

        BaseResponse baseResponse = patientService.fetchDepartment(request1,session.getAttribute("token").toString());
        listFreeSchedule = objectMapper.readValue(objectMapper.writeValueAsString(baseResponse.getData()).toString(), ListFreeSchedule.class);
        System.out.println(listFreeSchedule);
        if(!StringUtils.isEmpty(baseResponse.getMessageCode()) && baseResponse.getMessageCode().equalsIgnoreCase("EMPTY_SYMPTOM")){
            redirectAttrs.addFlashAttribute("error", baseResponse.getMessage());
            redirectAttrs.addFlashAttribute("scheduleDate",date);
            return "redirect:/booking";
        }
        if(listFreeSchedule != null){
            session.setAttribute("chooseTime",true);
            redirectAttrs.addFlashAttribute("listFreeSchedule", listFreeSchedule);
        }

        List<ListFreeSchedule.DetailSchedule> detailSchedules = listFreeSchedule.getDetailSchedules();
        for (int i= 1; i <=  detailSchedules.size(); i++) {
            //check date and hour
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(listFreeSchedule.getMedicalDate(), format);
            if(localDate.compareTo(LocalDate.now()) == 0){ //so sanh bang ngay hien tai
                // check hour: phai dat truoc 30p
                int time = Integer.parseInt(detailSchedules.get(i-1).getScheduleTime());
                if((LocalDateTime.now().getHour() >= time)
                || ((LocalDateTime.now().getHour() == time - 1) && (LocalDateTime.now().getMinute() > 45))){
                    redirectAttrs.addFlashAttribute("free" + i , null);
                    continue;
                }
            }
            redirectAttrs.addFlashAttribute("free" + i ,detailSchedules.get(i-1).getDoctorId() != null ? detailSchedules.get(i-1).getDoctorId()+"-"+detailSchedules.get(i-1).getScheduleTime()+"-"+detailSchedules.get(i-1).getPrice() : null);

        }
        redirectAttrs.addFlashAttribute("scheduleDate",date);
        redirectAttrs.addFlashAttribute("symptom",symptom);
        redirectAttrs.addFlashAttribute("departmentId",listFreeSchedule.getDepartmentId());
        System.out.println(listFreeSchedule);


        return "redirect:/booking";
    }

    @GetMapping("/updateProfile")
    public String loadProfile(Model model, HttpSession session, @ModelAttribute("patientResponse") PatientResponse patientResponse, @ModelAttribute(name = "error") String error) throws JsonProcessingException {
        if (error != "" && error != null) {
            model.addAttribute("patientResponse", patientResponse);
            model.addAttribute("error", error);
        } else {
            model.addAttribute("patientResponse", new PatientResponse());
            model.addAttribute("error", "");
        }

        //get patien/doctor by userID
        if (session.getAttribute("token") == null || StringUtils.isEmpty(session.getAttribute("token").toString())) {
            model.addAttribute("loginRequest", new LoginRequest());
            return "login";
        }
        if (session.getAttribute("userType") != null || StringUtils.isEmpty(session.getAttribute("userType").toString())) {
            if (session.getAttribute("userType").toString().equalsIgnoreCase("PATIENT")) {
                BaseResponse baseResponse = patientService.getPatient(session.getAttribute("token").toString());
                patientResponse = objectMapper.readValue(objectMapper.writeValueAsString(baseResponse.getData()).toString(), PatientResponse.class);
                model.addAttribute("patientResponse", patientResponse);
                return "updateProfilePatient";
            }
            if (session.getAttribute("userType").toString().equalsIgnoreCase("DOCTOR")) {
                DoctorProfileResponse doctorProfileResponse = doctorService.getDoctorProfile(session.getAttribute("token").toString());
                model.addAttribute("doctorProfileResponse", doctorProfileResponse);
                return "updateProfileDoctor";
            }
        }
        return "home";
    }

    @PostMapping("/updateProfile")
    public String updateProfile(Model model, HttpServletRequest request, RedirectAttributes redirectAttrs, HttpSession session,
                                @ModelAttribute("patientResponse") PatientResponse patientResponse,
                                @ModelAttribute("doctorProfileResponse") DoctorProfileResponse doctorProfileResponse) throws JsonProcessingException {
        if (session.getAttribute("userType").toString().equalsIgnoreCase("PATIENT")) {
            SavePatientRequest savePatientRequest = new SavePatientRequest();
            savePatientRequest.setId(patientResponse.getId());
            savePatientRequest.setFullName(patientResponse.getFullName());
            savePatientRequest.setBirthday(patientResponse.getBirthday());
            savePatientRequest.setSex(patientResponse.getSex());
            savePatientRequest.setAddress(patientResponse.getAddress());
            savePatientRequest.setJob(patientResponse.getJob());
            savePatientRequest.setIdentityNumber(patientResponse.getIdentityNumber());
            savePatientRequest.setPhoneNumber(patientResponse.getPhoneNumber());
            System.out.println(savePatientRequest.toString());
            BaseResponse baseResponse = patientService.savePatient(savePatientRequest, session.getAttribute("token").toString());
            patientResponse = objectMapper.readValue(objectMapper.writeValueAsString(baseResponse.getData()).toString(), PatientResponse.class);
            redirectAttrs.addFlashAttribute("patientResponse", patientResponse);
            if(patientResponse != null){
                session.setAttribute("name",patientResponse.getFullName());
            }
        }else{
            SaveProfileRequest saveProfileRequest = new SaveProfileRequest();
            saveProfileRequest.setId(doctorProfileResponse.getDoctorId());
            saveProfileRequest.setSex(doctorProfileResponse.getSex());
            saveProfileRequest.setFullName(doctorProfileResponse.getFullName());
            saveProfileRequest.setPhoneNumber(doctorProfileResponse.getPhoneNumber());
            saveProfileRequest.setIdentityNumber(doctorProfileResponse.getIdentityNumber());
            BaseResponse baseResponse = doctorService.saveDoctorProfile(saveProfileRequest, session.getAttribute("token").toString());
            redirectAttrs.addFlashAttribute("patientResponse", patientResponse);
            session.setAttribute("name",doctorProfileResponse.getFullName());
        }

        return "redirect:/updateProfile";

    }

    @GetMapping("/introduce")
    public String introduce(Model model) {
        return "introduce";
    }

    @GetMapping("/booking/confirm")
    public  String bookingConfirm(Model model,HttpSession session, @RequestParam(required = false) String free,@RequestParam(required = false) String scheduleDate,
                                  @RequestParam(required = false) String departmentId,@RequestParam(required = false) String departmentName,
                                  @RequestParam(required = false) String symptom,HttpServletRequest request,
                                  @ModelAttribute("patientResponse") PatientResponse patientResponse,
                                  @ModelAttribute("saveScheduleRequest") SaveScheduleRequest saveScheduleRequest,
                                  @ModelAttribute("symptom") String symptom1,
                                  @ModelAttribute("departmentName") String departmentName1,
                                  @ModelAttribute("price") String price1
                                  ) throws JsonProcessingException {
        if(patientResponse != null && saveScheduleRequest != null
                && !StringUtils.isEmpty(departmentName1) && !StringUtils.isEmpty(price1) && !StringUtils.isEmpty(symptom1)){
            model.addAttribute("patientResponse", patientResponse);
            model.addAttribute("saveScheduleRequest", saveScheduleRequest);
            model.addAttribute("departmentName", departmentName1);
            model.addAttribute("price", price1);
            model.addAttribute("symptom", symptom1);
        }else{
            System.out.println("nhi" + free +" "+ scheduleDate +" "+ symptom +" "+ departmentId );
            BaseResponse baseResponse = patientService.getPatient(session.getAttribute("token").toString());
            patientResponse = objectMapper.readValue(objectMapper.writeValueAsString(baseResponse.getData()).toString(), PatientResponse.class);
            model.addAttribute("patientResponse", patientResponse);
            String[] a = free.split("-");
            String doctorId = a[0];
            String hours = a[1];
            String price = a[2];
            saveScheduleRequest.setHours(hours);
            saveScheduleRequest.setSymptom(symptom);
            saveScheduleRequest.setDepartmentId(Long.parseLong(departmentId));
            saveScheduleRequest.setDoctorId(Long.parseLong(doctorId));
            saveScheduleRequest.setMedicalDate(scheduleDate);
            saveScheduleRequest.setSymptom(symptom);

            model.addAttribute("saveScheduleRequest", saveScheduleRequest);
            model.addAttribute("departmentName", departmentName);
            model.addAttribute("price", price);
            model.addAttribute("symptom", symptom);
        }

        session.setAttribute("chooseTime",false);

//        String deparmentName = request.getParameter("deparmentId");
//        String date = request.getParameter("scheduleDate");
//        String symptom = request.getParameter("symptom");
//        System.out.println("nhi" + deparmentName + date + symptom);

        return "bookingConfirm";
    }
    @PostMapping("/saveSchedule")
    public String saveSchedule(Model model, HttpServletRequest request, HttpSession session,RedirectAttributes redirectAttrs,
                                @ModelAttribute("saveScheduleRequest") SaveScheduleRequest saveScheduleRequest) throws JsonProcessingException {
        DetailScheduleResponse detailScheduleResponse = patientService.saveSchedule(saveScheduleRequest,session.getAttribute("token").toString());
        if(detailScheduleResponse != null){
            String vnp_BankCode  = request.getParameter("bankCode");
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setVnp_BankCode(vnp_BankCode);
            String price = request.getParameter("price");
            paymentDTO.setVnp_Amount(price.replace(".0", ""));
            paymentDTO.setVnp_OrderInfo(session.getAttribute("name") + " thanh toán đặt lịch khám bệnh " + saveScheduleRequest.getMedicalDate());
            paymentDTO.setScheduleId(detailScheduleResponse.getScheduleId());
            System.out.println(paymentDTO.toString());
            String payment = paymentService.payment(paymentDTO,session.getAttribute("token").toString());
            if(vnp_BankCode.equalsIgnoreCase("CASH")){
                redirectAttrs.addFlashAttribute("message","Đặt lịch thành công");
                return "redirect:/patient/schedule/detail1?scheduleId=" + detailScheduleResponse.getScheduleId();
            }
            if(!payment.equalsIgnoreCase("error")){
                session.setAttribute("scheduleId", detailScheduleResponse.getScheduleId());
                if(!payment.equalsIgnoreCase("CASH")){
                    model.addAttribute("paymentDTO","paymentDTO");
                    model.addAttribute("payment", payment);
                    return "paymentConfirm";
                }
            }
        }
        redirectAttrs.addFlashAttribute("saveScheduleRequest", saveScheduleRequest);
        redirectAttrs.addFlashAttribute("departmentName", detailScheduleResponse.getDepartmentName());
        redirectAttrs.addFlashAttribute("price", detailScheduleResponse.getPrice());
        redirectAttrs.addFlashAttribute("symptom", detailScheduleResponse.getSymptom());
        session.setAttribute("chooseTime",false);
        redirectAttrs.addFlashAttribute("message","Xảy ra lỗi, vui lòng thử lại");
        return "redirect:/booking/confirm";


    }

    @GetMapping("/paymentSuccess")
    public String paymentSuccess(Model model, HttpSession session, @ModelAttribute("patientResponse") PatientResponse patientResponse, @ModelAttribute(name = "error") String error) throws JsonProcessingException {
        return "success";
    }

    @GetMapping("/patientProfile")
    public String test(Model model, HttpSession session, @ModelAttribute("parentDetail") ParentDetail parentDetail,
                       @ModelAttribute("createPatientResponse") CreatePatientResponse createPatientResponse,
                       @ModelAttribute(name = "error") String error) throws JsonProcessingException {
        if (error != "" && error != null) {
            model.addAttribute("parentDetail", parentDetail);
            model.addAttribute("createPatientResponse", createPatientResponse);
            model.addAttribute("error", error);
        } else {
            model.addAttribute("parentDetail", new ParentDetail());
            model.addAttribute("createPatientResponse", new CreatePatientResponse());
            model.addAttribute("error", "");
        }

        //get patien/doctor by userID
        if (session.getAttribute("token") == null || StringUtils.isEmpty(session.getAttribute("token").toString())) {
            model.addAttribute("loginRequest", new LoginRequest());
            return "login";
        }
//        if (session.getAttribute("userType") != null || StringUtils.isEmpty(session.getAttribute("userType").toString())) {
//            if (session.getAttribute("userType").toString().equalsIgnoreCase("PATIENT")) {
//                BaseResponse baseResponse = patientService.getPatient(session.getAttribute("token").toString());
//                patientResponse = objectMapper.readValue(objectMapper.writeValueAsString(baseResponse.getData()).toString(), PatientResponse.class);
//                model.addAttribute("patientResponse", patientResponse);
//                return "updateProfilePatient";
//            }
//            if (session.getAttribute("userType").toString().equalsIgnoreCase("DOCTOR")) {
//                DoctorProfileResponse doctorProfileResponse = doctorService.getDoctorProfile(session.getAttribute("token").toString());
//                model.addAttribute("doctorProfileResponse", doctorProfileResponse);
//                return "updateProfileDoctor";
//            }
//        }

        return "patientProfile";
    }
}
