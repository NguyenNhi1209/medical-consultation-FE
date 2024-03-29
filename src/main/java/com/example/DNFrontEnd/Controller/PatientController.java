package com.example.DNFrontEnd.Controller;

import com.example.DNFrontEnd.Model.BaseResponse;
import com.example.DNFrontEnd.Model.DTO.PatientInfoUI;
import com.example.DNFrontEnd.Model.DTO.PatientProfileDTO;
import com.example.DNFrontEnd.Model.entity.ParentDetail;
import com.example.DNFrontEnd.Model.entity.PatientDetail;
import com.example.DNFrontEnd.Model.request.*;
import com.example.DNFrontEnd.Model.response.*;
import com.example.DNFrontEnd.Service.DoctorService;
import com.example.DNFrontEnd.Service.PatientService;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    PatientService patientService;
    @Autowired
    DoctorService doctorService;
    ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    @GetMapping("/schedule")
    public String getSchedule(Model model, HttpSession session,
                              @ModelAttribute(name = "medicalDate") String medicalDate,@ModelAttribute(name = "page") String page,
                              @ModelAttribute(name = "isPay") String isPay,@ModelAttribute(name = "isDone") String isDone) throws JsonProcessingException {
        String patienId = session.getAttribute("id").toString();
        DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter format2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        if(!StringUtils.isEmpty(medicalDate)){
            LocalDate localDate = LocalDate.parse(medicalDate, format1);
            model.addAttribute("medicalDate", format2.format(localDate));
            model.addAttribute("medicalDate1", medicalDate);
        }else{
            medicalDate = "";
            model.addAttribute("medicalDate", "");
            model.addAttribute("medicalDate1", "");
        }
        ListPatientScheduleRequest listPatientScheduleRequest = new ListPatientScheduleRequest();
        listPatientScheduleRequest.setPatientId(Long.parseLong(patienId));
        listPatientScheduleRequest.setMedicalDate(medicalDate);
        if(!StringUtils.isEmpty(isPay)){
            if(isPay.equalsIgnoreCase("true")){
                listPatientScheduleRequest.setIsPay(true);
                model.addAttribute("isPay", isPay);
            }
            if(isPay.equalsIgnoreCase("false")){
                listPatientScheduleRequest.setIsPay(false);
                model.addAttribute("isPay", isPay);
            }
        }
        if(!StringUtils.isEmpty(isDone)){
            if(isDone.equalsIgnoreCase("true")){
                listPatientScheduleRequest.setIsDone(true);
                model.addAttribute("isDone", isDone);
            }
            if(isDone.equalsIgnoreCase("false")){
                listPatientScheduleRequest.setIsDone(false);
                model.addAttribute("isDone", isDone);
            }
        }
        System.out.println(listPatientScheduleRequest.toString());
        page = StringUtils.isEmpty(page) ? "0" : String.valueOf(Integer.parseInt(page) - 1);
        model.addAttribute("page", String.valueOf(Integer.parseInt(page) + 1));

        BasePaginationResponse basePaginationResponse = patientService.getSchedule(listPatientScheduleRequest, session.getAttribute("token").toString(),page);
        List<SchedulesResponse> schedulesResponseList = new ArrayList<>();
        schedulesResponseList = objectMapper.readValue(objectMapper.writeValueAsString(basePaginationResponse.getData()).toString(), List.class);
        System.out.println(schedulesResponseList);
        model.addAttribute("schedulesResponseList", schedulesResponseList);

        List<String> pageList = new ArrayList<>();
        if(basePaginationResponse.getTotalPages() > 1){
            for (int i = 1; i <= basePaginationResponse.getTotalPages(); i++){
                pageList.add(String.valueOf(i));
            }
        }
        model.addAttribute("pageList", pageList);
        return "schedulePatient";
    }
    @PostMapping("schedule")
    public String getSchedulePost(HttpServletRequest request, RedirectAttributes redirectAttrs, HttpSession session,
                                  @ModelAttribute(name = "medicalDate") String medicalDate,
                                  @ModelAttribute(name = "isPay") String isPay,@ModelAttribute(name = "isDone") String isDone) {
        medicalDate = request.getParameter("medicalDate");
        isPay = request.getParameter("isPay");
        isDone = request.getParameter("isDone");
        String page = request.getParameter("page");
        redirectAttrs.addFlashAttribute("medicalDate", medicalDate);
        redirectAttrs.addFlashAttribute("isPay", isPay);
        redirectAttrs.addFlashAttribute("isDone", isDone);
        redirectAttrs.addFlashAttribute("page", page);
        return "redirect:/patient/schedule";
    }

    @GetMapping("/schedule/detail1")
    public String getScheduleDetail1(RedirectAttributes redirectAttrs, HttpSession session, @RequestParam String scheduleId,@ModelAttribute(name = "message") String message){
        DetailDoctorScheduleRequest detailDoctorScheduleRequest = new DetailDoctorScheduleRequest();
        detailDoctorScheduleRequest.setScheduleId(Long.parseLong(scheduleId));
        SchedulesResponse schedulesResponse = patientService.getScheduleDetail(detailDoctorScheduleRequest, session.getAttribute("token").toString());
        redirectAttrs.addFlashAttribute("schedulesResponse",schedulesResponse);
        System.out.println(schedulesResponse);
        redirectAttrs.addFlashAttribute("message",message);
        return "redirect:/patient/schedule/detail";
    }
    @GetMapping("/schedule/detail")
    public String getScheduleDetail(){
//        DetailDoctorScheduleRequest detailDoctorScheduleRequest = new DetailDoctorScheduleRequest();
//        detailDoctorScheduleRequest.setScheduleId(Long.parseLong(scheduleId));
//        SchedulesResponse schedulesResponse = patientService.getScheduleDetail(detailDoctorScheduleRequest, session.getAttribute("token").toString());
//        model.addAttribute("schedulesResponse",schedulesResponse);
//        System.out.println(schedulesResponse);
        return "schedulePatientDetail";
    }
    @PostMapping("/searchParent")
    public String searchParent(RedirectAttributes redirectAttrs, HttpSession session, @RequestParam String phoneNumber,
                               @ModelAttribute(name = "message") String message) throws JsonProcessingException {
        BaseResponse baseResponse = patientService.searchParent(phoneNumber, session.getAttribute("token").toString());
        List<PatientDetail> listPatient = new ArrayList<>();
        if(baseResponse.getMessageCode() == null){
            SearchPatientResponse searchPatientResponse = objectMapper.readValue(objectMapper.writeValueAsString(baseResponse.getData()).toString(), SearchPatientResponse.class);
            CreatePatientResponse createPatientResponse = new CreatePatientResponse();
            createPatientResponse.setParentDetail(searchPatientResponse.getParentDetail());
            createPatientResponse.setPatientDetail(searchPatientResponse.getPatientDetails().get(0));
            redirectAttrs.addFlashAttribute("createPatientResponse", createPatientResponse);
            listPatient = searchPatientResponse.getPatientDetails();
            redirectAttrs.addFlashAttribute("listPatient", listPatient);
        }else{
            redirectAttrs.addFlashAttribute("createPatientResponse", new CreatePatientResponse());
            redirectAttrs.addFlashAttribute("listPatient", listPatient);
            message = baseResponse.getMessage();
        }
        ParentDetail parentDetail = new ParentDetail();
        parentDetail.setPhoneNumber(phoneNumber);
//        redirectAttrs.addFlashAttribute("schedulesResponse",schedulesResponse);
//        System.out.println(schedulesResponse);
        System.out.println(phoneNumber);
        System.out.println(message);
        redirectAttrs.addFlashAttribute("message",message);
        redirectAttrs.addFlashAttribute("parentDetail",parentDetail);
        return "redirect:/patientProfile";
    }
    @PostMapping("/savePatient")
    public String savePatient(RedirectAttributes redirectAttrs, HttpSession session,
                              @ModelAttribute(name = "createPatientResponse") CreatePatientResponse createPatientResponse,
                              @ModelAttribute(name = "message") String message) throws JsonProcessingException {
        System.out.println(createPatientResponse.toString());
        CreatePatientRequest createPatientRequest = new CreatePatientRequest();
        createPatientRequest.setPhoneNumber(createPatientResponse.getParentDetail().getPhoneNumber());
        createPatientRequest.setAddress(createPatientResponse.getParentDetail().getAddress());
        createPatientRequest.setFullName(createPatientResponse.getParentDetail().getFullName());
        createPatientRequest.setPatientDetail(createPatientResponse.getPatientDetail());
        BaseResponse baseResponse = patientService.savePatient(createPatientRequest,session.getAttribute("token").toString());
        if (baseResponse.getMessageCode() == null){
            System.out.println("luu dc r");
            createPatientResponse = objectMapper.readValue(objectMapper.writeValueAsString(baseResponse.getData()).toString(), CreatePatientResponse.class);
        }else{

            message = baseResponse.getMessage();
            System.out.println(message);
        }

//        SearchPatientResponse searchPatientResponse = patientService.searchParent(phoneNumber, session.getAttribute("token").toString());
//        ParentDetail parentDetail = new ParentDetail();
//        parentDetail.setPhoneNumber(phoneNumber);
//        if (searchPatientResponse != null && searchPatientResponse.getParentDetail() != null){
//            redirectAttrs.addFlashAttribute("createPatientResponse", new CreatePatientResponse());
//        }else{
//            redirectAttrs.addFlashAttribute("createPatientResponse", new CreatePatientResponse());
//            message = "Không tìm thấy ai";
//        }
////        redirectAttrs.addFlashAttribute("schedulesResponse",schedulesResponse);
////        System.out.println(schedulesResponse);
//        System.out.println(phoneNumber);
//        System.out.println(message);
//        redirectAttrs.addFlashAttribute("message",message);
        redirectAttrs.addFlashAttribute("createPatientResponse",createPatientResponse);
        redirectAttrs.addFlashAttribute("listPatient", createPatientResponse.getPatientDetails());
        return "redirect:/patientProfile";
    }

    @GetMapping("/history")
    public String history(Model model, HttpSession session,
                          @RequestParam("patientId") Long patientId,
                          @ModelAttribute(name = "message") String message) throws JsonProcessingException {
        DetailPatientResponse detailPatientResponse = new DetailPatientResponse();
        BaseResponse baseResponse = patientService.getPatientById(patientId, session.getAttribute("token").toString());
        if (baseResponse.getMessageCode() == null){
            detailPatientResponse = objectMapper.readValue(objectMapper.writeValueAsString(baseResponse.getData()).toString(), DetailPatientResponse.class);
        }else{
            message = baseResponse.getMessage();
        }
        model.addAttribute("detailPatientResponse", detailPatientResponse);
        model.addAttribute("histories", detailPatientResponse.getHistories());
        return "patientHistories";
    }
    @GetMapping("/medical")
    public String medicalUI(Model model, HttpSession session,
                        @ModelAttribute(name = "detailPatientResponse") DetailPatientResponse detailPatientResponse,
                        @ModelAttribute(name = "medicalPatientRequest") MedicalPatientRequest medicalPatientRequest,
                        @ModelAttribute(name = "message") String message) throws JsonProcessingException {

        medicalPatientRequest = new MedicalPatientRequest();
//        medicalPatientRequest.getMedicines().add(new MedicineDTO());
        medicalPatientRequest.setPatientId(detailPatientResponse.getPatientDetail().getId());
        PatientInfoUI patientInfoUI = new PatientInfoUI();
        patientInfoUI.setParentDetail(detailPatientResponse.getParentDetail());
        patientInfoUI.setPatientDetail(detailPatientResponse.getPatientDetail());
        medicalPatientRequest.setPatientInfo(patientInfoUI);
//        System.out.println(patientInfoUI);
//        model.addAttribute("detailPatientResponse", detailPatientResponse);
        model.addAttribute("medicalPatientRequest", medicalPatientRequest);
        model.addAttribute("patientInfoUI", patientInfoUI);
        model.addAttribute("token", session.getAttribute("token").toString());
        return "medicalPatient";
    }

    @PostMapping("/medical")
    public String medical(Model model, HttpSession session,
                          @ModelAttribute(name = "detailPatientResponse") DetailPatientResponse detailPatientResponse,
                          @ModelAttribute(name = "medicalPatientRequest") MedicalPatientRequest medicalPatientRequest,
                          @ModelAttribute(name = "patientInfoUI") PatientInfoUI patientInfoUI,
                        @RequestParam String phoneNumberP,
                          @RequestParam String fullNameP,
                          @RequestParam String addressP,
                          @RequestParam Long idP,
                        @ModelAttribute(name = "message") String message) throws JsonProcessingException {
        ParentDetail parentDetail = new ParentDetail(idP,phoneNumberP,fullNameP,addressP);
        patientInfoUI.setParentDetail(parentDetail);
        DetailPatientProfileResponse detailPatientProfileResponse = new DetailPatientProfileResponse();
        BaseResponse baseResponse = doctorService.updateScheduleDetail(medicalPatientRequest, session.getAttribute("token").toString());
        if (baseResponse.getMessageCode() == null){
            detailPatientProfileResponse = objectMapper.readValue(objectMapper.writeValueAsString(baseResponse.getData()).toString(), DetailPatientProfileResponse.class);
        }else{
            message = baseResponse.getMessage();
        }
        model.addAttribute("detailPatientProfileResponse", detailPatientProfileResponse);
        model.addAttribute("patientInfo", medicalPatientRequest.getPatientInfo());
        model.addAttribute("medicines", detailPatientProfileResponse.getMedicines());
        model.addAttribute("patientInfoUI", patientInfoUI);
        return "resultMedicalPatient";
    }
}
