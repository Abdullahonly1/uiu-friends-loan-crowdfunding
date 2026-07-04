package com.uiu.friendsloan.dto;

import com.uiu.friendsloan.entity.PostStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CrowdfundingPostResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String title;
    private String description;
    private double amountNeeded;
    private double currentAmount;
    private boolean isUrgent;
    private PostStatus status;
    private double progressPercentage;   // (currentAmount / amountNeeded) * 100
    private LocalDateTime createdAt;
}