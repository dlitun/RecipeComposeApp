package com.example.recipecomposeapp.features.categories.presentation

import app.cash.turbine.test
import com.example.recipecomposeapp.data.dto.CategoryDto
import com.example.recipecomposeapp.data.repository.RecipesRepository
import com.example.recipecomposeapp.fixtures.CategoryTestFixtures
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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CategoriesViewModelTest {

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
    fun `loads categories from repository`() = runTest {
        val categories = CategoryTestFixtures.createCategoryDtoList()
        every { repository.getCategories() } returns flowOf(categories)

        val viewModel = CategoriesViewModel(repository)

        viewModel.uiState.test {
            val state = awaitItem()

            assertEquals(categories.size, state.categories.size)
            assertFalse(state.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `shows empty list when repository returns no data`() = runTest {
        every { repository.getCategories() } returns flowOf(emptyList())

        val viewModel = CategoriesViewModel(repository)

        viewModel.uiState.test {
            val state = awaitItem()

            assertTrue(state.categories.isEmpty())
            assertNull(state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `shows error when repository throws`() = runTest {
        val errorMessage = "Unable to load categories"
        val errorFlow: Flow<List<CategoryDto>> = flow {
            throw IOException(errorMessage)
        }
        every { repository.getCategories() } returns errorFlow

        val viewModel = CategoriesViewModel(repository)

        viewModel.uiState.test {
            val state = awaitItem()

            assertFalse(state.isLoading)
            assertEquals(errorMessage, state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
