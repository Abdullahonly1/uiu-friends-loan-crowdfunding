package com.uiu.friendsloan.controller;

import com.uiu.friendsloan.dto.RatingRequest;
import com.uiu.friendsloan.entity.UserRating;
import com.uiu.friendsloan.service.UserRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class UserRatingController {

    private final UserRatingService userRatingService;

    // Rating Submit
    @PostMapping
    public ResponseEntity<UserRating> addRating(@RequestBody RatingRequest request) {
        UserRating rating = userRatingService.addRating(
                request.getRatedUserId(),
                request.getRating(),
                request.getComment()
        );
        return ResponseEntity.ok(rating);
    }

    // যেকোনো ইউজারের রেটিং দেখা (সবার জন্য উন্মুক্ত)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserRating>> getUserRatings(@PathVariable Long userId) {
        return ResponseEntity.ok(userRatingService.getRatingsForUser(userId));
    }

    // Average Rating (সবার জন্য উন্মুক্ত)
    @GetMapping("/user/{userId}/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long userId) {
        return ResponseEntity.ok(userRatingService.getAverageRating(userId));
    }
}