package com.uiu.friendsloan.repository;

import com.uiu.friendsloan.entity.LoanPost;
import com.uiu.friendsloan.entity.PostStatus;
import com.uiu.friendsloan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanPostRepository extends JpaRepository<LoanPost, Long> {

    // Admin এর জন্য Pending posts
    List<LoanPost> findByStatus(PostStatus status);

    // User এর নিজের posts দেখার জন্য
    List<LoanPost> findByBorrower(User borrower);

    // Optional: সবচেয়ে নতুন Pending posts প্রথমে দেখানোর জন্য
    List<LoanPost> findByStatusOrderByCreatedAtDesc(PostStatus status);

    // Optional: Specific borrower এর pending posts
    List<LoanPost> findByBorrowerAndStatus(User borrower, PostStatus status);

    List<LoanPost> findByBorrowerId(Long borrowerId);
}