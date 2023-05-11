package com.example.DNFrontEnd.Model.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StatsRevenueResponse {
    private int year;
    private List<StatsRevenue> statsRevenues = new ArrayList<>();
    @Data
    public static class StatsRevenue {
        private int month;
        private Double revenues = 0.0;
    }
}
