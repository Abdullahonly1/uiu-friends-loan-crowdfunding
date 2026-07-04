package com.uiu.friendsloan.service;

import com.uiu.friendsloan.entity.LoanPost;
import com.uiu.friendsloan.entity.Offer;
import com.uiu.friendsloan.entity.OfferStatus;
import com.uiu.friendsloan.entity.PostStatus;
import com.uiu.friendsloan.entity.User;
import com.uiu.friendsloan.repository.LoanPostRepository;
import com.uiu.friendsloan.repository.OfferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanOfferService {

    private final OfferRepository offerRepository;
    private final LoanService loanService;
    private final UserService userService;
    private final LoanPostRepository loanPostRepository;

    private final NotificationService notificationService;

    public LoanOfferService(OfferRepository offerRepository, LoanService loanService, UserService userService,LoanPostRepository loanPostRepository,NotificationService notificationService) {
        this.offerRepository = offerRepository;
        this.loanService = loanService;
        this.userService = userService;
        this.loanPostRepository = loanPostRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public Offer submitOffer(Long loanId, Double interestRate, Integer repaymentMonths, String message) {
        User lender = userService.getCurrentUser();
        LoanPost loanPost = loanService.getById(loanId);

        Offer offer = Offer.builder()
                .loanPost(loanPost)
                .lender(lender)
                .interestRate(interestRate)
                .repaymentMonths(repaymentMonths)   // ← এই লাইনটা যোগ করো
                .message(message)
                .status(OfferStatus.PENDING)
                .build();

        Offer savedOffer = offerRepository.save(offer);

        notificationService.sendNotification(
                loanPost.getBorrower(),
                "New Offer Received",
                lender.getName() + " offered " + interestRate + "% interest on your loan.",
                "NEW_OFFER",
                loanPost.getId(),
                "LOAN"
        );

        System.out.println("📬 New Offer Notification sent to " + loanPost.getBorrower().getName()
                + " for Loan Post ID: " + loanPost.getId());

        return savedOffer;
    }

    public List<Offer> getOffersByLoan(Long loanId) {
        LoanPost loanPost = loanService.getById(loanId);
        return offerRepository.findByLoanPost(loanPost);
    }

    @Transactional
    public Offer acceptOffer(Long offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found"));

        LoanPost post = offer.getLoanPost();

        // 1. Post কে COMPLETED করে দাও (Dashboard থেকে সরিয়ে দাও)
        post.setStatus(PostStatus.COMPLETED);
        post.setApprovedAt(LocalDateTime.now());
        loanPostRepository.save(post);

        // 2. এই Offer কে Accepted করো
        offer.setStatus(OfferStatus.ACCEPTED);
        Offer savedOffer = offerRepository.save(offer);

        // ==================== NOTIFICATION ADDED ====================
        // Lender কে জানানো যে তার Offer Accept হয়েছে
        notificationService.sendNotification(
                offer.getLender(),
                "Offer Accepted! 🎉",
                "Your offer on '" + post.getPurpose() + "' has been accepted by the borrower.",
                "OFFER_ACCEPTED",
                post.getId(),
                "LOAN"
        );
        // ============================================================

        // 3. একই Post এর বাকি সব Pending Offer গুলো Rejected করে দাও
        List<Offer> otherOffers = offerRepository.findByLoanPostAndStatus(post, OfferStatus.PENDING);
        otherOffers.forEach(otherOffer -> {
            if (!otherOffer.getId().equals(offerId)) {
                otherOffer.setStatus(OfferStatus.REJECTED);
                offerRepository.save(otherOffer);
            }
        });

        System.out.println("✅ Offer Accepted! Post ID " + post.getId()
                + " is now COMPLETED. " + otherOffers.size()
                + " other offers rejected.");

        return savedOffer;
    }
}