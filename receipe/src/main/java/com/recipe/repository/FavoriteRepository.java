package com.recipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.recipe.model.Favorite;
import com.recipe.model.Recipe;
import com.recipe.model.User;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByUserAndRecipe(User user, Recipe recipe);
    List<Favorite> findByUser(User user);
}
