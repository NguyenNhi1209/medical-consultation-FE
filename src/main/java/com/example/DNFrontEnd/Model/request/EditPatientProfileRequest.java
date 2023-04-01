package com.example.DNFrontEnd.Model.request;

import lombok.Data;

@Data
public class EditPatientProfileRequest {
    public Long id;
    public String fullName;
    public String birthday;
    public String phoneNumber;
    public String sex;
    public String job;
    public String email;
    public String identityNumber;
    public String ethnic;
    public String address;
}