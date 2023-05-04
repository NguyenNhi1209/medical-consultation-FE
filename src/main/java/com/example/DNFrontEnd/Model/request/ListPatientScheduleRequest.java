package com.example.DNFrontEnd.Model.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class ListPatientScheduleRequest {
    private Long patientId;
    private Boolean isDone;
    private Boolean isPay;
    private String medicalDate;
    public static String convertToString(ListPatientScheduleRequest listPatientScheduleRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(listPatientScheduleRequest);
        return result;
    }
}
