package com.uiu.friendsloan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "loan_offers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loan_post_id", nullable = false)
    private LoanPost loanPost;

    @ManyToOne
    @JoinColumn(name = "lender_id", nullable = false)
    private User lender;

    @Column(nullable = false)
    private Double interestRate;

    @Column(length = 500)
    private String message;

    @Column(nullable = false)
    private LocalDateTime offeredAt;

    @Enumerated(EnumType.STRING)
    private OfferStatus status = OfferStatus.PENDING;

    @PrePersist
    protected void onCreate() {
        this.offeredAt = LocalDateTime.now();
    }
}