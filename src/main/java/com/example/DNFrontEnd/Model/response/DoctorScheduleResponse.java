package com.example.DNFrontEnd.Model.response;

import lombok.Data;

@Data
public class DoctorScheduleResponse {
    private Long scheduleId;
    private Long doctorId;
    private Long patientProfileId;
    private String medicalDate;
    private String hours;
    private Boolean isDone;
    private Boolean isPay;
}
