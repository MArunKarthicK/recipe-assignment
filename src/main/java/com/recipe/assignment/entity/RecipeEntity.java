package com.recipe.assignment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Recipe")
public class RecipeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String recipeKey;

    private String instructions;

    private Boolean vegetarian;

    private int serving;

    private String createdBy;

    @UpdateTimestamp
    private Date lastupdate;

    @OneToMany(mappedBy = "recipeEntity",cascade = CascadeType.ALL)
    private List<RecipeIngredientRelationMapEntity> recipeIngredientRelationMapEntities;

    public static String _RECIPE_INGREDIENT_RELATION_MAP_ENTITIES = "recipeIngredientRelationMapEntities";

}
