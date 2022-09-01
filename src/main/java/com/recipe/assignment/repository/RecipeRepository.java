package com.recipe.assignment.repository;

import com.recipe.assignment.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity,Long> {

    Optional<RecipeEntity> findByRecipeKey(String recipeKey);

    Optional<RecipeEntity> findByNameAndCreatedBy(String name, String createdBy);
}
