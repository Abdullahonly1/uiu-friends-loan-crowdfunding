package com.uiu.friendsloan.service;

import com.uiu.friendsloan.dto.AuthRequest;
import com.uiu.friendsloan.dto.AuthResponse;
import com.uiu.friendsloan.dto.RegisterRequest;
import com.uiu.friendsloan.entity.Role;
import com.uiu.friendsloan.entity.User;
import com.uiu.friendsloan.repository.UserRepository;
import com.uiu.friendsloan.config.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        Role userRole = Role.USER;
        String emailLower = request.getEmail().toLowerCase();

        if (emailLower.contains("admin") || emailLower.endsWith("@admin.uiu.edu")) {
            userRole = Role.ADMIN;
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(userRole)
                .trustScore(50.0)
                .isActive(true)
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return new AuthResponse(token, user.getName(), user.getRole().name(), user.getId(), user.getTrustScore(), "Registration successful!");
    }

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return new AuthResponse(token, user.getName(), user.getRole().name(), user.getId(), user.getTrustScore(), "Login successful!");
    }

    // ✅ এই মেথডটা সবচেয়ে গুরুত্বপূর্ণ — এটাকে সহজ করা হয়েছে
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {
            throw new RuntimeException("User not authenticated. Please login again.");
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void updateTrustScore(Long userId, double score) {
        User user = getUserById(userId);
        user.setTrustScore(Math.max(0, Math.min(100, score)));
        userRepository.save(user);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}