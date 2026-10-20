package com.example.recipecomposeapp.data.dto

import com.example.recipecomposeapp.features.categories.presentation.model.toUiModel
import org.junit.Assert.assertEquals
import org.junit.Test

class CategoryDtoTest {

    @Test
    fun `converts DTO to UI model`() {
        // Arrange
        val dto = CategoryDto(
            id = 1,
            title = "Завтраки",
            description = "Утренние блюда",
            imageUrl = "breakfast.jpg"
        )

        // Act
        val result = dto.toUiModel()

        // Assert
        assertEquals("Завтраки", result.title)
    }
}
