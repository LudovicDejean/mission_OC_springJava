package com.openclassrooms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByEmail(String email);

	Boolean existsByName(String name);

	Optional<User> findByName(String name);

	Optional<Integer> findIdByName(String name);
}
