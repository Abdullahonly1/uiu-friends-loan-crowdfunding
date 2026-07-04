package com.uiu.friendsloan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;                    // যার কাছে নোটিফিকেশন যাবে

    private String title;
    private String message;

    private String type;                  // NEW_LOAN_POST, NEW_OFFER, OFFER_ACCEPTED, etc.

    private Long relatedId;               // postId বা offerId
    private String relatedType;           // "LOAN" or "CROWDFUNDING"

    private boolean isRead = false;

    private LocalDateTime createdAt = LocalDateTime.now();
}