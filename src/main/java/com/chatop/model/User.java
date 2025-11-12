
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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // --- GETTERS & SETTERS  ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @PrePersist
    /** Initialise createdAt à l’insertion. */
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    /** Met à jour updatedAt à chaque update. */
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
