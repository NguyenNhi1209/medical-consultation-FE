package com.example.DNFrontEnd.Model.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StatsScheduleResponse {
    private String condition;
    private List<StatsSchedule> statsSchedules = new ArrayList<>();
}
