package com.chatop.controller;

import com.chatop.model.User;
import com.chatop.security.JwtUtils;
import com.chatop.service.UserService;
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

    /** Enregistre un utilisateur puis retourne un JWT. */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody final User user) {
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already used"));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userService.save(user);
        String token = jwtUtils.generateToken(saved.getEmail(), saved.getRole());
        return ResponseEntity.ok(Map.of("token", token));
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