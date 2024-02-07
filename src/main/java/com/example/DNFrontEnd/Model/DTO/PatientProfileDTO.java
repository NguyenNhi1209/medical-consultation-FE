package com.example.DNFrontEnd.Model.DTO;

import lombok.Data;

@Data
public class PatientProfileDTO {
    private Long patientId;
    private Long patientProfileId;
    private String name;
    private String birthday;
    private String sex;
    private String phoneNumber;
    private String symptom;
    private String diagnostic;
    private Boolean isComplete;
    private String createdAt;
    private String anamnesis;
    private String advice;
}
