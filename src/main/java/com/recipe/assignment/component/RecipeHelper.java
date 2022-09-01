package com.recipe.assignment.component;

import com.recipe.assignment.entity.IngredientEntity;
import com.recipe.assignment.entity.RecipeEntity;
import com.recipe.assignment.entity.RecipeIngredientRelationMapEntity;
import com.recipe.assignment.exception.IngredientNotFoundException;
import com.recipe.assignment.exception.NotAuthorizedException;
import com.recipe.assignment.exception.RecipeNotFoundException;
import com.recipe.assignment.model.Request.IngredientRequest;
import com.recipe.assignment.model.Request.RecipeFilterRequest;
import com.recipe.assignment.model.Request.RecipeRequest;
import com.recipe.assignment.model.enums.FoodType;
import com.recipe.assignment.repository.IngredientRepository;
import com.recipe.assignment.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecipeHelper {

    private final RecipeRepository recipeRepository;

    private final IngredientRepository ingredientRepository;

    private final RecipeUtil recipeUtil;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * to create new  recipe
     *
     * @param recipeRequest
     * @param userName
     * @return
     */
    public RecipeEntity createRecipe(RecipeRequest recipeRequest, String userName) {
        Optional<RecipeEntity> existingRecipeEntity = recipeRepository.findByNameAndCreatedBy(recipeRequest.getName(), userName);

        if (existingRecipeEntity.isPresent()) {
            log.info("retrieveRecipe User Not Match : [" + userName + "] [" + existingRecipeEntity.get().getCreatedBy() + "]");
            throw new RecipeNotFoundException("Recipe not found");
        }
        RecipeEntity newRecipeEntity = constructRecipeEntity(recipeRequest, userName);
        newRecipeEntity.setRecipeKey(recipeUtil.RecipeKeyGenerator(recipeRequest.getName()));

        recipeRepository.save(newRecipeEntity);
        return newRecipeEntity;
    }

    /**
     * to update recipe based on encryptedRecipeKey
     *
     * @param recipeRequest
     * @param encryptedRecipeKey
     * @param userName
     * @return
     */
    public RecipeEntity updateRecipe(RecipeRequest recipeRequest, String encryptedRecipeKey, String userName) {

        return recipeRepository.findByRecipeKey(recipeUtil.DecryptRecipeKey(encryptedRecipeKey)).map(recipeEntity -> {
            if (!recipeEntity.getCreatedBy().equalsIgnoreCase(userName)) {
                throw new NotAuthorizedException("not authorized");
            }
            RecipeEntity updatedRecipeEntity = updateRecipeWithId(recipeRequest, userName, recipeEntity);
            return updatedRecipeEntity;
        }).orElseThrow(() -> {
            log.error("No recipe found for encryptedRecipeKey:{} throw RecipeNotFoundException", encryptedRecipeKey);
            throw new RecipeNotFoundException("Invalid recipeKey");
        });
    }

    private RecipeEntity updateRecipeWithId(RecipeRequest recipeRequest, String userName, RecipeEntity recipeEntity) {

        RecipeEntity updateRecipeEntity = constructRecipeEntity(recipeRequest, userName);
        updateRecipeEntity.setId(recipeEntity.getId());
        updateRecipeEntity.setRecipeKey(recipeEntity.getRecipeKey());
        recipeRepository.deleteById(recipeEntity.getId());

        recipeRepository.save(updateRecipeEntity);
        return updateRecipeEntity;
    }


    private RecipeEntity constructRecipeEntity(RecipeRequest recipeRequest, String userName) {
        List<String> ingredientRequestList = recipeRequest.getIngredient().stream()
                .map(IngredientRequest::getName)
                .collect(Collectors.toList());

        List<IngredientEntity> ingredientEntityList = ingredientRepository.findByNameIgnoreCaseIn(ingredientRequestList);

        List<String> ingredientNotInDBList = ingredientRequestList.stream().filter(ingredientName -> !ingredientEntityList.stream().anyMatch(ingredientEntity -> ingredientEntity.getName().equalsIgnoreCase(ingredientName))).collect(Collectors.toList());

        if (!ingredientNotInDBList.isEmpty()) {
            throw new IngredientNotFoundException("Ingredients not in db, Ask admin to add the Ingredients:{}" + ingredientNotInDBList.toString());
        }

        return getRecipeEntity(recipeRequest, userName, ingredientEntityList);
    }

    private RecipeEntity getRecipeEntity(RecipeRequest recipeRequest, String userName, List<IngredientEntity> ingredientEntityList) {
        RecipeEntity newRecipeEntity = RecipeEntity.builder().name(recipeRequest.getName()).vegetarian(isVegetarianRecipe(ingredientEntityList)).createdBy(userName).instructions(recipeRequest.getInstructions()).serving(recipeRequest.getServings() > 0 ? recipeRequest.getServings() : 1).build();
        List<RecipeIngredientRelationMapEntity> ingredientRelationMapEntities = new ArrayList<>();
        ingredientEntityList.forEach(ingredientEntity -> {
            ingredientRelationMapEntities.add(RecipeIngredientRelationMapEntity.builder().ingredientEntity(ingredientEntity).recipeEntity(newRecipeEntity).build());
        });
        newRecipeEntity.setRecipeIngredientRelationMapEntities(ingredientRelationMapEntities);
        return newRecipeEntity;
    }

    /**
     * to find all recipes
     *
     * @param sortBy
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<RecipeEntity> findAllRecipes(String sortBy, int pageNo, int pageSize) {
        return this.filterRecipeBasedOnRequest(null, sortBy, pageNo, pageSize);
    }

    /**
     * filter recipes based on filter request
     *
     * @param recipeFilterRequest
     * @param sortBy
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<RecipeEntity> filterRecipesBasedOnFilterRequest(RecipeFilterRequest recipeFilterRequest, String sortBy, int pageNo, int pageSize) {
        return this.filterRecipeBasedOnRequest(recipeFilterRequest, sortBy, pageNo, pageSize);
    }

    /**
     * to delete recipe based on encryptedRecipeKey
     *
     * @param encryptedRecipeKey
     * @param userName
     */
    public void deleteRecipe(String encryptedRecipeKey, String userName) {
        recipeRepository.findByRecipeKey(recipeUtil.DecryptRecipeKey(encryptedRecipeKey)).ifPresentOrElse(recipeEntity -> {
            if (!recipeEntity.getCreatedBy().equalsIgnoreCase(userName)) {
                throw new NotAuthorizedException("not authorized");
            }
            recipeRepository.deleteById(recipeEntity.getId());
        }, () -> {
            log.error("No recipe found for encryptedRecipeKey:{} throw RecipeNotFoundException", encryptedRecipeKey);
            throw new RecipeNotFoundException("Invalid recipeKey");
        });
    }

    /**
     * to get recipe based on encryptedRecipeKey
     *
     * @param encryptedRecipeKey
     * @return
     */
    public RecipeEntity getRecipeByRecipeKey(String encryptedRecipeKey) {
        Optional<RecipeEntity> recipeEntityOptional = recipeRepository.findByRecipeKey(recipeUtil.DecryptRecipeKey(encryptedRecipeKey));

        if (recipeEntityOptional.isPresent()) {
            return recipeEntityOptional.get();
        } else {
            log.error("No recipe found for encryptedRecipeKey:{} throw RecipeNotFoundException", encryptedRecipeKey);
            throw new RecipeNotFoundException("Invalid recipeKey");
        }
    }

    private List<RecipeEntity> filterRecipeBasedOnRequest(RecipeFilterRequest recipeFilterRequest, String sortBy, int pageNo, int pageSize) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RecipeEntity> cbQuery = cb.createQuery(RecipeEntity.class);

        Root<RecipeEntity> recipeEntityRoot = cbQuery.from(RecipeEntity.class);
        Join<RecipeEntity, RecipeIngredientRelationMapEntity> recipeRelationMapJoin = recipeEntityRoot.join(RecipeEntity._RECIPE_INGREDIENT_RELATION_MAP_ENTITIES, JoinType.INNER);
        Join<RecipeIngredientRelationMapEntity, IngredientEntity> relationMapIngredientJoin = recipeRelationMapJoin.join(RecipeIngredientRelationMapEntity._INGREDIENT_ENTITY, JoinType.INNER);
        cbQuery.distinct(true);
        cbQuery.orderBy(cb.asc(recipeEntityRoot.get(sortBy)));

        if (Objects.isNull(recipeFilterRequest)) {
            return entityManager.createQuery(cbQuery).setFirstResult(pageNo * pageSize).setMaxResults(pageSize).getResultList();
        }

        List<Predicate> predicates = getPredicates(recipeFilterRequest, cb, recipeEntityRoot, relationMapIngredientJoin, cbQuery);

        if (!predicates.isEmpty())
            cbQuery.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        List<RecipeEntity> resultList = entityManager.createQuery(cbQuery).setFirstResult(pageNo * pageSize).setMaxResults(pageSize).getResultList();

        return resultList;
    }

    private List<Predicate> getPredicates(RecipeFilterRequest recipeFilterRequest, CriteriaBuilder cb, Root<RecipeEntity> recipeEntityRoot, Join<RecipeIngredientRelationMapEntity, IngredientEntity> relationMapIngredientJoin, CriteriaQuery<RecipeEntity> cbQuery) {

        List<Predicate> predicates = new ArrayList<>();

        if (!Objects.isNull(recipeFilterRequest.getFoodType()) && !recipeFilterRequest.getFoodType().equals(FoodType.ALL))
            predicates.add(cb.equal(recipeEntityRoot.get("vegetarian"), recipeFilterRequest.getFoodType().equals(FoodType.VEGETARIAN)));

        if (recipeFilterRequest.getServings() != 0)
            predicates.add(cb.equal(recipeEntityRoot.get("serving"), recipeFilterRequest.getServings()));
        
        if (recipeFilterRequest.getInstructionSearchText() != null)
            predicates.add(cb.like(recipeEntityRoot.get("instructions"), "%" + recipeFilterRequest.getInstructionSearchText() + "%"));

        if (recipeFilterRequest.getIncludeIngredients() != null && !recipeFilterRequest.getIncludeIngredients().isEmpty()) {
            CriteriaBuilder.In<String> inClause = cb.in(relationMapIngredientJoin.get("name"));
            for (String ingredient : recipeFilterRequest.getIncludeIngredients()) {
                inClause.value(ingredient);
            }
            predicates.add(inClause);
        }

        if (recipeFilterRequest.getExcludeIngredients() != null && !recipeFilterRequest.getExcludeIngredients().isEmpty()) {
            Subquery<RecipeEntity> entitySubQuery = getRecipeEntityQuery(recipeFilterRequest, cb, cbQuery);
            predicates.add(cb.not(cb.in(recipeEntityRoot.get("id")).value(entitySubQuery)));
        }

        return predicates;
    }

    private Subquery<RecipeEntity> getRecipeEntityQuery(RecipeFilterRequest recipeFilterRequest, CriteriaBuilder cb, CriteriaQuery<RecipeEntity> cbQuery) {
        Subquery<RecipeEntity> subquery = cbQuery.subquery(RecipeEntity.class);
        Root<RecipeEntity> recipeSubQueryRoot = subquery.from(RecipeEntity.class);
        Join<RecipeEntity, RecipeIngredientRelationMapEntity> recipeRelationMapSubJoin = recipeSubQueryRoot.join(RecipeEntity._RECIPE_INGREDIENT_RELATION_MAP_ENTITIES, JoinType.INNER);
        Join<RecipeIngredientRelationMapEntity, IngredientEntity> relationMapIngredientSubJoin = recipeRelationMapSubJoin.join(RecipeIngredientRelationMapEntity._INGREDIENT_ENTITY, JoinType.INNER);
        CriteriaBuilder.In<String> incClause = cb.in(relationMapIngredientSubJoin.get("name"));
        for (String ingredient : recipeFilterRequest.getExcludeIngredients()) {
            incClause.value(ingredient);
        }
        subquery.select(recipeSubQueryRoot)
                .distinct(true)
                .where(incClause);
        return subquery;
    }

    private Boolean isVegetarianRecipe(List<IngredientEntity> ingredientEntityList) {
        return ingredientEntityList.stream().filter(ingredientEntity -> !ingredientEntity.getVegetarian()).collect(Collectors.toList()).isEmpty();
    }
}
