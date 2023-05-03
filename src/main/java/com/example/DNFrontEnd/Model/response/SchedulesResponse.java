package com.example.DNFrontEnd.Model.response;

import com.example.DNFrontEnd.Model.DTO.DoctorDTO;
import com.example.DNFrontEnd.Model.DTO.PatientProfileDTO;
import lombok.Data;

@Data
public class SchedulesResponse {
    private Long scheduleId;
    private String medicalDate;
    private String hours;
    private Double price = 0.0;
    private Boolean isDone;
    private Boolean isPay;
    private Long doctorId;
    private DoctorDTO doctor;
    private Long patientProfileId;
    private PatientProfileDTO patientProfile;
}
