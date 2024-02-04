package com.example.DNFrontEnd.Model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParentDetail {
    private Long id;
    private String phoneNumber;
    private String fullName;
    private String address;
}
