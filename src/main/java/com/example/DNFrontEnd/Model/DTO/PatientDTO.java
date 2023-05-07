package com.example.DNFrontEnd.Model.DTO;

import lombok.Data;

@Data
public class PatientDTO {
    private Long id;
    private String fullName;
    private String birthday;
    private String phoneNumber;
    private String sex;
    private String address;
    private String job;
    private String identityNumber;
}
