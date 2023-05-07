package com.example.DNFrontEnd.Model.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class ListDoctorRequest {
    private Long departmentId;
    public static String convertToString(ListDoctorRequest listDoctorRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(listDoctorRequest);
        return result;
    }
}
