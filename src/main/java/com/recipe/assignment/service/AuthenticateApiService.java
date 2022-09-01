package com.recipe.assignment.service;


import com.recipe.assignment.component.JWTTokenProvider;
import com.recipe.assignment.model.Request.AuthenticationRequest;
import com.recipe.assignment.model.Response.AuthenticationResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticateApiService implements AuthenticateApi {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticateApiService.class);

    private final AuthenticationManager authenticationManager;

    private final JWTTokenProvider jwtTokenProvider;

    private final UserDetailsService userDetailsService;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        try {
            String username = authenticationRequest.getUserName();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword()));
            String token = jwtTokenProvider.createToken(username, this.userDetailsService.loadUserByUsername(username).getAuthorities());
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setJwt(token);
            logger.info("JWT Token is generated and returned through response");
            return authenticationResponse;
        } catch (AuthenticationException e) {
            logger.error("user credentials are wrong, throwing BadCredentialsException");
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }
}