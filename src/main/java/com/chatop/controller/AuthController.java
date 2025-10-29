
package com.chatop.controller;

import com.chatop.model.User;
import com.chatop.repository.UserRepository;
import com.chatop.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        if(userRepository.existsByEmail(user.getEmail())){
            return ResponseEntity.badRequest().body(Map.of("error","email already used"));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getRole() == null) user.setRole("TENANT");
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("status","ok"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body){
        String email = body.get("email");
        String password = body.get("password");
        var opt = userRepository.findByEmail(email);
        if(opt.isEmpty()) return ResponseEntity.status(401).body(Map.of("error","invalid credentials"));
        var user = opt.get();
        if(!passwordEncoder.matches(password, user.getPassword())) return ResponseEntity.status(401).body(Map.of("error","invalid credentials"));
        String token = jwtUtils.generateToken(user.getEmail(), user.getRole());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(org.springframework.security.core.Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }

        // 👉 Récupérer l'email de l'utilisateur connecté
        String email = authentication.getName();

        var opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        var user = opt.get();

        // 👉 Construire la réponse au format attendu par le front
        Map<String, Object> body = Map.of(
                "id", user.getId(),
                "name", (user.getFirstName() == null ? "" : user.getFirstName())
                        + (user.getLastName() == null ? "" : (" " + user.getLastName())),
                "email", user.getEmail(),
                "created_at", user.getCreatedAt(),
                "updated_at", user.getUpdatedAt()
        );
        return ResponseEntity.ok(body);
    }
}
