package com.recipe.assignment.repository;

import com.recipe.assignment.entity.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository extends JpaRepository<IngredientEntity,Long> {

    List<IngredientEntity> findByNameIgnoreCaseIn(List<String> nameList);
}
