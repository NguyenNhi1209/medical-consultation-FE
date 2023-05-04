package com.example.DNFrontEnd.Controller;

import com.example.DNFrontEnd.Model.request.DetailDoctorScheduleRequest;
import com.example.DNFrontEnd.Model.request.ListDoctorScheduleRequest;
import com.example.DNFrontEnd.Model.request.ListPatientScheduleRequest;
import com.example.DNFrontEnd.Model.response.SchedulesResponse;
import com.example.DNFrontEnd.Service.PatientService;
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
import java.util.List;

@Controller
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    PatientService patientService;

    @GetMapping("/schedule")
    public String getSchedule(Model model, HttpSession session, @ModelAttribute(name = "medicalDate") String medicalDate) {
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
        System.out.println(listPatientScheduleRequest.toString());
        List<SchedulesResponse> schedulesResponseList = patientService.getSchedule(listPatientScheduleRequest, session.getAttribute("token").toString());
        System.out.println(schedulesResponseList);
        model.addAttribute("schedulesResponseList", schedulesResponseList);

        return "schedulePatient";
    }
    @PostMapping("schedule")
    public String getSchedulePost(HttpServletRequest request, RedirectAttributes redirectAttrs, HttpSession session, @ModelAttribute(name = "medicalDate") String medicalDate) {
        medicalDate = request.getParameter("medicalDate");
        redirectAttrs.addFlashAttribute("medicalDate", medicalDate);
        return "redirect:/patient/schedule";
    }

    @GetMapping("/schedule/detail1")
    public String getScheduleDetail1(RedirectAttributes redirectAttrs, HttpSession session, @RequestParam String scheduleId){
        DetailDoctorScheduleRequest detailDoctorScheduleRequest = new DetailDoctorScheduleRequest();
        detailDoctorScheduleRequest.setScheduleId(Long.parseLong(scheduleId));
        SchedulesResponse schedulesResponse = patientService.getScheduleDetail(detailDoctorScheduleRequest, session.getAttribute("token").toString());
        redirectAttrs.addFlashAttribute("schedulesResponse",schedulesResponse);
        System.out.println(schedulesResponse);
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
