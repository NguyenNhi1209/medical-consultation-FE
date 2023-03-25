package com.example.DNFrontEnd.Model;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String expiredAt;
}
