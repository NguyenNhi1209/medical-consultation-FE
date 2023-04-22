package com.example.DNFrontEnd.Controller;

import com.example.DNFrontEnd.Model.BaseResponse;
import com.example.DNFrontEnd.Model.request.*;
import com.example.DNFrontEnd.Model.response.DetailScheduleResponse;
import com.example.DNFrontEnd.Model.response.ListFreeSchedule;
import com.example.DNFrontEnd.Model.response.PatientProfileResponse;
import com.example.DNFrontEnd.Model.response.PatientResponse;
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
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping({"/", "/home"})
public class HomeController {
    @Autowired
    PatientService patientService;

    @Autowired
    PaymentService paymentService;

    ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

    @GetMapping
    public String home(Model model, @ModelAttribute(name = "name") String name,
                       @ModelAttribute(name = "userType") String userType, HttpSession session) {


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
        return "booking";
    }
    @PostMapping("/booking")
    public String postBooking(Model model, HttpSession session,RedirectAttributes redirectAttrs, HttpServletRequest servletRequest,
                              @ModelAttribute("patientProfileResponse")PatientProfileResponse patientProfileResponse,
                              @ModelAttribute("listFreeSchedule") ListFreeSchedule listFreeSchedule ) {
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

//        CreatePatientProfileRequest request = new CreatePatientProfileRequest();
//        request.setSymptom(servletRequest.getParameter("symptom"));
//        patientProfileResponse = patientService.createPatientProfile(request, session.getAttribute("token").toString());
        patientProfileResponse.setSymptom(symptom);
        redirectAttrs.addFlashAttribute("patientProfileResponse", patientProfileResponse);
        FetchDepartmentRequest request1 = new FetchDepartmentRequest();
        request1.setMedicalDate(date);
        request1.setSymptom(symptom);
        System.out.println(request1);
        listFreeSchedule = patientService.fetchDepartment(request1,session.getAttribute("token").toString());

        if(listFreeSchedule != null){
            session.setAttribute("chooseTime",true);
            redirectAttrs.addFlashAttribute("listFreeSchedule", listFreeSchedule);

        }

        List<ListFreeSchedule.DetailSchedule> detailSchedules = listFreeSchedule.getDetailSchedules();
        for (int i= 1; i <=  detailSchedules.size(); i++) {
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
                return "updateProfilePatient";
            }
        }
        return "home";
    }

    @PostMapping("/updateProfile")
    public String updateProfile(Model model, HttpServletRequest request, RedirectAttributes redirectAttrs, HttpSession session,
                                @ModelAttribute("patientResponse") PatientResponse patientResponse) throws JsonProcessingException {
        System.out.println("vô nè");

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
        return "redirect:/updateProfile";

    }

    @GetMapping("/introduce")
    public String introduce(Model model) {
        return "introduce";
    }

    @GetMapping("/booking/confirm")
    public  String bookingConfirm(Model model,HttpSession session, @RequestParam String free,@RequestParam String scheduleDate,
                                  @RequestParam String departmentId,@RequestParam String departmentName,@RequestParam String symptom,HttpServletRequest request,
                                  @ModelAttribute("patientResponse") PatientResponse patientResponse,
                                  @ModelAttribute("saveScheduleRequest") SaveScheduleRequest saveScheduleRequest) throws JsonProcessingException {
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
        session.setAttribute("chooseTime",false);

//        String deparmentName = request.getParameter("deparmentId");
//        String date = request.getParameter("scheduleDate");
//        String symptom = request.getParameter("symptom");
//        System.out.println("nhi" + deparmentName + date + symptom);
        return "bookingConfirm";
    }
    @PostMapping("/saveSchedule")
    public String saveSchedule(Model model, HttpServletRequest request, HttpSession session,
                                @ModelAttribute("saveScheduleRequest") SaveScheduleRequest saveScheduleRequest) throws JsonProcessingException {
        System.out.println("vô nè");
        System.out.println(saveScheduleRequest.toString());
        DetailScheduleResponse detailScheduleResponse = patientService.saveSchedule(saveScheduleRequest,session.getAttribute("token").toString());
        if(detailScheduleResponse != null){
            String vnp_BankCode  = request.getParameter("bankCode");
            if(!StringUtils.isEmpty(vnp_BankCode) && vnp_BankCode.equalsIgnoreCase("M")){
                return "test";
            }
            if(!StringUtils.isEmpty(vnp_BankCode) && !vnp_BankCode.equalsIgnoreCase("M")){
                PaymentDTO paymentDTO = new PaymentDTO();
                paymentDTO.setVnp_BankCode(vnp_BankCode);
                String price = request.getParameter("price");
                paymentDTO.setVnp_Amount(price.replace(".0", ""));
                paymentDTO.setVnp_OrderInfo(session.getAttribute("name") + " thanh toán đặt lịch khám bệnh " + saveScheduleRequest.getMedicalDate());
                System.out.println(paymentDTO.toString());
                boolean is_pay = paymentService.payment(paymentDTO,session.getAttribute("token").toString() );
            }

        }
        return "redirect:/";

    }

}
