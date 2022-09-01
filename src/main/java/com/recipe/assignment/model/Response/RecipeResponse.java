package com.recipe.assignment.model.Response;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecipeResponse {

    private String recipeKey;

    private String ownerId;

    private String name;

    private String instructions;

    private int servings;

    private Boolean vegetarian;

    private List<IngredientResponse> ingredient;

}
