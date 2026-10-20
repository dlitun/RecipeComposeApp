package com.example.recipecomposeapp.data.dto

import com.example.recipecomposeapp.core.utils.Constants
import com.example.recipecomposeapp.features.recipes.presentation.model.IngredientUiModel
import com.example.recipecomposeapp.features.recipes.presentation.model.toUiModel
import com.example.recipecomposeapp.fixtures.RecipeTestFixtures
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class RecipeDtoMapperTest {

    @Test
    fun `maps DTO to UI model correctly`() {
        val dto = RecipeTestFixtures.createRecipeDto(
            id = 42,
            title = "Pasta Carbonara",
            ingredients = listOf(
                RecipeTestFixtures.createIngredientDto(
                    quantity = "200",
                    unitOfMeasure = "г",
                    description = "Паста"
                )
            ),
            method = listOf("Отварить пасту", "Смешать ингредиенты"),
            imageUrl = "pasta.jpg"
        )

        val result = dto.toUiModel()

        assertEquals(42, result.id)
        assertEquals("Pasta Carbonara", result.title)
        assertEquals(Constants.IMAGES_BASE_URL + "pasta.jpg", result.imageUrl)
        assertEquals(
            listOf(IngredientUiModel(name = "Паста", quantity = "200", unitOfMeasure = "г")),
            result.ingredients
        )
        assertEquals(listOf("Отварить пасту", "Смешать ингредиенты"), result.method)
        assertFalse(result.isFavorite)
    }

    @Test
    fun `prepends base url to relative imageUrl`() {
        val dto = RecipeTestFixtures.createRecipeDto(imageUrl = "pasta.jpg")

        val result = dto.toUiModel()

        assertEquals(Constants.IMAGES_BASE_URL + "pasta.jpg", result.imageUrl)
    }

    @Test
    fun `preserves full imageUrl starting with http`() {
        val fullImageUrl = "https://example.com/images/pasta.jpg"
        val dto = RecipeTestFixtures.createRecipeDto(imageUrl = fullImageUrl)

        val result = dto.toUiModel()

        assertEquals(fullImageUrl, result.imageUrl)
    }
}
