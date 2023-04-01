package com.example.DNFrontEnd.Model.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String password1;
    private String phoneNumber;
    private String fullName;
    private String birthday;
    private String sex;

    public static String convertToString(RegisterRequest registerRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(registerRequest);
        return result;
    }
}
