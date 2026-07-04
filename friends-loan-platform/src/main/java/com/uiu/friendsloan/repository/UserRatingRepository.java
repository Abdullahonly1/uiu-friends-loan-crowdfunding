package com.uiu.friendsloan.repository;

import com.uiu.friendsloan.entity.User;
import com.uiu.friendsloan.entity.UserRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.OptionalDouble;

@Repository
public interface UserRatingRepository extends JpaRepository<UserRating, Long> {

    List<UserRating> findByRatedUserId(Long ratedUserId);

    // ✅ Average Rating এর জন্য সঠিক Query


    @Query("SELECT COALESCE(AVG(u.rating), 0.0) FROM UserRating u WHERE u.ratedUser.id = :userId")
    Double findAverageRatingByRatedUserId(@Param("userId") Long userId);

    boolean existsByRaterUserAndRatedUser(User raterUser, User ratedUser);
}