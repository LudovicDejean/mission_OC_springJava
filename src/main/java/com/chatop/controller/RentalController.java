package com.chatop.controller;

import com.chatop.model.Rental;
import com.chatop.model.User;
import com.chatop.service.RentalService;
import com.chatop.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Endpoints REST pour la gestion des locations.
 * Aucune dépendance à un Repository : on passe par le service.
 */
@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(final RentalService rentalService) {
        this.rentalService = rentalService;
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
    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<?> createRental(
            @RequestParam String name,
            @RequestParam Integer surface,
            @RequestParam Integer price,
            @RequestParam MultipartFile picture,
            @RequestParam String description,
            Authentication authentication
    ) {
        String email = authentication.getName();
        User owner = UserService.findByEmail(email).orElseThrow();

        Rental rental = new Rental();
        rental.setName(name);
        rental.setSurface(Double.valueOf(surface));
        rental.setPrice(Double.valueOf(price));
        rental.setDescription(description);
        rental.setOwnerId(owner.getId());

        rentalService.save(rental);

        return ResponseEntity.ok().body(Map.of("message", "Rental created"));
    }

    /** Supprime une location. */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<?> delete(@PathVariable final Long id) {
        rentalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}