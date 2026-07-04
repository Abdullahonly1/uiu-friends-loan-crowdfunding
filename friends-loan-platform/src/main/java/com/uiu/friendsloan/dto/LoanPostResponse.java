package com.uiu.friendsloan.dto;

import com.uiu.friendsloan.entity.PostStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoanPostResponse {
    private Long id;
    private Long borrowerId;
    private String borrowerName;
    private double amount;
    private String purpose;
    private int repaymentMonths;
    private PostStatus status;
    private double trustScore;
    private LocalDateTime createdAt;
}