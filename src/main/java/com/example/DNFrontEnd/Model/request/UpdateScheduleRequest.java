package com.example.DNFrontEnd.Model.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class UpdateScheduleRequest {
    private Long scheduleId;
    private String diagnostic;
    private boolean isPay = false;
    public static String convertToString(UpdateScheduleRequest updateScheduleRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(updateScheduleRequest);
        return result;
    }
}
