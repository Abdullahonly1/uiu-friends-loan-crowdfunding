package com.uiu.friendsloan.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "offers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_post_id", nullable = false)
    private LoanPost loanPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lender_id", nullable = false)
    private User lender;

    @Column(name = "interest_rate", nullable = false)
    private double interestRate;   // যেমন: 8.5 (%)

    @Column(name = "repayment_months", nullable = false)
    private int repaymentMonths;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    private OfferStatus status = OfferStatus.PENDING;

    @Column(name = "offered_at")
    private LocalDateTime offeredAt = LocalDateTime.now();
}