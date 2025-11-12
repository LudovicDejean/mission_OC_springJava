package com.chatop.service;

import com.chatop.model.Rental;
import com.chatop.repository.RentalRepository;
import com.chatop.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;

/**
 * Service de gestion des locations (rentals).
 */
@Service
@Transactional
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;

    public RentalService(final RentalRepository rentalRepository,
                         final UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retourne toutes les locations.
     */
    @Transactional(readOnly = true)
    public List<Rental> findAll() {
        return rentalRepository.findAll();
    }

    /**
     * Recherche une location par son identifiant.
     */
    @Transactional(readOnly = true)
    public Optional<Rental> findById(final Long id) {
        return rentalRepository.findById(id);
    }

    /**
     * Sauvegarde une location. Si seul l'id du propriétaire est fourni,
     * résout l'entité User et la rattache à la location.
     */
    public Rental save(final Rental rental) {
        if (rental.getOwner() != null && rental.getOwner().getId() != null) {
            userRepository.findById(rental.getOwner().getId()).ifPresent(rental::setOwner);
        }
        return rentalRepository.save(rental);
    }

    /**
     * Supprime une location par id.
     */
    public void delete(final Long id) {
        rentalRepository.deleteById(id);
    }

    /**
     * Stocke un fichier sur disque (ex: upload d'image).
     * @param bytes contenu du fichier
     * @param target chemin cible
     * @return chemin absolu/relatif écrit
     */
    public String storeFile(final byte[] bytes, final Path target) throws IOException {
        Files.createDirectories(target.getParent());
        Files.write(target, bytes);
        return target.toString();
    }
}