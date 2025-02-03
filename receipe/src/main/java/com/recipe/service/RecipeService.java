package com.recipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.recipe.model.Favorite;
import com.recipe.model.Recipe;
import com.recipe.model.Role;
import com.recipe.model.User;
import com.recipe.repository.FavoriteRepository;
import com.recipe.repository.RecipeRepository;
import com.recipe.repository.UserRepository;
import com.recipe.token.JwtUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    private static final String IMAGE_UPLOAD_DIR = "E:\\uploads";

    // 🟢 Create a new recipe
    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    // 🟢 Get all recipes with pagination
    public List<Recipe> getAllRecipes(int page, int size) {
        return recipeRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    // 🟢 Get a single recipe by ID
    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    // 🟢 Update an existing recipe
    public Recipe updateRecipe(Long id, Recipe updatedRecipe) {
        return recipeRepository.findById(id).map(recipe -> {
            recipe.setName(updatedRecipe.getName());
            recipe.setIngredients(updatedRecipe.getIngredients());
            recipe.setSteps(updatedRecipe.getSteps());
            recipe.setTags(updatedRecipe.getTags());
            return recipeRepository.save(recipe);
        }).orElseThrow(() -> new RuntimeException("Recipe not found!"));
    }

    // 🟢 Delete a recipe
    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    // 🟢 Filter recipes by ingredient or tag
    public List<Recipe> searchRecipes(String ingredient, String tag) {
        if (ingredient != null && tag != null) {
            return recipeRepository.findByIngredientsContainingAndTagsContaining(ingredient, tag);
        } else if (ingredient != null) {
            return recipeRepository.findByIngredientsContaining(ingredient);
        } else if (tag != null) {
            return recipeRepository.findByTagsContaining(tag);
        }
        return recipeRepository.findAll();
    }

    // 🟢 Upload an image for a recipe
    public String uploadRecipeImage(Long recipeId, MultipartFile file) throws IOException {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found!"));

        // Generate a unique file name
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File uploadDir = new File(IMAGE_UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Save the file
        File destinationFile = new File(IMAGE_UPLOAD_DIR + fileName);
        file.transferTo(destinationFile);

        // Set image URL and update recipe
        recipe.setImageUrl("/" + IMAGE_UPLOAD_DIR + fileName);
        recipeRepository.save(recipe);

        return recipe.getImageUrl();
    }
    
    
    //Favorite services
    
    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private JwtUtil jwtUtil;

    public String favoriteRecipe(Long recipeId, String token) {
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Ensure only users (not recipe creators) can favorite
        if (!user.getRoles().contains(Role.USER)) {
            throw new RuntimeException("Only users can favorite recipes");
        }

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        // Check if already favorited
        if (favoriteRepository.existsByUserAndRecipe(user, recipe)) {
            return "Recipe is already in favorites";
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setRecipe(recipe);
        favoriteRepository.save(favorite);

        return "Recipe added to favorites";
    }

    public List<Recipe> getUserFavorites(String token) {
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Favorite> favorites = favoriteRepository.findByUser(user);
        return favorites.stream()
                .map(Favorite::getRecipe)
                .collect(Collectors.toList());
    }
}
