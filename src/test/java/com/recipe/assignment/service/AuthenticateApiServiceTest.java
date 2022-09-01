package com.recipe.assignment.service;

import com.recipe.assignment.component.JWTTokenProvider;
import com.recipe.assignment.model.Request.AuthenticationRequest;
import com.recipe.assignment.model.Response.AuthenticationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticateApiServiceTest {

    @InjectMocks
    AuthenticateApiService authenticateApiService;

    @Mock
    JWTTokenProvider jwtTokenProvider;

    @Mock
    UserDetailsService userDetailsService;

    @Mock
    AuthenticationManager authenticationManager;

    String mockJwt = "aferfew!!@#$_y498r";
    Authentication authentication = mock(Authentication.class);
    User user = new User("test", "padd", new ArrayList<>());

    @Test
    public void shouldAuthenticateandGiveJWTBasedonRequestTest() {
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenProvider.createToken(any(), any())).thenReturn(mockJwt);
        when(userDetailsService.loadUserByUsername(any())).thenReturn(user);
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUserName("test");
        authenticationRequest.setPassword("pass");
        AuthenticationResponse authenticationResponse = authenticateApiService.authenticate(authenticationRequest);
        assertThat(authenticationResponse).isNotNull();
        assertThat(authenticationResponse.getJwt()).isEqualTo(mockJwt);
    }

    @Test
    public void shouldThrowsExceptionBasedonRequestTest() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException(""));
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUserName("test");
        authenticationRequest.setPassword("pass");
        Exception badCredentialsException = assertThrows(BadCredentialsException.class, () ->
            authenticateApiService.authenticate(authenticationRequest));
        assertThat(badCredentialsException.getMessage()).isEqualTo("Invalid username/password supplied");
    }
}
