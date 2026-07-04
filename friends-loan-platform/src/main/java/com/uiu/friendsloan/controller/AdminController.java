package com.uiu.friendsloan.controller;

import com.uiu.friendsloan.entity.CrowdfundingPost;
import com.uiu.friendsloan.entity.LoanPost;
import com.uiu.friendsloan.entity.User;
import com.uiu.friendsloan.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "http://localhost:8080")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ==================== USER MANAGEMENT ====================

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/users/{id}/ban")
    public ResponseEntity<String> banUser(@PathVariable Long id) {
        adminService.banUser(id);
        return ResponseEntity.ok("User has been banned successfully!");
    }

    @PutMapping("/users/{id}/unban")
    public ResponseEntity<String> unbanUser(@PathVariable Long id) {
        adminService.unbanUser(id);
        return ResponseEntity.ok("User has been unbanned successfully!");
    }

    // ==================== LOAN MANAGEMENT ====================

    @GetMapping("/loans/pending")
    public ResponseEntity<List<LoanPost>> getPendingLoans() {
        return ResponseEntity.ok(adminService.getPendingLoanPosts());
    }

    @PutMapping("/loans/{id}/approve")
    public ResponseEntity<String> approveLoan(@PathVariable Long id) {
        adminService.approveLoanPost(id);
        return ResponseEntity.ok("Loan post approved successfully!");
    }

    @PutMapping("/loans/{id}/reject")
    public ResponseEntity<String> rejectLoan(@PathVariable Long id) {
        adminService.rejectLoanPost(id);
        return ResponseEntity.ok("Loan post rejected!");
    }

    // ==================== CROWDFUNDING MANAGEMENT ====================

    @GetMapping("/crowdfunding/pending")
    public ResponseEntity<List<CrowdfundingPost>> getPendingCrowdfunding() {
        return ResponseEntity.ok(adminService.getPendingCrowdfundingPosts());
    }

    @PutMapping("/crowdfunding/{id}/approve")
    public ResponseEntity<String> approveCrowdfunding(@PathVariable Long id) {
        adminService.approveCrowdfundingPost(id);
        return ResponseEntity.ok("Crowdfunding campaign approved successfully!");
    }

    @PutMapping("/crowdfunding/{id}/reject")
    public ResponseEntity<String> rejectCrowdfunding(@PathVariable Long id) {
        adminService.rejectCrowdfundingPost(id);
        return ResponseEntity.ok("Crowdfunding campaign rejected!");
    }
}