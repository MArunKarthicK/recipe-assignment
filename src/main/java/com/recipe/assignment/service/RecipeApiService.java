package com.recipe.assignment.service;

import com.recipe.assignment.component.RecipeHelper;
import com.recipe.assignment.component.RecipeUtil;
import com.recipe.assignment.entity.IngredientEntity;
import com.recipe.assignment.entity.RecipeEntity;
import com.recipe.assignment.model.Request.RecipeFilterRequest;
import com.recipe.assignment.model.Request.RecipeRequest;
import com.recipe.assignment.model.Response.IngredientResponse;
import com.recipe.assignment.model.Response.RecipeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeApiService implements RecipeApi {

    private final RecipeHelper recipeHelper;

    private final RecipeUtil recipeUtil;


    @Override
    public RecipeResponse createNewRecipe(RecipeRequest recipeRequest,String userName) {
        RecipeEntity newRecipeEntity = recipeHelper.createRecipe(recipeRequest, userName);

        return buildRecipeResponseFromEntity(newRecipeEntity);
    }


    @Override
    public List<RecipeResponse> findAllRecipes(String sortBy, int pageNo, int pageSize) {
        log.info("Processing the request to find all recipes");
        List<RecipeEntity> recipeEntityList = recipeHelper.findAllRecipes(sortBy,pageNo,pageSize);

        return buildRecipeResponseListFromEntityList(recipeEntityList);
    }

    @Override
    public List<RecipeResponse> filterRecipes(RecipeFilterRequest recipeFilterRequest,String sortBy, int pageNo, int pageSize) {
        List<RecipeEntity> recipeEntityList = recipeHelper.filterRecipesBasedOnFilterRequest(recipeFilterRequest,sortBy,pageNo,pageSize);
      return buildRecipeResponseListFromEntityList(recipeEntityList);
    }

    @Override
    public void deleteRecipe(String encryptedRecipeKey, String userName) {
        recipeHelper.deleteRecipe(encryptedRecipeKey, userName);
    }

    @Override
    public RecipeResponse getRecipeByRecipeKey(String encryptedRecipeKey) {
        log.info("Processing the request to get recipe based on recipeKey:{}",encryptedRecipeKey);
        return buildRecipeResponseFromEntity(recipeHelper.getRecipeByRecipeKey(encryptedRecipeKey));
    }

    @Override
    public RecipeResponse updateRecipe(String recipeKey, RecipeRequest recipeRequest, String userName) {
        return buildRecipeResponseFromEntity(recipeHelper.updateRecipe(recipeRequest,recipeKey,userName));
    }

    private List<RecipeResponse> buildRecipeResponseListFromEntityList(List<RecipeEntity> recipeEntityList) {
        List<RecipeResponse> recipeResponseList = new ArrayList<>();
        recipeEntityList.forEach(recipeEntity -> recipeResponseList.add(buildRecipeResponseFromEntity(recipeEntity))
        );
        return recipeResponseList;
    }

    private RecipeResponse buildRecipeResponseFromEntity(RecipeEntity recipeEntity) {
        List<IngredientResponse> ingredientResponseList = new ArrayList<>();
        recipeEntity.getRecipeIngredientRelationMapEntities().forEach(recipeIngredientRelationMapEntity -> {
            IngredientEntity ingredientEntity = recipeIngredientRelationMapEntity.getIngredientEntity();
            ingredientResponseList.add(IngredientResponse.builder().name(ingredientEntity.getName()).vegetarian(ingredientEntity.getVegetarian()).build());
        });
        return RecipeResponse.builder().name(recipeEntity.getName()).recipeKey(recipeUtil.EncryptRecipeKey(recipeEntity.getRecipeKey())).instructions(recipeEntity.getInstructions()).servings(recipeEntity.getServing()).ownerId(recipeEntity.getCreatedBy()).vegetarian(recipeEntity.getVegetarian()).ingredient(ingredientResponseList).build();
    }

}
