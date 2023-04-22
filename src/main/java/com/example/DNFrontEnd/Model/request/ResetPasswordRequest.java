package com.example.DNFrontEnd.Model.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String code;
    private String password;
    public static String convertToString(ResetPasswordRequest resetPasswordRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(resetPasswordRequest);
        return result;
    }
}
