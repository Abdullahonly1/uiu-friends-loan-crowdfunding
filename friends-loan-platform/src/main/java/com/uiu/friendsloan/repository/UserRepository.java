package com.uiu.friendsloan.repository;

import com.uiu.friendsloan.entity.Role;
import com.uiu.friendsloan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(Role role);

    List<User> findByIsActiveTrue();

    // Admin এর জন্য সব User (active/inactive সব)
    List<User> findAll();

    // Trust Score অনুসারে Top Users
    @Query("SELECT u FROM User u WHERE u.isActive = true ORDER BY u.trustScore DESC")
    List<User> findTopUsersByTrustScore();
}