package com.example.DNFrontEnd.Model.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class StatsScheduleRequest {
    private Long departmentId;
    private String condition;
    public static String convertToString(StatsScheduleRequest statsScheduleRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(statsScheduleRequest);
        return result;
    }
}
