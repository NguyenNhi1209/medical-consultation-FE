package com.example.DNFrontEnd.Model.response;

import lombok.Data;

@Data
public class UserProfileResponse {
    private String name;
    private String email;
    private String phoneNumber;
    private String type;
}
