package com.example.DNFrontEnd.Model.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class ActivateUserRequest {
    private Long userId;
    private Boolean isActive = false;
    public static String convertToString(ActivateUserRequest activateUserRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(activateUserRequest);
        return result;
    }
}
