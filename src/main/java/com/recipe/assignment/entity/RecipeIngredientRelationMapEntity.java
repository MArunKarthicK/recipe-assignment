package com.recipe.assignment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="RecipeIngredientRelationMap")
public class RecipeIngredientRelationMapEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="recipe_id")
    private RecipeEntity recipeEntity;

    @ManyToOne
    @JoinColumn(name="ingredient_id")
    private IngredientEntity ingredientEntity;

    @UpdateTimestamp
    private Date lastUpdate;

    public static String _INGREDIENT_ENTITY = "ingredientEntity";
}
