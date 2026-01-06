package com.openclassrooms.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import static org.springframework.security.config.Customizer.withDefaults;
import com.openclassrooms.jwt.JwtAuthEntryPoint;
import com.openclassrooms.jwt.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final UserDetailsService userDetailsService;
	private final JwtAuthEntryPoint authEntryPoint;
	private final JwtFilter jwtFilter;

	public SecurityConfig(UserDetailsService userDetailsService,
			JwtAuthEntryPoint authEntryPoint, JwtFilter jwtFilter) {
		this.userDetailsService = userDetailsService;
		this.authEntryPoint = authEntryPoint;
		this.jwtFilter = jwtFilter;
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.addFilterBefore(new CorsFilter(), UsernamePasswordAuthenticationFilter.class).cors(withDefaults())
				.csrf((AbstractHttpConfigurer::disable));
		http.exceptionHandling(handling -> handling.authenticationEntryPoint(authEntryPoint));
		http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.securityMatcher("/api/**").authorizeHttpRequests(rmr -> rmr
				.requestMatchers("/api/auth/login", "/api/auth/register", "/swagger*/**", "/v3/api-docs/**","/api/rentals/image/**").permitAll()
	            .requestMatchers("/api/**").authenticated()				
				);
		http.authenticationProvider(authenticationProvider())
		.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}