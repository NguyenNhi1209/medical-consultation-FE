package com.example.DNFrontEnd.Controller;

import com.example.DNFrontEnd.Model.request.DetailDoctorScheduleRequest;
import com.example.DNFrontEnd.Model.request.ListDoctorScheduleRequest;
import com.example.DNFrontEnd.Model.request.ListPatientScheduleRequest;
import com.example.DNFrontEnd.Model.response.BasePaginationResponse;
import com.example.DNFrontEnd.Model.response.SchedulesResponse;
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

}
