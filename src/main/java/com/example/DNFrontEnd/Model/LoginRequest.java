package com.example.DNFrontEnd.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class LoginRequest {

    @NotNull(message = "không null")
    private String email;

    @NotNull(message = "không null")
    private String password;

    public static String convertToString(LoginRequest loginRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(loginRequest);
        return result;
    }
}
