package com.github.pallocchi.positions.auth;

import com.github.pallocchi.positions.config.JwtConfig;
import com.github.pallocchi.positions.services.PositionService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Collections.emptyList;

/**
 * Filter to validate JWT tokens, and if they are valid, register the subject in security context.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final static Logger LOGGER = LoggerFactory.getLogger(PositionService.class);

    private final JwtConfig config;

    @Autowired
    public JwtAuthenticationFilter(JwtConfig config) {
        this.config = config;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)  throws ServletException, IOException {

        final String header = request.getHeader(config.getHeader());

        if(header != null && header.startsWith(config.getPrefix())) {

            // Get the token from header
            final String token = header.replace(config.getPrefix(), "");

            try {

                // Validate the token
                final Claims claims = Jwts.parser()
                    .setSigningKey(config.getSecret().getBytes())
                    .parseClaimsJws(token)
                    .getBody();

                final String username = claims.getSubject();

                if(username != null) {

                    // Create the authentication, which does not have credentials
                    // since we haven't implemented a users module yet
                    final Authentication auth = new UsernamePasswordAuthenticationToken(username, null, emptyList());

                    // Now, user is authenticated
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

            } catch (Exception e) {

                LOGGER.debug("Authentication failed!", e);

                // In case of failure. Make sure it's clear; so guarantee user won't be authenticated
                SecurityContextHolder.clearContext();
            }

        }

        chain.doFilter(request, response);

    }

}
