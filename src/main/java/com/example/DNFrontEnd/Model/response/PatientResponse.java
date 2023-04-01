package com.example.DNFrontEnd.Model.response;

import lombok.Data;

@Data
public class PatientResponse {
    private Long id;
    private Long userId;
    private String fullName;
    private String birthday;
    private String phoneNumber;
    private String sex;
    private String address;
    private String job;
    private String identityNumber;
}
