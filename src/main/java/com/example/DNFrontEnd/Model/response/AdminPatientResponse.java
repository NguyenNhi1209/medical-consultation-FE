package com.example.DNFrontEnd.Model.response;

import com.example.DNFrontEnd.Model.DTO.PatientDTO;
import com.example.DNFrontEnd.Model.DTO.PatientProfileDTO;
import com.example.DNFrontEnd.Model.DTO.UserDTO;
import lombok.Data;

import java.util.List;

@Data
public class AdminPatientResponse {
    private PatientDTO patient;
    private UserDTO user;
    private List<PatientProfileDTO> patientProfiles;
}
