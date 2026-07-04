package com.uiu.friendsloan.service;

import com.uiu.friendsloan.dto.CrowdfundingPostRequest;
import com.uiu.friendsloan.entity.CrowdfundingPost;
import com.uiu.friendsloan.entity.PostStatus;
import com.uiu.friendsloan.entity.User;
import com.uiu.friendsloan.repository.CrowdfundingPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CrowdfundingService {

    private final CrowdfundingPostRepository crowdfundingPostRepository;
    private final UserService userService;

    public CrowdfundingService(CrowdfundingPostRepository crowdfundingPostRepository, UserService userService) {
        this.crowdfundingPostRepository = crowdfundingPostRepository;
        this.userService = userService;
    }

    public CrowdfundingPost createPost(CrowdfundingPostRequest request) {
        User user = userService.getCurrentUser();

        CrowdfundingPost post = CrowdfundingPost.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .amountNeeded(request.getAmountNeeded())
                .isUrgent(request.isUrgent())
                .status(PostStatus.PENDING)
                .currentAmount(0.0)
                .build();

        return crowdfundingPostRepository.save(post);
    }

    public CrowdfundingPost getById(Long id) {
        return crowdfundingPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crowdfunding Post not found"));
    }

    @Transactional
    public CrowdfundingPost save(CrowdfundingPost post) {
        return crowdfundingPostRepository.save(post);
    }

    public List<CrowdfundingPost> getApprovedPosts() {
        return crowdfundingPostRepository.findByStatus(PostStatus.APPROVED);
    }

    public List<CrowdfundingPost> getMyPosts(String email) {
        User currentUser = userService.getCurrentUser();
        return crowdfundingPostRepository.findByUser(currentUser);
    }

    public List<CrowdfundingPost> getPendingPosts() {
        return crowdfundingPostRepository.findByStatus(PostStatus.PENDING);
    }

    public void approvePost(Long postId) {
        CrowdfundingPost post = getById(postId);
        post.setStatus(PostStatus.APPROVED);
        post.setApprovedAt(LocalDateTime.now());
        crowdfundingPostRepository.save(post);
    }

    public void rejectPost(Long postId) {
        CrowdfundingPost post = getById(postId);
        post.setStatus(PostStatus.REJECTED);
        crowdfundingPostRepository.save(post);
    }
}