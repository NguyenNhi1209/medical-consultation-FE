package com.example.DNFrontEnd.Model.request;

import lombok.Data;

@Data
public class CreatePatientProfileRequest {
    public String scheduleDate;
    public String symptom;
}