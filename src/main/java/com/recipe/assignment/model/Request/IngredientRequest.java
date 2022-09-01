package com.recipe.assignment.model.Request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
@Builder
public class IngredientRequest {

    @NotNull
    private String name;

    @NotNull
    private Boolean vegetarian;
}
