package com.example.recipecomposeapp.features.categories.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.recipecomposeapp.core.ui.theme.RecipesAppTheme
import com.example.recipecomposeapp.features.categories.presentation.model.CategoriesUiState
import com.example.recipecomposeapp.features.categories.presentation.model.CategoryUiModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class CategoriesContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysCategories() {
        val category = category(id = 1, title = "Завтраки")

        composeTestRule.setContent {
            RecipesAppTheme {
                CategoriesContent(
                    uiState = CategoriesUiState(categories = listOf(category)),
                    onCategoryClick = { _, _, _ -> }
                )
            }
        }

        composeTestRule
            .onNodeWithText(category.title.uppercase())
            .assertIsDisplayed()
    }

    @Test
    fun clickingCategoryNavigatesToRecipes() {
        val category = category(id = 7, title = "Супы")
        var clickedId: Int? = null

        composeTestRule.setContent {
            RecipesAppTheme {
                CategoriesContent(
                    uiState = CategoriesUiState(categories = listOf(category)),
                    onCategoryClick = { id, _, _ -> clickedId = id }
                )
            }
        }

        composeTestRule
            .onNodeWithText(category.title.uppercase())
            .performClick()

        assertEquals(category.id, clickedId)
    }

    @Test
    fun showsLoadingState() {
        composeTestRule.setContent {
            RecipesAppTheme {
                CategoriesContent(
                    uiState = CategoriesUiState(isLoading = true),
                    onCategoryClick = { _, _, _ -> }
                )
            }
        }

        composeTestRule
            .onNodeWithTag("loading_indicator")
            .assertIsDisplayed()
    }

    private fun category(id: Int, title: String) = CategoryUiModel(
        id = id,
        title = title,
        description = "Описание категории",
        imageUrl = ""
    )
}
