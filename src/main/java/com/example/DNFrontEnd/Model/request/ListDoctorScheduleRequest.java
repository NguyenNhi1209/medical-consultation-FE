package com.example.DNFrontEnd.Model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class ListDoctorScheduleRequest{
    private Long doctorId;
    private String medicalDate;
    private Boolean isDone;
    private Boolean isPay;
    public static String convertToString(ListDoctorScheduleRequest listDoctorScheduleRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(listDoctorScheduleRequest);
        return result;
    }
}
