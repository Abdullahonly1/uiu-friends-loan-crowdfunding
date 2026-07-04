package com.uiu.friendsloan.dto;

import lombok.Data;

@Data
public class DonationRequest {
    private Long crowdfundingPostId;
    private double amount;
    private String message;   // optional message
}