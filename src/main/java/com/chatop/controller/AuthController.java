package com.chatop.controller;

import com.chatop.model.User;
import com.chatop.security.JwtUtils;
import com.chatop.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Authentification / inscription et endpoint "me".
 * Passe par UserService (aucun accès direct au repository).
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthController(final UserService userService,
                          final PasswordEncoder passwordEncoder,
                          final JwtUtils jwtUtils) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    // DTOs
    public record RegisterRequest(String email, String name, String password) {}
    public record LoginRequest(String email, String password) {}

    /** Enregistre un utilisateur puis retourne un JWT. */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (userService.existsByEmail(request.email())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Email already used"));
        }

        User user = new User();
        user.setEmail(request.email());
        user.setName(request.name());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole("TENANT"); // rôle par défaut

        User saved = userService.save(user);
        String token = jwtUtils.generateToken(saved.getEmail(), saved.getRole());

        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userService.findByEmail(request.email())
                .filter(u -> passwordEncoder.matches(request.password(), u.getPassword()))
                .<ResponseEntity<?>>map(user -> {
                    String token = jwtUtils.generateToken(user.getEmail(), user.getRole());
                    return ResponseEntity.ok(Map.of("token", token));
                })
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid credentials")));
    }

    /** Retourne le profil “me” à partir du token Bearer. */
    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("Authorization") final String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtils.getUserNameFromJwtToken(token);
        return userService.findByEmail(email)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}