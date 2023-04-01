package com.example.DNFrontEnd.Model.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class CreatePatientProfileRequest {
    public String symptom;
    public static String convertToString(CreatePatientProfileRequest createPatientProfileRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(createPatientProfileRequest);
        return result;
    }

}