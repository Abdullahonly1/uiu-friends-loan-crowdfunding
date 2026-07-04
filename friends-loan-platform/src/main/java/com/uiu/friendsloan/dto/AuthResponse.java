package com.uiu.friendsloan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor   // ← এই লাইনটি খুব জরুরি
public class AuthResponse {

    private String token;
    private String name;
    private String role;
    private Long userId;
    private double trustScore;
    private String message;
}