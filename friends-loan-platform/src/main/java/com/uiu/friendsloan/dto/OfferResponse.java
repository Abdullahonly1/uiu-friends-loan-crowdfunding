package com.uiu.friendsloan.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OfferResponse {
    private Long id;
    private Long lenderId;
    private String lenderName;
    private double interestRate;
    private int repaymentMonths;
    private String message;
    private LocalDateTime offeredAt;
}