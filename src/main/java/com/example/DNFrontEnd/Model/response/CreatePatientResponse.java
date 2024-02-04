package com.example.DNFrontEnd.Model.response;

import com.example.DNFrontEnd.Model.entity.ParentDetail;
import com.example.DNFrontEnd.Model.entity.PatientDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePatientResponse {
    private ParentDetail parentDetail;
    private PatientDetail patientDetail;

}
