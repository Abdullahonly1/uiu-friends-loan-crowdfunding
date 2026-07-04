package com.uiu.friendsloan.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DonationResponse {
    private Long id;
    private Long donorId;
    private String donorName;
    private double amount;
    private String message;
    private LocalDateTime donatedAt;
}