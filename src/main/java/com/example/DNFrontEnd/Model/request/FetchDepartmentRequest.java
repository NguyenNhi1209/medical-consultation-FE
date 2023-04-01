package com.example.DNFrontEnd.Model.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class FetchDepartmentRequest {
    private String symptom;
    private String scheduleDate;
    public static String convertToString(FetchDepartmentRequest fetchDepartmentRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(fetchDepartmentRequest);
        return result;
    }

}
