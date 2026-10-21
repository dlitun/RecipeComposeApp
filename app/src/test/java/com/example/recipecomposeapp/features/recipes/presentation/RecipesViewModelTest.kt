package com.example.recipecomposeapp.features.recipes.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.recipecomposeapp.core.utils.Constants
import com.example.recipecomposeapp.data.dto.RecipeDto
import com.example.recipecomposeapp.data.repository.RecipesRepository
import com.example.recipecomposeapp.fixtures.RecipeTestFixtures
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipesViewModelTest {

    private lateinit var repository: RecipesRepository

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `loads recipes for category`() = runTest {
        val recipes = RecipeTestFixtures.createRecipeDtoList()
        every { repository.getRecipesByCategory(1) } returns flowOf(recipes)

        val viewModel = createViewModel(categoryId = 1)

        viewModel.uiState.test {
            val state = awaitItem()

            assertEquals(recipes.size, state.recipes.size)
            assertFalse(state.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `state reflects category title from savedState`() = runTest {
        val categoryTitle = "Завтраки"
        every { repository.getRecipesByCategory(1) } returns flowOf(emptyList())

        val viewModel = createViewModel(
            categoryId = 1,
            categoryTitle = categoryTitle
        )

        viewModel.uiState.test {
            val state = awaitItem()

            assertEquals(categoryTitle, state.categoryTitle)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `shows error when repository throws`() = runTest {
        val errorMessage = "Unable to load recipes"
        val errorFlow: Flow<List<RecipeDto>> = flow {
            throw IOException(errorMessage)
        }
        every { repository.getRecipesByCategory(1) } returns errorFlow

        val viewModel = createViewModel(categoryId = 1)

        viewModel.uiState.test {
            val state = awaitItem()

            assertFalse(state.isLoading)
            assertEquals(errorMessage, state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createViewModel(
        categoryId: Int = 1,
        categoryTitle: String = "Категория",
        categoryImageUrl: String = "category.jpg"
    ): RecipesViewModel {
        val savedStateHandle = SavedStateHandle(
            mapOf(
                Constants.CATEGORY_ID to categoryId,
                Constants.CATEGORY_TITLE to categoryTitle,
                Constants.CATEGORY_IMAGE_URL to categoryImageUrl
            )
        )
        return RecipesViewModel(savedStateHandle, repository)
    }
}
