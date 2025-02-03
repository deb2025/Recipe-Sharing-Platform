package com.recipe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.recipe.model.Recipe;
import com.recipe.service.RecipeService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    // 🟢 Create a new recipe
    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) {
        Recipe savedRecipe = recipeService.saveRecipe(recipe);
        return ResponseEntity.ok(savedRecipe);
    }

    // 🟢 Get all recipes with pagination
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Recipe> recipes = recipeService.getAllRecipes(page, size);
        return ResponseEntity.ok(recipes);
    }

    // 🟢 Get a single recipe by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getRecipeById(@PathVariable Long id) {
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        return recipe.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🟢 Update a recipe
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecipe(@PathVariable Long id, @RequestBody Recipe updatedRecipe) {
        try {
            Recipe recipe = recipeService.updateRecipe(id, updatedRecipe);
            return ResponseEntity.ok(recipe);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 🟢 Delete a recipe
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id) {
        try {
            recipeService.deleteRecipe(id);
            return ResponseEntity.ok("Recipe deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 🟢 Search recipes by ingredient and/or tag
    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipes(
            @RequestParam(required = false) String ingredient,
            @RequestParam(required = false) String tag) {
        List<Recipe> recipes = recipeService.searchRecipes(ingredient, tag);
        return ResponseEntity.ok(recipes);
    }

    // 🟢 Upload an image for a recipe
    @PostMapping("/{recipeId}/upload-image")
    public ResponseEntity<?> uploadRecipeImage(@PathVariable Long recipeId, @RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = recipeService.uploadRecipeImage(recipeId, file);
            return ResponseEntity.ok("Image uploaded successfully: " + imageUrl);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Image upload failed: " + e.getMessage());
        }
    }
    
    //Favorite API's
    

    // ✅ Favorite a recipe (Only Users)
    @PostMapping("/{recipeId}/favorite")
    public ResponseEntity<String> favoriteRecipe(@PathVariable Long recipeId, @RequestHeader("Authorization") String token) {
        String jwt = token.substring(7); // Remove "Bearer " from token
        String message = recipeService.favoriteRecipe(recipeId, jwt);
        return ResponseEntity.ok(message);
    }

    // ✅ Get all favorite recipes of a user
    @GetMapping("/favorites")
    public ResponseEntity<List<Recipe>> getUserFavorites(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7); // Remove "Bearer " from token
        List<Recipe> favoriteRecipes = recipeService.getUserFavorites(jwt);
        return ResponseEntity.ok(favoriteRecipes);
    }
}