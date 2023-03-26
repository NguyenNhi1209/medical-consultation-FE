package com.example.DNFrontEnd.Model.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String email;
    private String password;
    private String phone;
    private String fullName;
    private String birthday;
    private String sex;

    public static String convertToString(RegisterRequestDTO registerRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(registerRequest);
        return result;
    }
}
