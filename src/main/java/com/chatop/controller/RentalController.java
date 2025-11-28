package com.chatop.controller;

import com.chatop.mapper.RentalMapper;
import com.chatop.model.Rental;
import com.chatop.model.User;
import com.chatop.service.RentalService;
import com.chatop.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Endpoints REST pour la gestion des locations.
 * Aucune dépendance à un Repository : on passe par le service.
 */
@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;
    private final UserService userService;
    private final RentalMapper rentalMapper;

    public RentalController(final RentalService rentalService, UserService userService, RentalMapper rentalMapper) {
        this.rentalService = rentalService;
        this.userService = userService;
        this.rentalMapper = rentalMapper;
    }

    /** Retourne toutes les locations. */
    @GetMapping
    public ResponseEntity<?> all() {
        return ResponseEntity.ok(rentalService.findAll());
    }

    /** Retourne une location par id. */
    @GetMapping("/{id}")
    public ResponseEntity<?> byId(@PathVariable final Long id) {
        return rentalService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /** Crée une location (multipart) avec fichier optionnel. */
    @PostMapping(value = "/rentals", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<?> create(
            @RequestParam("name") String name,
            @RequestParam("surface") Integer surface,
            @RequestParam("price") Integer price,
            @RequestParam(value = "picture", required = false) MultipartFile picture,
            @RequestParam("description") String description,
            Authentication authentication
    ) throws IOException {

        // Récupérer l'email depuis le token JWT
        String email = authentication.getName();
        User owner = userService.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        Rental rental = new Rental();
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);
        rental.setOwner(owner); // ManyToOne<User> owner

        // Gestion du fichier si présent
        if (picture != null && !picture.isEmpty()) {
            Path uploadDir = Paths.get("uploads");
            Files.createDirectories(uploadDir);

            Path target = uploadDir.resolve(picture.getOriginalFilename());
            Files.copy(picture.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            rental.setPicture(target.getFileName().toString());
        }

        Rental saved = rentalService.save(rental);

        return ResponseEntity.ok(rentalMapper.toDto(saved));
    }

    /** Supprime une location. */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<?> delete(@PathVariable final Long id) {
        rentalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}