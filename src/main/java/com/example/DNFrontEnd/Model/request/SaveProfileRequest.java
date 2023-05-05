package com.example.DNFrontEnd.Model.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class SaveProfileRequest {
    private Long id;
    private String fullName;
    private String sex;
    private String identityNumber;
    private String phoneNumber;
    public static String convertToString(SaveProfileRequest saveProfileRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(saveProfileRequest);
        return result;
    }
}
