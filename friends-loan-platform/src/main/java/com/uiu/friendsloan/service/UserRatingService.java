package com.uiu.friendsloan.service;

import com.uiu.friendsloan.entity.User;
import com.uiu.friendsloan.entity.UserRating;
import com.uiu.friendsloan.repository.UserRatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRatingService {

    private final UserRatingRepository userRatingRepository;
    private final UserService userService;

    // Rating দেয়া + Trust Score আপডেট
    @Transactional
    public UserRating addRating(Long ratedUserId, int rating, String comment) {
        User rater = userService.getCurrentUser();
        User ratedUser = userService.getUserById(ratedUserId);

        if (rater.getId().equals(ratedUserId)) {
            throw new RuntimeException("You cannot rate yourself");
        }

        boolean alreadyRated = userRatingRepository.existsByRaterUserAndRatedUser(rater, ratedUser);
        if (alreadyRated) {
            throw new RuntimeException("You have already rated this user");
        }

        UserRating userRating = new UserRating();
        userRating.setRatedUser(ratedUser);
        userRating.setRaterUser(rater);
        userRating.setRating(rating);
        userRating.setComment(comment);

        UserRating savedRating = userRatingRepository.save(userRating);

        // Trust Score আপডেট করো
        updateTrustScore(ratedUser);

        return savedRating;
    }

    // Trust Score ক্যালকুলেট করে আপডেট করা
    private void updateTrustScore(User user) {
        double avgRating = getAverageRating(user.getId());
        double newTrustScore = avgRating * 20.0;   // 5 স্টার = 100

        // 0 থেকে 100 এর মধ্যে রাখা
        newTrustScore = Math.max(0.0, Math.min(100.0, newTrustScore));

        user.setTrustScore(Math.round(newTrustScore * 10.0) / 10.0);
        userService.saveUser(user);

        System.out.println("✅ Trust Score Updated | User ID: " + user.getId()
                + " | Avg Rating: " + avgRating
                + " | New Trust Score: " + user.getTrustScore());
    }

    // কোনো ইউজারের সব রেটিং দেখা
    public List<UserRating> getRatingsForUser(Long ratedUserId) {
        return userRatingRepository.findByRatedUserId(ratedUserId);
    }

    // Average Rating বের করা
    public double getAverageRating(Long ratedUserId) {
        Double avg = userRatingRepository.findAverageRatingByRatedUserId(ratedUserId);
        return avg != null ? avg : 0.0;
    }
}