package com.example.DNFrontEnd.Model.DTO;

import com.example.DNFrontEnd.Model.entity.ParentDetail;
import com.example.DNFrontEnd.Model.entity.PatientDetail;
import lombok.Data;

@Data
public class PatientInfoUI {
    private ParentDetail parentDetail;
    private PatientDetail patientDetail;
}
