package com.example.DNFrontEnd.Model.DTO;

import lombok.Data;

@Data
public class DoctorDTO {
    private Long id;
    private String fullName;
    private String sex;
    private DepartmentDTO department;
}
