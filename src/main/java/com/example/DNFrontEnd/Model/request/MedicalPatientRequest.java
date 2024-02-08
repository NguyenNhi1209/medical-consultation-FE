package com.example.DNFrontEnd.Model.request;

import com.example.DNFrontEnd.Model.DTO.MedicineDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MedicalPatientRequest {
    private Long patientId;
    private String anamnesis;
    private String symptom;
    private String diagnostic;
    private String advice;
    private List<MedicineDTO> medicines = new ArrayList<>();

    public static String convertToString(MedicalPatientRequest medicalPatientRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(medicalPatientRequest);
        return result;
    }
}
