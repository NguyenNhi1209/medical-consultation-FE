package com.example.DNFrontEnd.Model.DTO;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String type;
    private Boolean isActive = false;
}
