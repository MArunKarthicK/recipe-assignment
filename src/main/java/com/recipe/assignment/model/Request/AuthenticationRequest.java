package com.recipe.assignment.model.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * AuthenticationRequest
 */
public class AuthenticationRequest {
    @NotNull
    @JsonProperty("userName")
    private String userName;
    @NotNull
    @JsonProperty("password")
    private String password;

    /**
     * Get userName
     *
     * @return userName
     */
    @Schema(example = "abnUser")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Get password
     *
     * @return password
     */
    @Schema(example = "userpass")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthenticationRequest authenticationRequest = (AuthenticationRequest) o;
        return Objects.equals(this.userName, authenticationRequest.userName) &&
                Objects.equals(this.password, authenticationRequest.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AuthenticationRequest {\n");

        sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
        sb.append("    password: ").append(toIndentedString(password)).append("\n");
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

