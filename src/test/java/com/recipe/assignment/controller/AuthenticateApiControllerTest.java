package com.recipe.assignment.controller;


import com.recipe.assignment.model.Request.AuthenticationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticateApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Test
    public void testAuthenticationSuccess() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUserName("abnUser");
        request.setPassword("testpassword");
        HttpEntity<AuthenticationRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(), HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("jwt");

    }

    @Test
    public void testAuthenticationFailure() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUserName("arun");
        request.setPassword("wrong");
        HttpEntity<AuthenticationRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(), HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private String createURLWithPort() {
        return "http://localhost:" + port + "/api/authenticate";
    }
}
