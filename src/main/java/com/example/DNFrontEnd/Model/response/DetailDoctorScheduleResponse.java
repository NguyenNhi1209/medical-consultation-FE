package com.example.DNFrontEnd.Model.response;

import com.example.DNFrontEnd.Model.DTO.PatientProfileDTO;
import lombok.Data;

@Data
public class DetailDoctorScheduleResponse {
    private Long scheduleId;
    private Long doctorId;
    private String medicalDate;
    private String hours;
    private Double price = 0.0;
    private Boolean isDone;
    private Boolean isPay;
    private PatientProfileDTO patientProfile;
}
