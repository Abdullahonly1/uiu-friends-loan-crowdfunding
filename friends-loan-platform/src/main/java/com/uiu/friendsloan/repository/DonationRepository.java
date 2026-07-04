package com.uiu.friendsloan.repository;

import com.uiu.friendsloan.entity.CrowdfundingPost;
import com.uiu.friendsloan.entity.Donation;
import com.uiu.friendsloan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    List<Donation> findByCrowdfundingPost(CrowdfundingPost post);

    List<Donation> findByDonor(User donor);

    List<Donation> findByCrowdfundingPostOrderByDonatedAtDesc(CrowdfundingPost post);
}