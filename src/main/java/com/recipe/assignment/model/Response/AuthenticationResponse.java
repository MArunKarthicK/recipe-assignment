package com.recipe.assignment.model.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

/**
 * AuthenticationResponse
 */
public class AuthenticationResponse {
    @JsonProperty("jwt")
    private String jwt;

    /**
     * Get jwt
     *
     * @return jwt
     */
    @Schema(example = "aasweqwqdderf")
    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthenticationResponse authenticationResponse = (AuthenticationResponse) o;
        return Objects.equals(this.jwt, authenticationResponse.jwt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jwt);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AuthenticationResponse {\n");

        sb.append("    jwt: ").append(toIndentedString(jwt)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

