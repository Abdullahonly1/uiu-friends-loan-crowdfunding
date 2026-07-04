package com.uiu.friendsloan.controller;

import com.uiu.friendsloan.dto.AuthRequest;
import com.uiu.friendsloan.dto.AuthResponse;
import com.uiu.friendsloan.dto.RegisterRequest;
import com.uiu.friendsloan.entity.User;
import com.uiu.friendsloan.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:8080")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = userService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        try {
            User user = userService.getCurrentUser();
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            System.out.println("Error in /me: " + e.getMessage());
            return ResponseEntity.status(401).build();
        }
    }
}