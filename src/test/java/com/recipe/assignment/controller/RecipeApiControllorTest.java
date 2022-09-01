package com.recipe.assignment.controller;

import com.recipe.assignment.model.Response.RecipeResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RecipeApiControllorTest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate = new TestRestTemplate();


    @Test
    public void testGetAllRecipes() {
        HashMap<String, String> urlVariables = new HashMap<>();
        urlVariables.put("sortBy", "id");
        urlVariables.put("pageNo", "0");
        urlVariables.put("pageSize", "11");
        ResponseEntity<RecipeResponse[]> response = restTemplate.getForEntity(
                createURLWithPort("/api/getAllRecipes"), RecipeResponse[].class, urlVariables);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void testGetRecipeByRecipeKey() {
        HashMap<String, String> urlVariables = new HashMap<>();
        urlVariables.put("recipeKey", "q2BmKLaPcypUW1IuPCTnQg");

        ResponseEntity<RecipeResponse> response = restTemplate.getForEntity(
                createURLWithPort("/api/recipe/{recipeKey}"), RecipeResponse.class, urlVariables);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
