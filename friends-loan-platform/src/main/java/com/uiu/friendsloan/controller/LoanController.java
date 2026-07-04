package com.uiu.friendsloan.controller;

import com.uiu.friendsloan.dto.LoanPostRequest;
import com.uiu.friendsloan.entity.LoanPost;
import com.uiu.friendsloan.entity.Offer;
import com.uiu.friendsloan.service.LoanOfferService;
import com.uiu.friendsloan.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin(origins = "http://localhost:8080")
public class LoanController {

    private final LoanService loanService;
    private final LoanOfferService loanOfferService;

    public LoanController(LoanService loanService, LoanOfferService loanOfferService) {
        this.loanService = loanService;
        this.loanOfferService = loanOfferService;
    }

    // Create Loan Post
    @PostMapping
    public ResponseEntity<LoanPost> createLoanPost(@RequestBody LoanPostRequest request) {
        return ResponseEntity.ok(loanService.createLoanPost(request));
    }

    // Get Single Loan by ID
    /*@GetMapping("/{id}")
    public ResponseEntity<LoanPost> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getById(id));
    }*/


    @GetMapping("/{id}")
    public ResponseEntity<LoanPost> getLoanById(@PathVariable Long id) {
        // সব স্ট্যাটাসের পোস্ট লোড করবে (COMPLETED সহ)
        LoanPost post = loanService.getByIdForView(id);
        return ResponseEntity.ok(post);
    }

    // ✅ NORMAL USER DASHBOARD - শুধু Admin Approved Post দেখাবে
    // ✅ NORMAL USER DASHBOARD — শুধু Admin Approved এবং এখনো Accept না করা post দেখাবে
    @GetMapping
    public ResponseEntity<List<LoanPost>> getAllLoans() {
        return ResponseEntity.ok(loanService.getApprovedLoanPosts());
    }
    // Admin এর জন্য Pending posts
    @GetMapping("/pending")
    public ResponseEntity<List<LoanPost>> getPendingLoans() {
        return ResponseEntity.ok(loanService.getPendingLoanPosts());
    }

    // Admin এর জন্য Approved posts
    @GetMapping("/approved")
    public ResponseEntity<List<LoanPost>> getApprovedLoans() {
        return ResponseEntity.ok(loanService.getApprovedLoanPosts());
    }

    // Get My Loans
    @GetMapping("/my")
    public ResponseEntity<List<LoanPost>> getMyLoans() {
        return ResponseEntity.ok(loanService.getMyLoanPosts());
    }

    // ==================== OFFER ENDPOINTS ====================
    @PostMapping("/{loanId}/offer")
    public ResponseEntity<Offer> submitOffer(
            @PathVariable Long loanId,
            @RequestParam Double interestRate,
            @RequestParam Integer repaymentMonths,   // ← এই লাইনটা যোগ করো
            @RequestParam(required = false) String message) {

        Offer offer = loanOfferService.submitOffer(loanId, interestRate, repaymentMonths, message);
        return ResponseEntity.ok(offer);
    }

    @GetMapping("/{loanId}/offers")
    public ResponseEntity<List<Offer>> getOffers(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanOfferService.getOffersByLoan(loanId));
    }

    @PostMapping("/offers/{offerId}/accept")
    public ResponseEntity<Offer> acceptOffer(@PathVariable Long offerId) {
        Offer acceptedOffer = loanOfferService.acceptOffer(offerId);
        return ResponseEntity.ok(acceptedOffer);
    }

    @GetMapping("/user/{userId}")
    public List<LoanPost> getLoansByUser(@PathVariable Long userId) {
        return loanService.getLoansByUserId(userId);   // loanService হলো তোমার সার্ভিসের নাম
    }
}