package com.example.DNFrontEnd.Model.response;

import com.example.DNFrontEnd.Model.DTO.DoctorDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DepartmentResponse {
    private Long id;
    private String name;
    private String symbol;
    private Double price = 0.0;
    private List<DoctorDTO> doctors = new ArrayList<>();
    private List<String> symptoms = new ArrayList<>();
}
