package com.chatop.controller;

import com.chatop.model.Rental;
import com.chatop.service.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    public ResponseEntity<?> create(@RequestPart("rental") final Rental rental,
                                    @RequestPart(value = "picture", required = false) final MultipartFile picture)
            throws IOException {
        if (picture != null && !picture.isEmpty()) {
            // Tu peux injecter UPLOAD_DIR via @Value("${chatop.upload.dir}")
            Path uploadDir = Paths.get("uploads");
            Path target = uploadDir.resolve(picture.getOriginalFilename());
            rentalService.storeFile(picture.getBytes(), target);
            rental.setPicture(target.toString());
        }
        Rental saved = rentalService.save(rental);
        return ResponseEntity.ok(saved);
    }

    /** Supprime une location. */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<?> delete(@PathVariable final Long id) {
        rentalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}