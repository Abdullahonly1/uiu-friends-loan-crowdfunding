package com.uiu.friendsloan.repository;

import com.uiu.friendsloan.entity.CrowdfundingPost;
import com.uiu.friendsloan.entity.PostStatus;
import com.uiu.friendsloan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrowdfundingPostRepository extends JpaRepository<CrowdfundingPost, Long> {

    // Admin এর জন্য Pending posts
    List<CrowdfundingPost> findByStatus(PostStatus status);

    // User এর নিজের posts
    List<CrowdfundingPost> findByUser(User user);

    // Urgent + Pending posts (Admin-এ highlight করার জন্য)
    List<CrowdfundingPost> findByIsUrgentTrueAndStatus(PostStatus status);

    // সবচেয়ে নতুন Pending posts প্রথমে
    List<CrowdfundingPost> findByStatusOrderByCreatedAtDesc(PostStatus status);

    // Specific user এর pending posts
    List<CrowdfundingPost> findByUserAndStatus(User user, PostStatus status);
}