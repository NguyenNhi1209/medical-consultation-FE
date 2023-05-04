package com.example.DNFrontEnd.Model.response;

import lombok.Data;

@Data
public class DetailScheduleResponse {
    private Long scheduleId;
    private Long departmentId;
    private String departmentName;
    private Long doctorId;
    private String doctorName;
    private Long patientProfileId;
    private String symptom;
    private String medicalDate;
    private String hours;
    private Double price = 0.0;
    private Boolean isDone;
    private Boolean isPay;
}
