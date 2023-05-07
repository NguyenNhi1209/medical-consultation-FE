package com.example.DNFrontEnd.Service;

import com.example.DNFrontEnd.Model.BaseResponse;
import com.example.DNFrontEnd.Model.ERROR;
import com.example.DNFrontEnd.Model.request.ForgotPasswordRequest;
import com.example.DNFrontEnd.Model.request.LoginRequest;
import com.example.DNFrontEnd.Model.DTO.RegisterRequestDTO;
import com.example.DNFrontEnd.Model.request.ResetPasswordRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

@Service
public class AuthService {

    @Value("${admin.rest.url:}")
    private String adminUrl;

    private Logger logger = Logger.getLogger(getClass().getName());


    public BaseResponse login(LoginRequest loginRequest, Boolean isAdmin) {
        BaseResponse baseResponse ;
        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/auth/login"))
                    .header("accept", "application/json")
//                .header("Authorization", "Bearer ")
                    .header("content-type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(LoginRequest.convertToString(loginRequest)))
                    .build();
            if(isAdmin){
                request = HttpRequest.newBuilder()
                        .uri(URI.create(adminUrl+"/admin/login"))
                        .header("accept", "application/json")
//                .header("Authorization", "Bearer ")
                        .header("content-type", "application/json")
                        .method("POST", HttpRequest.BodyPublishers.ofString(LoginRequest.convertToString(loginRequest)))
                        .build();
            }
            HttpResponse response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

            baseResponse = objectMapper.readValue(response.body().toString(),BaseResponse.class);

            System.out.println(baseResponse);
            return  baseResponse;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public BaseResponse getProfileUser(String token)  {
        BaseResponse baseResponse;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/auth/profile"))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .header("content-type", "application/json")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

            baseResponse = objectMapper.readValue(response.body().toString(),BaseResponse.class);
            return  baseResponse;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public BaseResponse register(RegisterRequestDTO registerRequest) {
        BaseResponse baseResponse ;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/auth/register"))
                    .header("accept", "application/json")
//                .header("Authorization", "Bearer ")
                    .header("content-type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(RegisterRequestDTO.convertToString(registerRequest)))
                    .build();
            HttpResponse response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

            baseResponse = objectMapper.readValue(response.body().toString(),BaseResponse.class);

            System.out.println(baseResponse);
            return  baseResponse;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public BaseResponse verify(String code)  {
        BaseResponse baseResponse;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/auth/verify-email?code=" + code))
                    .header("accept", "application/json")
//                    .header("Authorization", "Bearer " + token)
                    .header("content-type", "application/json")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

            baseResponse = objectMapper.readValue(response.body().toString(),BaseResponse.class);
            return  baseResponse;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public BaseResponse logout(String token) {
        BaseResponse baseResponse;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/auth/logout"))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .header("content-type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

            baseResponse = objectMapper.readValue(response.body().toString(),BaseResponse.class);
            return  baseResponse;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public boolean forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        BaseResponse baseResponse ;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/auth/forgot-password"))
                    .header("accept", "application/json")
//                .header("Authorization", "Bearer ")
                    .header("content-type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(ForgotPasswordRequest.convertToString(forgotPasswordRequest)))
                    .build();
            HttpResponse response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

            baseResponse = objectMapper.readValue(response.body().toString(),BaseResponse.class);
            System.out.println(baseResponse);
            return baseResponse.getCode() == ERROR.SUCCESS.getCode();
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean resetPassword(ResetPasswordRequest resetPasswordRequest) {
        BaseResponse baseResponse ;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/auth/reset-password"))
                    .header("accept", "application/json")
//                .header("Authorization", "Bearer ")
                    .header("content-type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(ResetPasswordRequest.convertToString(resetPasswordRequest)))
                    .build();
            HttpResponse response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

            baseResponse = objectMapper.readValue(response.body().toString(),BaseResponse.class);

            System.out.println(baseResponse);
            return baseResponse.getCode() == ERROR.SUCCESS.getCode();
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
