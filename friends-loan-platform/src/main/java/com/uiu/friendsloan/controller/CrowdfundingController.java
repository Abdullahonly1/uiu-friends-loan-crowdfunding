package com.uiu.friendsloan.controller;

import com.uiu.friendsloan.dto.CrowdfundingPostRequest;
import com.uiu.friendsloan.entity.CrowdfundingPost;
import com.uiu.friendsloan.entity.Donation;
import com.uiu.friendsloan.service.CrowdfundingService;
import com.uiu.friendsloan.service.DonationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crowdfunding")
@CrossOrigin(origins = "http://localhost:8080")
public class CrowdfundingController {

    private final CrowdfundingService crowdfundingService;
    private final DonationService donationService;

    public CrowdfundingController(CrowdfundingService crowdfundingService, DonationService donationService) {
        this.crowdfundingService = crowdfundingService;
        this.donationService = donationService;
    }

    @PostMapping
    public ResponseEntity<CrowdfundingPost> create(@RequestBody CrowdfundingPostRequest request) {
        return ResponseEntity.ok(crowdfundingService.createPost(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CrowdfundingPost> getById(@PathVariable Long id) {
        return ResponseEntity.ok(crowdfundingService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CrowdfundingPost>> getAll() {
        return ResponseEntity.ok(crowdfundingService.getApprovedPosts());
    }

    @GetMapping("/my")
    public ResponseEntity<List<CrowdfundingPost>> getMyPosts() {
        return ResponseEntity.ok(crowdfundingService.getMyPosts(null));
    }

    // Donation Endpoints
    @PostMapping("/{postId}/donate")
    public ResponseEntity<Donation> donate(
            @PathVariable Long postId,
            @RequestParam Double amount,
            @RequestParam(required = false) String message) {

        Donation donation = donationService.makeDonation(postId, amount, message);
        return ResponseEntity.ok(donation);
    }

    @GetMapping("/{postId}/donations")
    public ResponseEntity<List<Donation>> getDonations(@PathVariable Long postId) {
        return ResponseEntity.ok(donationService.getDonationsByPost(postId));
    }
}