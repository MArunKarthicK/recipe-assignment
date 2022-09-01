package com.recipe.assignment.controller;

import com.recipe.assignment.model.Request.AuthenticationRequest;
import com.recipe.assignment.service.AuthenticateApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Validated
@Tag(name = "Authentication", description = "To Authenticate API")
public class AuthenticateApiController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticateApiController.class);

    private final AuthenticateApi authenticateApi;

    /**
     * POST /authenticate : Authenticate the user
     * Authenticate user by credentials and generate JWT
     *
     * @param authenticationRequest has userName and password to authenticate (required)
     * @return successful operation - jwt response (status code 200)
     * or Invalid Request (status code 400)
     * or not found (status code 404)
     */
    @Operation(summary = "Authenticate the user", method = "authenticate", description = "Authenticate user by credentials and generate JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation - jwt response"),
            @ApiResponse(responseCode = "400", description = "Invalid Request"),
            @ApiResponse(responseCode = "404", description = "not found")})
    @PostMapping(
            value = "/authenticate",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    ResponseEntity<?> authenticate(@Parameter(description = "has userName and password to authenticate", required = true)
                                   @Valid @RequestBody AuthenticationRequest authenticationRequest) {
        logger.info("Request received for authentication at /api/authenticate");
        return ResponseEntity.ok().body(authenticateApi.authenticate(authenticationRequest));
    }
}