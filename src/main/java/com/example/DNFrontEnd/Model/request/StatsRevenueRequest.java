package com.example.DNFrontEnd.Model.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class StatsRevenueRequest {
    private Integer year;
    public static String convertToString(StatsRevenueRequest statsRevenueRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(statsRevenueRequest);
        return result;
    }
}
