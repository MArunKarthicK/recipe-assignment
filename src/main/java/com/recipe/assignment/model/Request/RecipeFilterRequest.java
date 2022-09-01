package com.recipe.assignment.model.Request;

import com.recipe.assignment.model.enums.FoodType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class RecipeFilterRequest {

    private FoodType foodType;

    private int servings;

    private List<String> includeIngredients;

    private List<String> excludeIngredients;

    private String instructionSearchText;
}



