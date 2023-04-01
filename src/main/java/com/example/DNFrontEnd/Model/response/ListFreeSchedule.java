package com.example.DNFrontEnd.Model.response;

import lombok.Data;

import java.util.List;

@Data
public class ListFreeSchedule {
    private Long departmentId;
    private String departmentName;
    private Long patientProfileId;
    private String medicalDate;
    private List<DetailSchedule> detailSchedules;

    @Data
    public static class DetailSchedule {
        private Long doctorId;
        private String scheduleTime;
        private Double price = 0.0;
    }

}
