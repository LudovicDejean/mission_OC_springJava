package com.openclassrooms.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.openclassrooms.dto.RegisterDTO;
import com.openclassrooms.exception.UserAlreadyExistException;
import com.openclassrooms.jwt.TokenProvider;
import com.openclassrooms.service.AccountService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
@Tag(name="Register")
public class AccountController {

	private AccountService accountService;
	private TokenProvider tokenProvider;
	private AuthenticationManager authenticationManager;
	private final String AUTHORIZATION_HEADER = "Authorization";

	public AccountController(AccountService accountService, TokenProvider tokenProvider,
			AuthenticationManager authenticationManager) {
		this.accountService = accountService;
		this.tokenProvider = tokenProvider;
		this.authenticationManager = authenticationManager;
	}

	@Operation(summary = "Create user", description = "This method creates a new user and returns a token")
	@PostMapping("/register")
	public ResponseEntity<JWTToken> register(
			@RequestBody RegisterDTO registerDto)
			throws UserAlreadyExistException {
		accountService.save(registerDto);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				registerDto.getEmail(), registerDto.getPassword());

		Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.createToken(authentication);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(AUTHORIZATION_HEADER, "Bearer " + jwt);
		return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
	}
	@Hidden
	static class JWTToken {

		private String idToken;

		public JWTToken(String idToken) {
			this.idToken = idToken;
		}

		@JsonProperty("token")
		String getIdToken() {
			return idToken;
		}

		void setIdToken(String idToken) {
			this.idToken = idToken;
		}
	}
}
