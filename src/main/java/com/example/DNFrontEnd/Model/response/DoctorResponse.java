package com.example.DNFrontEnd.Model.response;

import com.example.DNFrontEnd.Model.DTO.DoctorDTO;
import com.example.DNFrontEnd.Model.DTO.UserDTO;
import lombok.Data;

@Data
public class DoctorResponse {
    private DoctorDTO doctor;
    private UserDTO user;
}
