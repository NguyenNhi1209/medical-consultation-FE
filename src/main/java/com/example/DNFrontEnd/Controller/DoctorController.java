package com.example.DNFrontEnd.Controller;

import com.example.DNFrontEnd.Model.request.ListDoctorScheduleRequest;
import com.example.DNFrontEnd.Model.response.SchedulesResponse;
import com.example.DNFrontEnd.Service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

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
//        private Long doctorId;
//        private String medicalDate;
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

}
