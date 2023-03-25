package com.example.DNFrontEnd.Controller;

import com.example.DNFrontEnd.Model.AuthMessageCode;
import com.example.DNFrontEnd.Model.BaseResponse;
import com.example.DNFrontEnd.Model.LoginRequest;
import com.example.DNFrontEnd.Model.LoginResponse;
import com.example.DNFrontEnd.Model.response.UserProfileResponse;
import com.example.DNFrontEnd.Service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping({ "/user"})
public class AuthController {

    @Autowired
    AuthService authService;

    ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String success, Model model, @ModelAttribute("loginRequest") LoginRequest loginRequest, @ModelAttribute(name = "error") String error){
//        model.addAttribute("loginRequest", new LoginRequest());
//        return "login";

        if(error!=""&&error!=null){
            model.addAttribute("loginRequest", loginRequest);
            model.addAttribute("error", error);
        }else {
            model.addAttribute("loginRequest", new LoginRequest());
            model.addAttribute("error", "");
        }

        return "login";
    }
    @PostMapping("/login")
    public String login(Model model, HttpServletRequest request, RedirectAttributes redirectAttrs,@ModelAttribute("loginRequest") LoginRequest loginRequest ){
        if(StringUtils.isEmpty(loginRequest.getEmail())){
            redirectAttrs.addFlashAttribute("error","Nhập email");
            redirectAttrs.addFlashAttribute("loginRequest", loginRequest);
            return "redirect:/user/login";
        }
        if(!loginRequest.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            redirectAttrs.addFlashAttribute("error","Email không hợp lệ");
            redirectAttrs.addFlashAttribute("loginRequest", loginRequest);
            return "redirect:/user/login";
        }
        if(StringUtils.isEmpty(loginRequest.getPassword())){
            redirectAttrs.addFlashAttribute("error","Nhập mật khẩu");
            redirectAttrs.addFlashAttribute("loginRequest", loginRequest);
            return "redirect:/user/login";
        }
        System.out.println("user.toString()");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        User user = new User();
//        user.setUsername(request.getParameter("username"));
//        user.setPassword(request.getParameter("password"));
        LoginRequest user = new LoginRequest();
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));
        System.out.println(user.toString());

        try {
            BaseResponse baseResponse = authService.login(user);
            if(baseResponse != null){
                if(baseResponse.getCode() == 1){
                    LoginResponse loginResponse = objectMapper.readValue(objectMapper.writeValueAsString(baseResponse.getData()).toString(),LoginResponse.class);
                    if(loginResponse != null){
                        request.getSession().setAttribute("Authorization", loginResponse.getToken());
                        BaseResponse userProfileResponseBaseResponse = authService.getProfileUser(loginResponse.getToken());
                        System.out.println(userProfileResponseBaseResponse);
                    }
                }else{
                    System.out.println(baseResponse.getMessage());

                    AuthMessageCode authMessageCode = AuthMessageCode.from(baseResponse.getMessageCode());
                    if(authMessageCode != null && !authMessageCode.getCode().equalsIgnoreCase("1111.0.Unknown")){
                        model.addAttribute("error",authMessageCode.getMessage());
                    }else{
                        model.addAttribute("error",baseResponse.getMessage());
                    }
                    return "login";
                }
            }

//            request.getSession().setAttribute("jwtResponse", (BaseResponse) baseResponse.getBody());

        }
        catch (HttpClientErrorException ex){
            ex.printStackTrace();
            System.out.println("catch roi");
            return "home";
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
    @GetMapping("/register")
    public String register(@RequestParam(required = false) String success, Model model, String error){
        return "register";
    }
}
