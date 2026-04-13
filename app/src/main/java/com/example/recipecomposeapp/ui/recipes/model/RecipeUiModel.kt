package com.example.recipecomposeapp.ui.recipes.model

import androidx.compose.runtime.Immutable
import com.example.recipecomposeapp.core.Constants
import com.example.recipecomposeapp.data.dto.RecipeDto

@Immutable
data class RecipeUiModel(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val ingredients: List<IngredientUiModel>,
    val method: List<String>,
    val isFavorite: Boolean
)

fun RecipeDto.toUiModel(): RecipeUiModel {
    return RecipeUiModel(
        id = id,
        title = title,
        imageUrl = if (imageUrl.startsWith("http")) imageUrl else Constants.ASSETS_URI_PREFIX + imageUrl,
        ingredients = ingredients.map { ingredientDto -> ingredientDto.toUiModel() },
        method = method,
        isFavorite = false
    )
}