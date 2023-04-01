package com.example.DNFrontEnd.Model.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class SaveScheduleRequest {
    private Long departmentId;
    private Long doctorId;
    private String symptom;
    private String medicalDate;
    private String hours;

    public static String convertToString(SaveScheduleRequest saveScheduleRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(saveScheduleRequest);
        return result;
    }
}
