
package com.chatop.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * Représente un utilisateur de l'application.
 */
@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;  // correspond à la colonne `name` et au front

    @Column(nullable = false)
    private String password;

    /**
     * OWNER / TENANT etc.
     * On met une valeur par défaut "TENANT" pour les nouveaux inscrits.
     */
    @Column(nullable = false)
    private String role = "TENANT";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}