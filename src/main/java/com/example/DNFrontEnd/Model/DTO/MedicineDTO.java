package com.example.DNFrontEnd.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineDTO {
    private Long id;
    private String name;
    private Long quantity;
    private String unit;
    private String instruction;
}
