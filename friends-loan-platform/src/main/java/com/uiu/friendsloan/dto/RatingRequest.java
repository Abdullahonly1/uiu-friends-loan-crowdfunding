package com.uiu.friendsloan.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingRequest {
    private Long ratedUserId;
    private int rating;        // 1 থেকে 5
    private String comment;
}