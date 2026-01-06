package com.openclassrooms.service;

import java.util.Collections;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.dto.RegisterDTO;
import com.openclassrooms.exception.UserAlreadyExistException;
import com.openclassrooms.model.User;
import com.openclassrooms.repository.RoleRepository;
import com.openclassrooms.repository.UserRepository;

@Service
public class AccountRepositoryBaseService implements AccountService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository ;

	public AccountRepositoryBaseService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
	}

	@Override
	public User save(RegisterDTO registerDto) throws UserAlreadyExistException {
		if (userRepository.existsByName(registerDto.getName())) {
			throw new UserAlreadyExistException(" user already exist "+registerDto.getName());
		}
		
		User user = new User();
		user.setName(registerDto.getName());
		user.setEmail(registerDto.getEmail());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

		roleRepository.findByName("CUSTOMER")
		.ifPresent(r -> user.setRoles(Collections.singletonList(r)));
		User saved=userRepository.save(user);
		return saved;
		
	}

}
