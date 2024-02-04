package com.example.DNFrontEnd.Model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDetail {
    private Long id;
    private String fullName;
    private String birthday;
    private String sex;
    private Long weight;
}
