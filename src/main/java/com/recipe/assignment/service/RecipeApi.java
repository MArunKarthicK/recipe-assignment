package com.recipe.assignment.service;

import com.recipe.assignment.model.Request.RecipeFilterRequest;
import com.recipe.assignment.model.Request.RecipeRequest;
import com.recipe.assignment.model.Response.RecipeResponse;

import java.util.List;

public interface RecipeApi {

    RecipeResponse createNewRecipe(RecipeRequest recipeRequest, String UserName);

    List<RecipeResponse> findAllRecipes(String sortBy, int pageNo, int pageSize);

    List<RecipeResponse> filterRecipes(RecipeFilterRequest recipeFilterRequest, String sortBy, int pageNo, int pageSize);

    void deleteRecipe(String recipeKey, String UserName);

    RecipeResponse getRecipeByRecipeKey(String recipeKey);

    RecipeResponse updateRecipe(String recipeKey, RecipeRequest recipeRequest,String UserName);
}
