package com.recipe.assignment.model.Request;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
public class RecipeRequest {

    @NotNull
    private String name;

    @NotNull
    private String instructions;

    @NotNull
    private int servings;

    @NotNull
    private List<IngredientRequest> ingredient;

}
