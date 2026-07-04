package com.uiu.friendsloan.service;

import com.uiu.friendsloan.dto.LoanPostRequest;
import com.uiu.friendsloan.entity.LoanPost;
import com.uiu.friendsloan.entity.PostStatus;
import com.uiu.friendsloan.entity.User;
import com.uiu.friendsloan.repository.LoanPostRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService {

    private final LoanPostRepository loanPostRepository;
    private final UserService userService;

    public LoanService(LoanPostRepository loanPostRepository, UserService userService) {
        this.loanPostRepository = loanPostRepository;
        this.userService = userService;
    }


   /* @PostConstruct
    public void autoResetOnStartup() {
        resetAllLoanPostsToPending();
    }*/
    @Transactional
    public LoanPost createLoanPost(LoanPostRequest request) {
        User currentUser = userService.getCurrentUser();

        LoanPost loanPost = LoanPost.builder()
                .borrower(currentUser)
                .amount(request.getAmount())
                .purpose(request.getPurpose())
                .repaymentMonths(request.getRepaymentMonths())
                .status(PostStatus.PENDING)   // খুব গুরুত্বপূর্ণ
                .createdAt(LocalDateTime.now())
                .build();

        LoanPost saved = loanPostRepository.save(loanPost);

        System.out.println("✅ New Loan Post Created - ID: " + saved.getId()
                + " | Status: PENDING | By: " + currentUser.getEmail());

        return saved;
    }

    public LoanPost getById(Long id) {
        return loanPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan Post not found"));
    }

    public List<LoanPost> getAllLoans() {
        return loanPostRepository.findAll();
    }

    public List<LoanPost> getMyLoanPosts() {
        User currentUser = userService.getCurrentUser();

        System.out.println("🔍 Getting my loans for user: " + currentUser.getEmail() + " (ID: " + currentUser.getId() + ")");

        // সঠিক ফিল্টার — শুধুমাত্র এই ইউজারের পোস্ট
        List<LoanPost> myLoans = loanPostRepository.findByBorrower(currentUser);

        System.out.println("✅ Found " + myLoans.size() + " loans for user: " + currentUser.getEmail());

        return myLoans;
    }


    // সাধারণ User এর জন্য — শুধু PENDING (Accept না করা) post
    public List<LoanPost> getAvailableLoanPosts() {
        return loanPostRepository.findByStatus(PostStatus.PENDING);
    }

    // Admin এর জন্য
    public List<LoanPost> getPendingLoanPosts() {
        return loanPostRepository.findByStatus(PostStatus.PENDING);
    }

    public List<LoanPost> getApprovedLoanPosts() {
        return loanPostRepository.findByStatus(PostStatus.APPROVED);
    }



    @Transactional
    public void approveLoanPost(Long postId) {
        LoanPost post = loanPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Loan Post not found"));
        post.setStatus(PostStatus.APPROVED);
        post.setApprovedAt(LocalDateTime.now());
        loanPostRepository.save(post);
    }

    @Transactional
    public void rejectLoanPost(Long postId) {
        LoanPost post = loanPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Loan Post not found"));
        post.setStatus(PostStatus.REJECTED);
        loanPostRepository.save(post);
    }


    @Transactional
    public LoanPost save(LoanPost loanPost) {
        return loanPostRepository.save(loanPost);
    }


    public List<LoanPost> getActiveLoanPosts() {
        User currentUser = userService.getCurrentUser();
        System.out.println("🔍 Getting ACTIVE loans for user: " + currentUser.getEmail());

        // শুধুমাত্র PENDING status এর post
        List<LoanPost> activeLoans = loanPostRepository.findByBorrowerAndStatus(currentUser, PostStatus.PENDING);

        System.out.println("✅ Found " + activeLoans.size() + " ACTIVE loans");
        return activeLoans;
    }


    public List<LoanPost> getAllActiveLoans() {
        System.out.println("🔍 Getting only PENDING loan posts for dashboard");

        List<LoanPost> allLoans = loanPostRepository.findAll();

        List<LoanPost> visibleLoans = allLoans.stream()
                .filter(post -> post.getStatus() == PostStatus.PENDING)
                .toList();

        System.out.println("✅ Found " + visibleLoans.size() + " PENDING loan posts for dashboard");

        visibleLoans.forEach(post -> {
            System.out.println("   → ID: " + post.getId()
                    + " | Purpose: " + post.getPurpose()
                    + " | Status: " + post.getStatus());
        });

        return visibleLoans;
    }


    // একবার কল করলে সব post আবার PENDING হয়ে যাবে
    // একবার চালালে সব post আবার PENDING হয়ে যাবে (একবারই চালাবে)
   /* @Transactional
    public void resetAllLoanPostsToPending() {
        List<LoanPost> allLoans = loanPostRepository.findAll();

        allLoans.forEach(post -> {
            post.setStatus(PostStatus.PENDING);
            post.setApprovedAt(null);
            loanPostRepository.save(post);
        });

        System.out.println("✅ RESET SUCCESS - All " + allLoans.size() + " loan posts set back to PENDING");
    }*/


    // Notification / Details View এর জন্য — সব স্ট্যাটাসের পোস্ট লোড করবে
    // Notification / Details View এর জন্য — সব স্ট্যাটাসের পোস্ট লোড করবে
    public LoanPost getByIdForView(Long id) {
        System.out.println("🔍 getByIdForView called for ID: " + id);

        LoanPost post = loanPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan Post not found with ID: " + id));

        System.out.println("✅ Post Found - ID: " + post.getId()
                + " | Status: " + post.getStatus());

        return post;
    }


    /**
     * কোনো ইউজারের সব Loan Posts দেখানো
     */
    // অন্য User এর সব Loan Posts দেখানো
    public List<LoanPost> getLoansByUserId(Long userId) {
        return loanPostRepository.findByBorrowerId(userId);
    }
}