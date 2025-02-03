package com.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.recipe.model.Recipe;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByIngredientsContaining(String ingredient);
    List<Recipe> findByTagsContaining(String tag);
    List<Recipe> findByIngredientsContainingAndTagsContaining(String ingredient, String tag);
}