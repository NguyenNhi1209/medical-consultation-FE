package com.example.DNFrontEnd.Model.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class AddUserRequest {
    private String name;
    private String email;
    private String sex;
    private String identityNumber;
    private String phoneNumber;
    private int type;
    private Long departmentId;

    public static String convertToString(AddUserRequest addUserRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(addUserRequest);
        return result;
    }
}
