package com.example.DNFrontEnd.Controller;

import com.example.DNFrontEnd.Model.BaseResponse;
import com.example.DNFrontEnd.Model.Contanst;
import com.example.DNFrontEnd.Model.request.LoginRequest;
import com.example.DNFrontEnd.Model.request.RegisterRequest;
import com.example.DNFrontEnd.Model.request.SavePatientRequest;
import com.example.DNFrontEnd.Model.response.PatientResponse;
import com.example.DNFrontEnd.Model.response.UserProfileResponse;
import com.example.DNFrontEnd.Service.AuthService;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping({"/", "/home"})
public class HomeController {
    @Autowired
    PatientService patientService;

    ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

    @GetMapping
    public String home(Model model, @ModelAttribute(name = "name") String name,
                       @ModelAttribute(name = "userType") String userType, HttpSession session) {


        return "home";
    }

    @GetMapping("/booking")
    public String booking(Model model, HttpSession session) {
        if (session.getAttribute("token") == null || StringUtils.isEmpty(session.getAttribute("token").toString())) {
            model.addAttribute("loginRequest", new LoginRequest());
            return "login";
        }
        return "booking";
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
    public String updateProfile(Model model, HttpServletRequest request, RedirectAttributes redirectAttrs, HttpSession session, @ModelAttribute("patientResponse") PatientResponse patientResponse) throws JsonProcessingException {
        System.out.println("vô nè");

        SavePatientRequest savePatientRequest = new SavePatientRequest();
        savePatientRequest.setFullName(patientResponse.getFullName());
        savePatientRequest.setBirthday(patientResponse.getBirthday());
        savePatientRequest.setSex(patientResponse.getSex());
        savePatientRequest.setAddress(patientResponse.getAddress());
        savePatientRequest.setJob(patientResponse.getJob());
        savePatientRequest.setIdentityNumber(patientResponse.getIdentityNumber());
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
}
