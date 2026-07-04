package com.uiu.friendsloan.dto;

import lombok.Data;

@Data
public class OfferRequest {
    private Long loanPostId;
    private double interestRate;      // শতাংশে (যেমন: 8.5)
    private int repaymentMonths;
    private String message;           // lender এর মেসেজ
}