package com.openclassrooms.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="users")
public class User {
	
	
	public User() {}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String email;
	private String name;
	private String password;
	@Column(name="created_at")
	private LocalDateTime createdAt ;
	@Column(name="updated_at")
	private LocalDateTime updatedAt ;
	
	
	@OneToMany(targetEntity=Rental.class, cascade = CascadeType.ALL)
	@JoinColumn(name= "owner_id", referencedColumnName="id")
	private List<Rental> Rental;
	
	@OneToMany(targetEntity=Message.class, cascade = CascadeType.ALL)
	@JoinColumn(name= "user_id", referencedColumnName="id")
	private List<Message> Message;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", table="users"),
	inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id", table="role"))
	private List<Role> roles = new ArrayList<>();


}