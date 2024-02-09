package com.example.DNFrontEnd.Model.response;

import com.example.DNFrontEnd.Model.DTO.MedicineDTO;
import com.example.DNFrontEnd.Model.entity.PatientDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailPatientProfileResponse {
    private Long patientId;
    private Long patientProfileId;
    private String createdAt;
    private String symptom;
    private String anamnesis;
    private String advice;
    private String diagnostic;
    private List<MedicineDTO> medicines;
    private PatientDetail patientDetail;
}
