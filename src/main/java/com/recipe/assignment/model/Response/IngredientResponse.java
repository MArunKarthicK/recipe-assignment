package com.recipe.assignment.model.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IngredientResponse {

    private String name;

    private Boolean vegetarian;
}
