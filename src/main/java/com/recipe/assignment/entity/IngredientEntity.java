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
@Table(name="INGREDIENT")
public class IngredientEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Boolean vegetarian;

    private String addedBy;

    @UpdateTimestamp
    private Date lastupdate;

    @OneToMany(mappedBy = "ingredientEntity")
    private List<RecipeIngredientRelationMapEntity> recipeIngredientRelationMapEntities;

}
