package com.openclassrooms.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="rentals")
public class Rental {
	
	public Rental() {}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private float surface;
	private float price;
	private String picture;
	private String description;
	@Column(name="created_at")
	private LocalDateTime createdAt ;
	@Column(name="updated_at")
	private LocalDateTime updatedAt ;
	
	@ManyToOne
	@JoinColumn(name= "owner_id")
    private User owner;
	
	@OneToMany(targetEntity=Message.class, cascade = CascadeType.ALL)
	@JoinColumn(name= "rental_id", referencedColumnName="id")
	private List<Message> Message;

}
