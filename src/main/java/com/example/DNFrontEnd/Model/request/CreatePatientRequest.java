package com.example.DNFrontEnd.Model.request;

import com.example.DNFrontEnd.Model.entity.PatientDetail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePatientRequest {
    private String phoneNumber;
    private String fullName;
    private String address;
    private PatientDetail patientDetail;
    public static String convertToString(CreatePatientRequest createPatientRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(createPatientRequest);
        return result;
    }
}
