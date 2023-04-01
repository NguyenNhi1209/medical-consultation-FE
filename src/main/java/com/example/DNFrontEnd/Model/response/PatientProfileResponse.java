package com.example.DNFrontEnd.Model.response;

import lombok.Data;

@Data
public class PatientProfileResponse {
    private Long id;
    private Long patientId;
    private String symptom;
    private String diagnostic;
    private Boolean isComplete;
}
