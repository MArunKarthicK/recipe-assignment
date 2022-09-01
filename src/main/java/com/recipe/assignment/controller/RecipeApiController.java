package com.recipe.assignment.controller;

import com.recipe.assignment.model.Request.RecipeFilterRequest;
import com.recipe.assignment.model.Request.RecipeRequest;
import com.recipe.assignment.model.Response.RecipeResponse;
import com.recipe.assignment.service.RecipeApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Validated
@Tag(name = "Recipe Api", description = "Recipe management web API")
@SecurityRequirement(name = "bearerAuth")
public class RecipeApiController {

    private final RecipeApi recipeApi;

    /**
     * POST /createRecipe : Create a Recipe
     * create a recipe and add to db
     *
     * @param recipeRequest recipe details (required)
     * @return Successful operation - new recipe Created (status code 200)
     * or Invalid input (status code 400)
     * or not found (status code 404)
     */
    @Operation(summary = "Create a Recipe", method = "createNewRecipe", description = "create a new recipe and add to db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation - new Recipe Created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "not found")})
    @PostMapping(
            value = "/createRecipe",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    ResponseEntity<?> createNewRecipe(@Parameter(description = "Recipe details", required = true) @Valid @RequestBody RecipeRequest recipeRequest, Principal principal) {
        log.info("Request received: /recipe to create new recipe");
        RecipeResponse recipeResponse = recipeApi.createNewRecipe(recipeRequest,principal.getName());
        return ResponseEntity.ok().body(recipeResponse);
    }

    /**
     * DELETE /recipe/{recipeKey} : Delete a recipe
     * delete a recipe
     *
     * @param recipeKey recipeKey to find recipe (required)
     * @return deleted successful - recipe deleted (status code 200)
     * or Invalid recipeKey (status code 400)
     * or not found (status code 404)
     */
    @Operation(summary = "Delete a recipe", operationId = "delete recipe", description = "delete a recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "deleted successful - recipe deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid recipeKey"),
            @ApiResponse(responseCode = "404", description = "not found")})
    @DeleteMapping(
            value = "/recipe/{recipeKey}"
    )
    ResponseEntity<?> deleteRecipe(@Parameter(description = "recipeKey to find recipe", required = true) @PathVariable("recipeKey") String recipeKey, Principal principal) {
        log.info("Request received: /recipe/{} to delete recipe",recipeKey);
        recipeApi.deleteRecipe(recipeKey,principal.getName());
        return ResponseEntity.ok("Recipe deleted successfully");
    }


    /**
     * GET /getAllRecipes : Get All Recipes
     * get a list of Recipes
     *
     * @param sortBy  (optional, default to id)
     * @param pageNo   (optional, default to 0)
     * @param pageSize (optional, default to 10)
     * @return Successful operation - List of recipe as response (status code 200)
     * or recipe not found (status code 404)
     * or bad request (status code 400)
     */
    @Operation(summary = "Get All Recipes", operationId = "findAllRecipes", description = "get a list of recipes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation - List of recipe as response"),
            @ApiResponse(responseCode = "404", description = "recipe not found"),
            @ApiResponse(responseCode = "400", description = "bad request")})
    @GetMapping(
            value = "/getAllRecipes",
            produces = {"application/json"}
    )
    ResponseEntity<?> findAllRecipes(@Parameter(description = "sortBy Id default") @Valid @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy, @Parameter(description = "default PageNo is 0") @Valid @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo, @Parameter(description = "default pageSize is 10") @Valid @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        log.info("Request received: /getAllRecipes to fetch all recipes");
        List<RecipeResponse> allRecipes = recipeApi.findAllRecipes(sortBy, pageNo, pageSize);
        return ResponseEntity.ok().body(allRecipes);
    }

    /**
     * GET /getAllRecipes : Get All Recipes
     * get a list of Recipes
     *
     * @return Successful operation - List of recipe as response (status code 200)
     * or recipe not found (status code 404)
     * or bad request (status code 400)
     */
    @Operation(summary = "Get All Recipes", operationId = "findAllRecipes", description = "get a list of recipes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation - List of recipes as response"),
            @ApiResponse(responseCode = "404", description = "recipe not found"),
            @ApiResponse(responseCode = "400", description = "bad request")})
    @PostMapping(
            value = "/filterRecipes",
            produces = {"application/json"}
    )
    ResponseEntity<?> filterRecipes(@Parameter(description = "Recipe details", required = true) @Valid @RequestBody RecipeFilterRequest recipeFilterRequest,
                                    @Parameter(description = "sortBy Id default") @Valid @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy, @Parameter(description = "default PageNo is 0") @Valid @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo, @Parameter(description = "default pageSize is 10") @Valid @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        log.info("Request received: /getAllRecipes to fetch all recipes");
        List<RecipeResponse> allRecipes = recipeApi.filterRecipes(recipeFilterRequest,sortBy,pageNo,pageSize);
        return ResponseEntity.ok().body(allRecipes);
    }


    /**
     * GET /recipe/{recipeKey} : Find recipe by recipeKey
     * get recipe based on recipeKey
     *
     * @param recipeKey recipeKey to find recipe (required)
     * @return successful operation - recipe as response (status code 200)
     * or Invalid recipe (status code 400)
     * or not found (status code 404)
     */
    @Operation(summary = "Find recipe by recipeKey", operationId = "getrecipeByrecipeKey", description = "get recipe based on recipeKey")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation - recipe as response"),
            @ApiResponse(responseCode = "400", description = "Invalid recipeKey"),
            @ApiResponse(responseCode = "404", description = "not found")})
    @GetMapping(
            value = "/recipe/{recipeKey}",
            produces = {"application/json"}
    )
     ResponseEntity<?> getRecipeByRecipeKey(@Parameter(description = "recipeKey to find recipe ", required = true) @PathVariable("recipeKey") String recipeKey) {
        log.info("Request received: /recipe/{} to find a based recipeKey",recipeKey);
        RecipeResponse allRecipes = recipeApi.getRecipeByRecipeKey(recipeKey);
        return ResponseEntity.ok().body(allRecipes);
    }

    /**
     * PUT /recipe/{recipeKey} : Update a recipe
     * Update a recipe
     *
     * @param recipeKey     recipeKey to find recipe (required)
     * @param recipeRequest update recipe in db (required)
     * @return successful operation - recipe updated (status code 200)
     * or Invalid input (status code 400)
     * or not found (status code 404)
     */
    @Operation(summary = "Update a recipe", operationId = "updaterecipe", description = "Update a recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation - recipe updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "not found")})
    @PutMapping(
            value = "/recipe/{recipeKey}",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    ResponseEntity<?> updateRecipe(@Parameter(description = "recipeKey to find recipe", required = true) @PathVariable("recipeKey") String recipeKey, @Parameter(description = "recipe details", required = true) @Valid @RequestBody RecipeRequest recipeRequest, Principal principal) {
        log.info("Request received: /recipe/{} to update recipe details based recipeKey",recipeKey);
        RecipeResponse recipeResponse =recipeApi.updateRecipe(recipeKey, recipeRequest, principal.getName());
        return ResponseEntity.ok().body(recipeResponse);
    }
}