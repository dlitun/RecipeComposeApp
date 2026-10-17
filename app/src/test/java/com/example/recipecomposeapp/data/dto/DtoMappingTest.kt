package com.example.recipecomposeapp.data.dto

import org.junit.Assert.assertEquals
import org.junit.Test

class DtoMappingTest {

    @Test
    fun categoryDtoConvertsToEntityAndBack() {
        val category = CategoryDto(
            id = 1,
            title = "Супы",
            description = "Горячие блюда",
            imageUrl = "soup.png"
        )

        assertEquals(category, category.toEntity().toDto())
    }

    @Test
    fun recipeDtoConvertsToEntityAndBack() {
        val recipe = RecipeDto(
            id = 10,
            title = "Томатный суп",
            ingredients = listOf(
                IngredientDto(
                    quantity = "2",
                    unitOfMeasure = "шт",
                    description = "помидоры"
                ),
                IngredientDto(
                    quantity = "по вкусу",
                    unitOfMeasure = "",
                    description = "соль"
                )
            ),
            method = listOf("Нарезать овощи", "Сварить суп"),
            imageUrl = "tomato-soup.png"
        )

        val entity = recipe.toEntity(categoryId = 5)

        assertEquals(5, entity.categoryId)
        assertEquals(recipe, entity.toDto())
    }

    @Test
    fun emptyRecipeListsConvertToEntityAndBack() {
        val recipe = RecipeDto(
            id = 11,
            title = "Пустой рецепт",
            ingredients = emptyList(),
            method = emptyList(),
            imageUrl = ""
        )

        assertEquals(recipe, recipe.toEntity(categoryId = 3).toDto())
    }
}
