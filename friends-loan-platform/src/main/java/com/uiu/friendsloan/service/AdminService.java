package com.uiu.friendsloan.service;

import com.uiu.friendsloan.entity.CrowdfundingPost;
import com.uiu.friendsloan.entity.LoanPost;
import com.uiu.friendsloan.entity.PostStatus;
import com.uiu.friendsloan.entity.User;
import com.uiu.friendsloan.repository.CrowdfundingPostRepository;
import com.uiu.friendsloan.repository.LoanPostRepository;
import com.uiu.friendsloan.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final LoanPostRepository loanPostRepository;
    private final CrowdfundingPostRepository crowdfundingPostRepository;

    public AdminService(UserRepository userRepository,
                        LoanPostRepository loanPostRepository,
                        CrowdfundingPostRepository crowdfundingPostRepository) {
        this.userRepository = userRepository;
        this.loanPostRepository = loanPostRepository;
        this.crowdfundingPostRepository = crowdfundingPostRepository;
    }

    // ==================== USER MANAGEMENT ====================

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void banUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setActive(false);
        userRepository.save(user);
    }

    @Transactional
    public void unbanUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setActive(true);
        userRepository.save(user);
    }

    // ==================== LOAN MANAGEMENT ====================

    public List<LoanPost> getPendingLoanPosts() {
        return loanPostRepository.findByStatus(PostStatus.PENDING);   // Enum ব্যবহার
    }

    @Transactional
    public void approveLoanPost(Long loanId) {
        LoanPost loan = loanPostRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan post not found with id: " + loanId));
        loan.setStatus(PostStatus.APPROVED);   // Enum ব্যবহার
        loanPostRepository.save(loan);
    }

    @Transactional
    public void rejectLoanPost(Long loanId) {
        LoanPost loan = loanPostRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan post not found with id: " + loanId));
        loan.setStatus(PostStatus.REJECTED);   // Enum ব্যবহার
        loanPostRepository.save(loan);
    }

    // ==================== CROWDFUNDING MANAGEMENT ====================

    public List<CrowdfundingPost> getPendingCrowdfundingPosts() {
        return crowdfundingPostRepository.findByStatus(PostStatus.PENDING);   // Enum ব্যবহার
    }

    @Transactional
    public void approveCrowdfundingPost(Long postId) {
        CrowdfundingPost post = crowdfundingPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Crowdfunding post not found with id: " + postId));
        post.setStatus(PostStatus.APPROVED);   // Enum ব্যবহার
        crowdfundingPostRepository.save(post);
    }

    @Transactional
    public void rejectCrowdfundingPost(Long postId) {
        CrowdfundingPost post = crowdfundingPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Crowdfunding post not found with id: " + postId));
        post.setStatus(PostStatus.REJECTED);   // Enum ব্যবহার
        crowdfundingPostRepository.save(post);
    }
}