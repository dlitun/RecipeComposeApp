package com.example.recipecomposeapp.features.details.presentation.model

import com.example.recipecomposeapp.features.recipes.presentation.model.IngredientUiModel
import com.example.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel
import org.junit.Assert.assertEquals
import org.junit.Test

class RecipeDetailsUiStateTest {

    @Test
    fun scaledIngredients_recalculatesNumericQuantitiesAndKeepsTextQuantities() {
        val recipe = RecipeUiModel(
            id = 1,
            title = "Recipe",
            imageUrl = "",
            ingredients = listOf(
                IngredientUiModel(name = "Flour", quantity = "1,5", unitOfMeasure = "kg"),
                IngredientUiModel(name = "Salt", quantity = "to taste", unitOfMeasure = "")
            ),
            method = emptyList(),
            isFavorite = false
        )

        val state = RecipeDetailsUiState(
            recipe = recipe,
            portions = 6,
            isLoading = false
        )

        assertEquals("3", state.scaledIngredients[0].quantity)
        assertEquals("to taste", state.scaledIngredients[1].quantity)
    }
}
