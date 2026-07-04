package com.uiu.friendsloan.dto;

import lombok.Data;

@Data
public class CrowdfundingPostRequest {
    private String title;
    private String description;
    private double amountNeeded;
    private boolean isUrgent;
}