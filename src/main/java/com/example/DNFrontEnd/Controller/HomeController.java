package com.example.DNFrontEnd.Controller;

import com.example.DNFrontEnd.Model.Contanst;
import com.example.DNFrontEnd.Model.request.LoginRequest;
import com.example.DNFrontEnd.Model.request.RegisterRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping({ "/","/home"})
public class HomeController {

    @GetMapping
    public String home(Model model,@ModelAttribute(name = "name") String name, @ModelAttribute(name = "userType") String userType)  {
//        if (name != "" && name != null) {
//            model.addAttribute("name", name);
//        }else{
//            model.addAttribute("name", "");
//        }
//        if (userType != "" && userType != null) {
//            model.addAttribute("userType", userType);
//        }
//        else {
//            model.addAttribute("userType", "");
//        }
        if (StringUtils.isEmpty(Contanst.name)) {
            model.addAttribute("name", "");
        }else{
            model.addAttribute("name", Contanst.name);
        }
        if (StringUtils.isEmpty(Contanst.userType)) {
            model.addAttribute("userType", "");
        }else {
            model.addAttribute("userType", Contanst.userType);
        }
        return "home";
    }
    @GetMapping("/booking")
    public String booking(Model model,@ModelAttribute(name = "name") String name, @ModelAttribute(name = "userType") String userType)  {
        if(StringUtils.isEmpty(Contanst.token)){
            model.addAttribute("loginRequest", new LoginRequest());
            return "login";
        }
        if (StringUtils.isEmpty(Contanst.name)) {
            model.addAttribute("name", "");
        }else{
            model.addAttribute("name", Contanst.name);
        }
        if (StringUtils.isEmpty(Contanst.userType)) {
            model.addAttribute("userType", "");
        }else {
            model.addAttribute("userType", Contanst.userType);
        }
        return "booking";
    }
    @GetMapping("/updateProfile")
    public String updateProfile(Model model,@ModelAttribute("registerRequest") RegisterRequest registerRequest, @ModelAttribute(name = "error") String error)  {
        if (error != "" && error != null) {
            model.addAttribute("registerRequest", registerRequest);
            model.addAttribute("error", error);
        } else {
            model.addAttribute("registerRequest", new RegisterRequest());
            model.addAttribute("error", "");
        }
        //get patien/doctor by userID
        if(StringUtils.isEmpty(Contanst.token)){
            model.addAttribute("loginRequest", new LoginRequest());
            return "login";
        }
        if (StringUtils.isEmpty(Contanst.name)) {
            model.addAttribute("name", "");
        }else{
            model.addAttribute("name", Contanst.name);
        }
        if (StringUtils.isEmpty(Contanst.userType)) {
            model.addAttribute("userType", "");
        }else {
            model.addAttribute("userType", Contanst.userType);
        }
        if(Contanst.userProfileResponse != null){
            if(Contanst.userType.equalsIgnoreCase("PATIENT")){
                return "updateProfilePatient";
            }
            if(Contanst.userType.equalsIgnoreCase("DOCTOR")){
                return "updateProfileDoctor";
            }
        }
        return "home";
    }
    @GetMapping("/introduce")
    public String introduce(Model model)  {
        if (StringUtils.isEmpty(Contanst.name)) {
            model.addAttribute("name", "");
        }else{
            model.addAttribute("name", Contanst.name);
        }
        if (StringUtils.isEmpty(Contanst.userType)) {
            model.addAttribute("userType", "");
        }else {
            model.addAttribute("userType", Contanst.userType);
        }
        return "introduce";
    }
}
