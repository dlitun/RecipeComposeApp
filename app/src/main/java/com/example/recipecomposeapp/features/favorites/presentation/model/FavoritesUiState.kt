package com.example.recipecomposeapp.features.favorites.presentation.model

import com.example.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel

data class FavoritesUiState(
    val favoriteRecipes: List<RecipeUiModel> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
) {
    val isEmpty: Boolean
        get() = favoriteRecipes.isEmpty()
}
