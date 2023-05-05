package com.example.DNFrontEnd.Model.response;

import com.example.DNFrontEnd.Model.DTO.AcademicRankDTO;
import com.example.DNFrontEnd.Model.DTO.DepartmentDTO;
import lombok.Data;

import java.util.List;

@Data
public class DoctorProfileResponse {
    private Long doctorId;
    private String fullName;
    private String sex;
    private String identityNumber;
    private String phoneNumber;
    private Long userId;
    private DepartmentDTO department;
    private List<AcademicRankDTO> academicRanks;
}
