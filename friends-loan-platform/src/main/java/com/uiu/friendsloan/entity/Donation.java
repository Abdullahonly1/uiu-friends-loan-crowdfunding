package com.uiu.friendsloan.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "donations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crowdfunding_post_id", nullable = false)
    private CrowdfundingPost crowdfundingPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", nullable = false)
    private User donor;

    @Column(nullable = false)
    private double amount;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "donated_at")
    private LocalDateTime donatedAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        this.donatedAt = LocalDateTime.now();
    }
}