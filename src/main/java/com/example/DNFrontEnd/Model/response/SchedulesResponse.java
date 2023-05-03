package com.example.DNFrontEnd.Model.response;

import com.example.DNFrontEnd.Model.DTO.PatientProfileDTO;
import lombok.Data;

@Data
public class SchedulesResponse {
    private Long scheduleId;
    private Long doctorId;
    private Long patientProfileId;
    private String medicalDate;
    private String hours;
    private Boolean isDone;
    private Boolean isPay;
    private PatientProfileDTO patientProfileDTO;

}
