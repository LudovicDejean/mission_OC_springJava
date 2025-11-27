
package com.chatop.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Jwts;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService){
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        // ---------- FIX MULTIPART ----------
        // Certains navigateurs / Angular / Postman en multipart peuvent faire perdre les headers
        if (header == null && request instanceof MultipartHttpServletRequest multipartRequest) {
            header = multipartRequest.getHeader("Authorization");
        }
        // ------------------------------------

        try {
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);

                if (jwtUtils.validateJwtToken(token)) {
                    String username = jwtUtils.getUserNameFromJwtToken(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    String role = jwtUtils.getRoleFromJwtToken(token);
                    UsernamePasswordAuthenticationToken auth;

                    if (role != null && !role.isBlank()) {
                        auth = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                List.of(new SimpleGrantedAuthority(role))
                        );
                    } else {
                        // fallback si jamais le token n'a pas de claim role
                        auth = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                    }

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (IllegalArgumentException ex) {
            // évite de casser toute la chaîne sur un rôle null
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
        //System.out.println("Header Authorization = " + header);
    }
}
