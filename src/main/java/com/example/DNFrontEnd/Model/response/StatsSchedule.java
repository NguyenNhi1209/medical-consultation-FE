package com.example.DNFrontEnd.Model.response;

import com.example.DNFrontEnd.Model.DTO.DepartmentDTO;
import lombok.Data;

@Data
public class StatsSchedule {
    private DepartmentDTO department;
    private ScheduleRevenue scheduleRevenue;

    @Data
    public static class ScheduleRevenue {
        private Long totalSchedule;
        private Long totalDone;
        private Long totalNotDone;
        private Long totalPay;
        private Long totalNotPay;
    }
}
