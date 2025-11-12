
package com.chatop.service;

import com.chatop.model.User;
import com.chatop.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service d'accès et de gestion des utilisateurs.
 * Toutes les méthodes sont transactionnelles par défaut.
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Recherche un utilisateur par email.
     * @param email email unique
     * @return Optional<User>
     */
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Recherche un utilisateur par identifiant.
     * @param id identifiant
     * @return Optional<User>
     */
    @Transactional(readOnly = true)
    public Optional<User> findById(final Long id) {
        return userRepository.findById(id);
    }

    /**
     * Crée ou met à jour un utilisateur.
     * @param user utilisateur
     * @return utilisateur persistant
     */
    public User save(final User user) {
        return userRepository.save(user);
    }
}
