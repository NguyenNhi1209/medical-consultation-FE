package com.example.DNFrontEnd.Model.request;

import com.example.DNFrontEnd.Model.DTO.MedicineDTO;
import com.example.DNFrontEnd.Model.DTO.PatientInfoUI;
import com.example.DNFrontEnd.Model.entity.ParentDetail;
import com.example.DNFrontEnd.Model.entity.PatientDetail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
    private String resMedicines;
    private List<MedicineDTO> medicines = new ArrayList<>();
    private PatientInfoUI patientInfo;

    public static String convertToString(MedicalPatientRequest medicalPatientRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<MedicineDTO> medicineDTOS = objectMapper.readValue(medicalPatientRequest.getResMedicines(), new TypeReference<List<MedicineDTO>>() {});
        medicalPatientRequest.setMedicines(medicineDTOS);
        String result = objectMapper.writeValueAsString(medicalPatientRequest);
        return result;
    }
}
