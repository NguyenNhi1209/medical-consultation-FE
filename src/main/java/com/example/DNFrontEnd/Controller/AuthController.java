package com.example.DNFrontEnd.Controller;

import com.example.DNFrontEnd.Model.AuthMessageCode;
import com.example.DNFrontEnd.Model.BaseResponse;
import com.example.DNFrontEnd.Model.Contanst;
import com.example.DNFrontEnd.Model.request.*;
import com.example.DNFrontEnd.Model.response.LoginResponse;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping({"/user"})
public class AuthController {

    @Autowired
    AuthService authService;

    ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String success, Model model,
                        @ModelAttribute("loginRequest") LoginRequest loginRequest, @ModelAttribute(name = "error") String error) {
//        model.addAttribute("loginRequest", new LoginRequest());
//        return "login";

        if (error != "" && error != null) {
            model.addAttribute("loginRequest", loginRequest);
            model.addAttribute("error", error);
        } else {
            model.addAttribute("loginRequest", new LoginRequest());
            model.addAttribute("error", "");
        }

        return "login";
    }

    @PostMapping("/login")
    public String login(Model model, HttpServletRequest request, RedirectAttributes redirectAttrs, @ModelAttribute("loginRequest") LoginRequest loginRequest,
                        HttpSession session) {

        if (StringUtils.isEmpty(loginRequest.getEmail())) {
            redirectAttrs.addFlashAttribute("error", "Nhập email");
            redirectAttrs.addFlashAttribute("loginRequest", loginRequest);
            return "redirect:/user/login";
        }
        if (!loginRequest.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            redirectAttrs.addFlashAttribute("error", "Email không hợp lệ");
            redirectAttrs.addFlashAttribute("loginRequest", loginRequest);
            return "redirect:/user/login";
        }
        if (StringUtils.isEmpty(loginRequest.getPassword())) {
            redirectAttrs.addFlashAttribute("error", "Nhập mật khẩu");
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
        String isDoctor =  request.getParameter("loginWithDoctor");
        if(isDoctor != null){
            user.setLoginWithDoctor(true);
        }else{
            user.setLoginWithDoctor(false);
        }
        System.out.println(user.toString());

        try {
            BaseResponse baseResponse = authService.login(user);
            if (baseResponse != null) {
                if (baseResponse.getCode() == 1) {
                    LoginResponse loginResponse = objectMapper.readValue(objectMapper.writeValueAsString(baseResponse.getData()).toString(), LoginResponse.class);
                    if (loginResponse != null) {
                        request.getSession().setAttribute("Authorization", loginResponse.getToken());
                        BaseResponse userProfileResponseBaseResponse = authService.getProfileUser(loginResponse.getToken());
                        session.setAttribute("token", loginResponse.getToken());
//                        Contanst.token = loginResponse.getToken();
                        System.out.println(userProfileResponseBaseResponse);
                        UserProfileResponse userProfileResponse = objectMapper.readValue(objectMapper.writeValueAsString(userProfileResponseBaseResponse.getData()).toString(), UserProfileResponse.class);

//                        Contanst.userProfileResponse = userProfileResponse;
                        redirectAttrs.addFlashAttribute("name", userProfileResponse.getName());
//                        Contanst.name = userProfileResponse.getName();
                        session.setAttribute("name",userProfileResponse.getName());
                        redirectAttrs.addFlashAttribute("userType", userProfileResponse.getType());
//                        Contanst.userType = userProfileResponse.getType();
                        session.setAttribute("userType",userProfileResponse.getType());
                        session.setAttribute("phone",userProfileResponse.getPhoneNumber());
                    }
                } else {
                    System.out.println(baseResponse.getMessage());

                    AuthMessageCode authMessageCode = AuthMessageCode.from(baseResponse.getMessageCode());
                    if (authMessageCode != null && !authMessageCode.getCode().equalsIgnoreCase(AuthMessageCode.UNKNOWN.getCode())) {
                        model.addAttribute("error", authMessageCode.getMessage());
                    } else {
                        model.addAttribute("error", baseResponse.getMessage());
                    }
                    return "login";
                }
            }

//            request.getSession().setAttribute("jwtResponse", (BaseResponse) baseResponse.getBody());

        } catch (HttpClientErrorException ex) {
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
    public String register(@RequestParam(required = false) String success, Model model, @ModelAttribute("registerRequest") RegisterRequest registerRequest, @ModelAttribute(name = "error") String error) {
        if (error != "" && error != null) {
            model.addAttribute("registerRequest", registerRequest);
            model.addAttribute("error", error);
        } else {
            model.addAttribute("registerRequest", new RegisterRequest());
            model.addAttribute("error", "");
        }

        return "register";
    }

    @PostMapping("/register")
    public String register(Model model, HttpServletRequest request, RedirectAttributes redirectAttrs, @ModelAttribute("registerRequest") RegisterRequest registerRequest) {
        if (StringUtils.isEmpty(registerRequest.getEmail())) {
            redirectAttrs.addFlashAttribute("error", "Nhập email");
            redirectAttrs.addFlashAttribute("registerRequest", registerRequest);
            return "redirect:/user/register";
        }
        if (!registerRequest.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            redirectAttrs.addFlashAttribute("error", "Email không hợp lệ");
            redirectAttrs.addFlashAttribute("registerRequest", registerRequest);
            return "redirect:/user/register";
        }
        if (StringUtils.isEmpty(registerRequest.getPassword())) {
            redirectAttrs.addFlashAttribute("error", "Nhập mật khẩu");
            redirectAttrs.addFlashAttribute("registerRequest", registerRequest);
            return "redirect:/user/register";
        }
        if (StringUtils.isEmpty(registerRequest.getPassword1())) {
            redirectAttrs.addFlashAttribute("error", "Nhập lại mật khẩu");
            redirectAttrs.addFlashAttribute("registerRequest", registerRequest);
            return "redirect:/user/register";
        }
        if (!registerRequest.getPassword1().equalsIgnoreCase(registerRequest.getPassword())) {
            redirectAttrs.addFlashAttribute("error", "Nhập lại mật khẩu không khớp");
            redirectAttrs.addFlashAttribute("registerRequest", registerRequest);
            return "redirect:/user/register";
        }
        if (!StringUtils.isEmpty(registerRequest.getPhoneNumber())
                && !registerRequest.getPhoneNumber().matches("^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$")) {
            redirectAttrs.addFlashAttribute("error", "Số điện thoại không hợp lệ");
            redirectAttrs.addFlashAttribute("registerRequest", registerRequest);
            return "redirect:/user/register";
        }
        if (!StringUtils.isEmpty(registerRequest.getBirthday())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate birthday = LocalDate.parse(registerRequest.getBirthday(), formatter);
            if (birthday.isAfter(LocalDate.now())) {
                redirectAttrs.addFlashAttribute("error", "Ngày sinh không hợp lệ");
                redirectAttrs.addFlashAttribute("registerRequest", registerRequest);
                return "redirect:/user/register";
            }
        }

        RegisterRequestDTO user = new RegisterRequestDTO();
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));
        user.setPhoneNumber(request.getParameter("phoneNumber"));
        user.setFullName(request.getParameter("fullName"));
        user.setBirthday(request.getParameter("birthday"));
        user.setSex(request.getParameter("sex"));
        System.out.println(user.toString());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        try {
            BaseResponse baseResponse = authService.register(user);
            System.out.println(baseResponse.toString());
            if(baseResponse != null){
                if(baseResponse.getCode() == 1){
                    return "register-success";
                }
            }
//            if(baseResponse != null){
//                if(baseResponse.getCode() == 1){
//                    LoginResponse loginResponse = objectMapper.readValue(objectMapper.writeValueAsString(baseResponse.getData()).toString(),LoginResponse.class);
//                    if(loginResponse != null){
//                        request.getSession().setAttribute("Authorization", loginResponse.getToken());
//                        BaseResponse userProfileResponseBaseResponse = authService.getProfileUser(loginResponse.getToken());
//                        System.out.println(userProfileResponseBaseResponse);
//                    }
//                }else{
//                    System.out.println(baseResponse.getMessage());
//
//                    AuthMessageCode authMessageCode = AuthMessageCode.from(baseResponse.getMessageCode());
//                    if(authMessageCode != null && !authMessageCode.getCode().equalsIgnoreCase("1111.0.Unknown")){
//                        model.addAttribute("error",authMessageCode.getMessage());
//                    }else{
//                        model.addAttribute("error",baseResponse.getMessage());
//                    }
//                    return "login";
//                }
//            }

//            request.getSession().setAttribute("jwtResponse", (BaseResponse) baseResponse.getBody());

        } catch (HttpClientErrorException ex) {
            ex.printStackTrace();
            System.out.println("catch roi");
//            return "home";
        }
//        catch (JsonMappingException e) {
//            e.printStackTrace();
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
        return "redirect:/";
    }

    @GetMapping("/auth/verify-email")
    public String active(@RequestParam("code") String code, @RequestParam("email") String email) {

        try {
            BaseResponse baseResponse = authService.verify(code);
            System.out.println(baseResponse.toString());

        } catch (HttpClientErrorException ex) {
            ex.printStackTrace();
            System.out.println("catch roi");
//            return "home";
        }


        return "active";
    }

    @GetMapping("/logout")
    public String login(Model model,HttpSession session) {

        BaseResponse baseResponse = authService.logout(session.getAttribute("token").toString());
        if(!StringUtils.isEmpty(baseResponse.getMessageCode()) && baseResponse.getMessageCode().equalsIgnoreCase(AuthMessageCode.AUTH_2_1.getCode())){
            session.invalidate();
            return "redirect:/";
        }
        return "home";
    }

    private void resetContanst(){
        Contanst.token = "";
        Contanst.name = "";
        Contanst.userType = "";
        Contanst.userProfileResponse = new UserProfileResponse();
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword(){
        return "forgotPassword";
    }
    @PostMapping("/forgotPassword")
    public String forgotPassword(Model model, HttpServletRequest request, RedirectAttributes redirectAttrs){
        if (StringUtils.isEmpty(request.getParameter("email"))) {
            redirectAttrs.addFlashAttribute("error", "Nhập email");
            return "redirect:/user/forgotPassword";
        }
        if (!request.getParameter("email").matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            redirectAttrs.addFlashAttribute("error", "Email không hợp lệ");
            return "redirect:/user/forgotPassword";
        }
        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setEmail(request.getParameter("email"));
        boolean result = authService.forgotPassword(forgotPasswordRequest);
        if(result){
            redirectAttrs.addFlashAttribute("error", "Đã gửi email xác thực, kiểm tra email của bạn");
            redirectAttrs.addFlashAttribute("code", "1");
            return "redirect:/user/forgotPassword";
        }else{
            redirectAttrs.addFlashAttribute("error", "Không tìm thấy email trong hệ thống");
            return "redirect:/user/forgotPassword";
        }

//        return "forgotPassword";
    }
    @GetMapping("/auth/reset-password")
    public String resetPassword(@RequestParam("code") String code, @RequestParam("email") String email,Model model, @ModelAttribute("resetPasswordRequest") ResetPasswordRequest resetPasswordRequest) {

        try {
            resetPasswordRequest.setCode(code);
            System.out.println(code);
            model.addAttribute("resetPasswordRequest", resetPasswordRequest);
        } catch (HttpClientErrorException ex) {
            ex.printStackTrace();
            System.out.println("catch roi");
            return "home";
        }
        return "resetPassword";
    }
    @GetMapping("/resetPassword")
    public String resetPassword1(Model model, @ModelAttribute("resetPasswordRequest") ResetPasswordRequest resetPasswordRequest) {
        try {
            model.addAttribute("resetPasswordRequest", resetPasswordRequest);
        } catch (HttpClientErrorException ex) {
            ex.printStackTrace();
            System.out.println("catch roi");
            return "home";
        }
        return "resetPassword";
    }
    @PostMapping("/resetPassword")
    public String resetPassword(Model model, HttpServletRequest request, RedirectAttributes redirectAttrs, @ModelAttribute("resetPasswordRequest") ResetPasswordRequest resetPasswordRequest) {
        if (StringUtils.isEmpty(resetPasswordRequest.getPassword())) {
            redirectAttrs.addFlashAttribute("error", "Nhập mật khẩu");
            redirectAttrs.addFlashAttribute("resetPasswordRequest", resetPasswordRequest);
            return "redirect:/user/resetPassword";
        }
        if (StringUtils.isEmpty(request.getParameter("password1"))) {
            redirectAttrs.addFlashAttribute("error", "Nhập lại mật khẩu");
            redirectAttrs.addFlashAttribute("resetPasswordRequest", resetPasswordRequest);
            return "redirect:/user/resetPassword";
        }
        if (!request.getParameter("password1").equalsIgnoreCase(resetPasswordRequest.getPassword())) {
            redirectAttrs.addFlashAttribute("error", "Nhập lại mật khẩu không khớp");
            redirectAttrs.addFlashAttribute("resetPasswordRequest", resetPasswordRequest);
            return "redirect:/user/resetPassword";
        }
        System.out.println(resetPasswordRequest.toString());
        boolean result = authService.resetPassword(resetPasswordRequest);
        if(result){
            redirectAttrs.addFlashAttribute("error", "Đặt lại mật khẩu thành công, đăng nhập lại để sử dụng ứng dụng");
            return "redirect:/user/login";
        }else{
            redirectAttrs.addFlashAttribute("resetPasswordRequest", resetPasswordRequest);
            redirectAttrs.addFlashAttribute("error", "Lỗi server");
            return "redirect:/user/resetPassword";
        }

    }

}
