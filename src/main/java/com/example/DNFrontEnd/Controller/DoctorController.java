package com.example.DNFrontEnd.Controller;

import com.example.DNFrontEnd.Model.BaseResponse;
import com.example.DNFrontEnd.Model.request.DetailDoctorScheduleRequest;
import com.example.DNFrontEnd.Model.request.ListDoctorScheduleRequest;
import com.example.DNFrontEnd.Model.request.LoginRequest;
import com.example.DNFrontEnd.Model.request.UpdateScheduleRequest;
import com.example.DNFrontEnd.Model.response.BasePaginationResponse;
import com.example.DNFrontEnd.Model.response.PatientResponse;
import com.example.DNFrontEnd.Model.response.SchedulesResponse;
import com.example.DNFrontEnd.Service.DoctorService;
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
@RequestMapping({"/doctor"})
public class DoctorController {
    @Autowired
    DoctorService doctorService;
    ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    @GetMapping("schedule")
    public String getSchedule(Model model,HttpSession session,
                              @ModelAttribute(name = "medicalDate") String medicalDate, @ModelAttribute(name = "page") String page,
                              @ModelAttribute(name = "isPay") String isPay,@ModelAttribute(name = "isDone") String isDone) throws JsonProcessingException {
        String doctorId = session.getAttribute("id").toString();
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
        ListDoctorScheduleRequest listDoctorScheduleRequest = new ListDoctorScheduleRequest();
        listDoctorScheduleRequest.setDoctorId(Long.parseLong(doctorId));
        listDoctorScheduleRequest.setMedicalDate(medicalDate);
        if(!StringUtils.isEmpty(isPay)){
            if(isPay.equalsIgnoreCase("true")){
                listDoctorScheduleRequest.setIsPay(true);
                model.addAttribute("isPay", isPay);
            }
            if(isPay.equalsIgnoreCase("false")){
                listDoctorScheduleRequest.setIsPay(false);
                model.addAttribute("isPay", isPay);
            }
        }
        if(!StringUtils.isEmpty(isDone)){
            if(isDone.equalsIgnoreCase("true")){
                listDoctorScheduleRequest.setIsDone(true);
                model.addAttribute("isDone", isDone);
            }
            if(isDone.equalsIgnoreCase("false")){
                listDoctorScheduleRequest.setIsDone(false);
                model.addAttribute("isDone", isDone);
            }
        }
        page = StringUtils.isEmpty(page) ? "0" : String.valueOf(Integer.parseInt(page) - 1);
        model.addAttribute("page", String.valueOf(Integer.parseInt(page) + 1));
        BasePaginationResponse basePaginationResponse = doctorService.getSchedule(listDoctorScheduleRequest, session.getAttribute("token").toString(),page);
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
        return "scheduleDoctor";
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
