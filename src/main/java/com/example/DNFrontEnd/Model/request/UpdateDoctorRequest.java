package com.example.DNFrontEnd.Model.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class UpdateDoctorRequest {
    private Long id;
    private String name;
    private String sex;
    private String identityNumber;
    private String phoneNumber;
    public static String convertToString(UpdateDoctorRequest updateDoctorRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(updateDoctorRequest);
        return result;
    }
}
