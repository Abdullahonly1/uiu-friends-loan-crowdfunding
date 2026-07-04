package com.uiu.friendsloan.service;

import com.uiu.friendsloan.entity.CrowdfundingPost;
import com.uiu.friendsloan.entity.Donation;
import com.uiu.friendsloan.entity.User;
import com.uiu.friendsloan.repository.DonationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DonationService {

    private final DonationRepository donationRepository;
    private final CrowdfundingService crowdfundingService;
    private final UserService userService;

    public DonationService(DonationRepository donationRepository,
                           CrowdfundingService crowdfundingService,
                           UserService userService) {
        this.donationRepository = donationRepository;
        this.crowdfundingService = crowdfundingService;
        this.userService = userService;
    }

    @Transactional
    public Donation makeDonation(Long postId, Double amount, String message) {
        User donor = userService.getCurrentUser();
        CrowdfundingPost post = crowdfundingService.getById(postId);

        Donation donation = Donation.builder()
                .crowdfundingPost(post)
                .donor(donor)
                .amount(amount)
                .message(message)
                .build();

        // Update raised amount
        post.setCurrentAmount(post.getCurrentAmount() + amount);
        crowdfundingService.save(post);   // যদি save মেথড না থাকে তাহলে বলো

        return donationRepository.save(donation);
    }

    public List<Donation> getDonationsByPost(Long postId) {
        CrowdfundingPost post = crowdfundingService.getById(postId);
        return donationRepository.findByCrowdfundingPost(post);
    }
}