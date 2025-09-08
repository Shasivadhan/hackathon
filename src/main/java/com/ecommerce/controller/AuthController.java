package com.ecommerce.controller;

import com.ecommerce.entity.User;

import com.ecommerce.repo.UserRepository;

import com.ecommerce.security.CustomUserDetailsService;

import com.ecommerce.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.*;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/auth")

public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepo;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final CustomUserDetailsService userDetailsService;

    @Autowired

    public AuthController(AuthenticationManager authManager, UserRepository repo, PasswordEncoder encoder,

                          JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {

        this.authenticationManager = authManager;

        this.userRepo = repo;

        this.passwordEncoder = encoder;

        this.jwtUtil = jwtUtil;

        this.userDetailsService = userDetailsService;

    }

    @PostMapping("/register")

    public String register(@RequestBody User user) {

        if (user.getUsername() == null || user.getUsername().isBlank())

            throw new IllegalArgumentException("Username required");

        if (user.getPassword() == null || user.getPassword().length() < 5)

            throw new IllegalArgumentException("Password must be at least 5 characters");

        if (userRepo.findByUsername(user.getUsername()) != null)

            throw new IllegalArgumentException("User already exists");

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRole("USER");

        userRepo.save(user);

        return "User registered";

    }

    @PostMapping("/login")

    public String login(@RequestBody User user) {

        if (user.getUsername() == null || user.getPassword() == null)

            throw new IllegalArgumentException("Username and password are required");

        authenticationManager

                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        UserDetails details = userDetailsService.loadUserByUsername(user.getUsername());

        return jwtUtil.generateToken(details.getUsername());

    }

}
