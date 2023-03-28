package com.example.DNFrontEnd.Model.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class LoginRequest {

    private String email;

    private String password;

    private Boolean loginWithDoctor;

    public static String convertToString(LoginRequest loginRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(loginRequest);
        return result;
    }
}
