package com.uiu.friendsloan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_ratings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rated_user_id", nullable = false)
    private User ratedUser;      // যাকে rating দেওয়া হচ্ছে

    @ManyToOne
    @JoinColumn(name = "rater_user_id", nullable = false)
    private User raterUser;      // যে rating দিচ্ছে

    @Column(nullable = false)
    private int rating;          // 1 to 5

    private String comment;

    private LocalDateTime createdAt = LocalDateTime.now();
}