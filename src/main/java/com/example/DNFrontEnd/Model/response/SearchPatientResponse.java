package com.example.DNFrontEnd.Model.response;

import com.example.DNFrontEnd.Model.entity.ParentDetail;
import com.example.DNFrontEnd.Model.entity.PatientDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchPatientResponse {
    private ParentDetail parentDetail;
    private List<PatientDetail> patientDetails;
}
