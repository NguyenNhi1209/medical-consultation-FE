package com.example.DNFrontEnd.Model.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class SavePatientRequest {

    private Long id;
    private String fullName;
    private String birthday;
    private String sex;
    private String address;
    private String job;
    private String identityNumber;
    public static String convertToString(SavePatientRequest savePatientRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(savePatientRequest);
        return result;
    }
}
