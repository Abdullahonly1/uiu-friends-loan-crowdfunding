package com.uiu.friendsloan.dto;

import lombok.Data;

@Data
public class LoanPostRequest {
    private double amount;
    private String purpose;
    private int repaymentMonths;
}