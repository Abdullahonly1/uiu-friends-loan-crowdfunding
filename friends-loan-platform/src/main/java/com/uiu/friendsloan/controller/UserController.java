package com.uiu.friendsloan.controller;

import com.uiu.friendsloan.entity.User;
import com.uiu.friendsloan.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get All Users (Admin use করতে পারবে)
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Get User by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // Update Trust Score (Admin)
    @PutMapping("/{id}/trust-score")
    public ResponseEntity<String> updateTrustScore(@PathVariable Long id, @RequestParam double score) {
        userService.updateTrustScore(id, score);
        return ResponseEntity.ok("Trust Score Updated Successfully!");
    }
}