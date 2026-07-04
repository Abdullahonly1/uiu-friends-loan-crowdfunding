package com.uiu.friendsloan.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "sent_at")
    private LocalDateTime sentAt = LocalDateTime.now();

    // Optional: কোন post এর সাথে সম্পর্কিত
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_post_id")
    private LoanPost loanPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crowdfunding_post_id")
    private CrowdfundingPost crowdfundingPost;
}