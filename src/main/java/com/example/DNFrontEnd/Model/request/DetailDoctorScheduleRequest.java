package com.example.DNFrontEnd.Model.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class DetailDoctorScheduleRequest {
    private Long scheduleId;
    public static String convertToString(DetailDoctorScheduleRequest detailDoctorScheduleRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(detailDoctorScheduleRequest);
        return result;
    }
}
