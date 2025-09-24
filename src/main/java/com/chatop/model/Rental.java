
package com.chatop.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "RENTALS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Rental {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double surface;
    private Double price;
    private String picture;
    @Column(length = 2000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist(){ this.createdAt = LocalDateTime.now(); }
    @PreUpdate
    public void preUpdate(){ this.updatedAt = LocalDateTime.now(); }

    // Getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

}
