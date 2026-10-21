package com.example.recipecomposeapp.features.recipes.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.recipecomposeapp.core.ui.theme.RecipesAppTheme
import com.example.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel
import com.example.recipecomposeapp.features.recipes.presentation.model.RecipesUiState
import org.junit.Rule
import org.junit.Test

class RecipesContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun showsLoadingState() {
        setRecipesContent(RecipesUiState(isLoading = true))

        composeTestRule
            .onNodeWithTag("loading_indicator")
            .assertIsDisplayed()
    }

    @Test
    fun showsErrorState() {
        setRecipesContent(RecipesUiState(error = "Network error"))

        composeTestRule
            .onNodeWithTag("error_message")
            .assertTextEquals("Network error")
    }

    @Test
    fun showsEmptyState() {
        setRecipesContent(RecipesUiState())

        composeTestRule
            .onNodeWithTag("empty_state")
            .assertIsDisplayed()
    }

    @Test
    fun displaysRecipeList() {
        val recipe = recipe(id = 3, title = "Томатный суп")

        setRecipesContent(RecipesUiState(recipes = listOf(recipe)))

        composeTestRule
            .onNodeWithText(recipe.title.uppercase())
            .assertIsDisplayed()
    }

    private fun setRecipesContent(uiState: RecipesUiState) {
        composeTestRule.setContent {
            RecipesAppTheme {
                RecipesContent(
                    uiState = uiState,
                    onRecipeClick = {}
                )
            }
        }
    }

    private fun recipe(id: Int, title: String) = RecipeUiModel(
        id = id,
        title = title,
        imageUrl = "",
        ingredients = emptyList(),
        method = emptyList(),
        isFavorite = false
    )
}
