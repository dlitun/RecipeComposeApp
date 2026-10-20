package com.example.recipecomposeapp.data.dto

import com.example.recipecomposeapp.features.categories.presentation.model.toUiModel
import com.example.recipecomposeapp.fixtures.CategoryTestFixtures
import org.junit.Assert.assertEquals
import org.junit.Test

class CategoryDtoTest {

    @Test
    fun `mapper maps empty title correctly`() {
        val dto = CategoryTestFixtures.createCategoryDto(title = "")

        val result = dto.toUiModel()

        assertEquals("", result.title)
    }

    @Test
    fun `mapper preserves very long description`() {
        val longDescription = "Очень длинное описание. ".repeat(500)
        val dto = CategoryTestFixtures.createCategoryDto(description = longDescription)

        val result = dto.toUiModel()

        assertEquals(longDescription, result.description)
    }
}
