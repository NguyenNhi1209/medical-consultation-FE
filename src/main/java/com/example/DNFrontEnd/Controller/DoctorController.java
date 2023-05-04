package com.example.DNFrontEnd.Controller;

import com.example.DNFrontEnd.Model.BaseResponse;
import com.example.DNFrontEnd.Model.request.DetailDoctorScheduleRequest;
import com.example.DNFrontEnd.Model.request.ListDoctorScheduleRequest;
import com.example.DNFrontEnd.Model.request.LoginRequest;
import com.example.DNFrontEnd.Model.request.UpdateScheduleRequest;
import com.example.DNFrontEnd.Model.response.PatientResponse;
import com.example.DNFrontEnd.Model.response.SchedulesResponse;
import com.example.DNFrontEnd.Service.DoctorService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
@RequestMapping({"/doctor"})
public class DoctorController {
    @Autowired
    DoctorService doctorService;

    @GetMapping("schedule")
    public String getSchedule(Model model,HttpSession session,@ModelAttribute(name = "medicalDate") String medicalDate) {
        String doctorId = session.getAttribute("id").toString();
        DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter format2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        if(!StringUtils.isEmpty(medicalDate)){
            LocalDate localDate = LocalDate.parse(medicalDate, format1);
            model.addAttribute("medicalDate", format2.format(localDate));
            model.addAttribute("medicalDate1", medicalDate);
        }else{
            LocalDate localDate1 = LocalDate.now();//For reference
            medicalDate = localDate1.format(format1);
            model.addAttribute("medicalDate", format2.format(localDate1));
            model.addAttribute("medicalDate1", medicalDate);
        }
        ListDoctorScheduleRequest listDoctorScheduleRequest = new ListDoctorScheduleRequest();
        listDoctorScheduleRequest.setDoctorId(Long.parseLong(doctorId));
        listDoctorScheduleRequest.setMedicalDate(medicalDate);
        List<SchedulesResponse> schedulesResponseList = doctorService.getSchedule(listDoctorScheduleRequest, session.getAttribute("token").toString());
        System.out.println(schedulesResponseList);
        model.addAttribute("schedulesResponseList", schedulesResponseList);

        return "scheduleDoctor";
    }

    @PostMapping("schedule")
    public String getSchedulePost(HttpServletRequest request, RedirectAttributes redirectAttrs, HttpSession session, @ModelAttribute(name = "medicalDate") String medicalDate) {
        medicalDate = request.getParameter("medicalDate");
        redirectAttrs.addFlashAttribute("medicalDate", medicalDate);
        return "redirect:/doctor/schedule";
    }

    @GetMapping("/schedule/detail")
    public String getScheduleDetail(Model model, HttpSession session, @RequestParam String scheduleId){
        DetailDoctorScheduleRequest detailDoctorScheduleRequest = new DetailDoctorScheduleRequest();
        detailDoctorScheduleRequest.setScheduleId(Long.parseLong(scheduleId));
        SchedulesResponse schedulesResponse = doctorService.getScheduleDetail(detailDoctorScheduleRequest, session.getAttribute("token").toString());
        model.addAttribute("schedulesResponse",schedulesResponse);
        System.out.println(schedulesResponse);
        return "scheduleDoctorDetail";
    }

    @PostMapping("/schedule/update")
    public String updateSchedule(HttpServletRequest request, RedirectAttributes redirectAttrs, HttpSession session, @ModelAttribute(name = "medicalDate") String medicalDate) {
        medicalDate = request.getParameter("medicalDate");
        redirectAttrs.addFlashAttribute("medicalDate", medicalDate);
        String diagnostic = request.getParameter("diagnostic");
        String scheduleId = request.getParameter("scheduleId");
        UpdateScheduleRequest updateScheduleRequest = new UpdateScheduleRequest();
        if(!StringUtils.isEmpty(diagnostic)){
            updateScheduleRequest.setDiagnostic(diagnostic);
            updateScheduleRequest.setPay(true);
            updateScheduleRequest.setScheduleId(Long.parseLong(scheduleId));
            BaseResponse baseResponse = doctorService.updateScheduleDetail(updateScheduleRequest,session.getAttribute("token").toString());
        }

        return "redirect:/doctor/schedule/detail?scheduleId=" + scheduleId;
    }

}
