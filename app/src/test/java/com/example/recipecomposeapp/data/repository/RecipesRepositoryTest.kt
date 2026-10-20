package com.example.recipecomposeapp.data.repository

import app.cash.turbine.test
import com.example.recipecomposeapp.core.network.api.RecipesApiService
import com.example.recipecomposeapp.data.database.RecipesDatabase
import com.example.recipecomposeapp.data.database.dao.CategoryDao
import com.example.recipecomposeapp.data.database.dao.RecipeDao
import com.example.recipecomposeapp.data.database.entity.CategoryEntity
import com.example.recipecomposeapp.data.database.entity.RecipeEntity
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import java.io.IOException
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RecipesRepositoryTest {

    private lateinit var apiService: RecipesApiService
    private lateinit var database: RecipesDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var recipeDao: RecipeDao
    private lateinit var repository: RecipesRepositoryImpl

    @Before
    fun setup() {
        apiService = mockk()
        database = mockk(relaxed = true)
        categoryDao = mockk()
        recipeDao = mockk()

        every { database.categoryDao() } returns categoryDao
        every { database.recipeDao() } returns recipeDao

        repository = RecipesRepositoryImpl(apiService, database)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getCategories emits categories from database`() = runTest {
        val databaseCategories = listOf(
            CategoryEntity(
                id = 1,
                name = "Завтраки",
                description = "Утренние блюда",
                imageUrl = "breakfast.jpg"
            )
        )
        every { categoryDao.getAllCategories() } returns flowOf(databaseCategories)
        coEvery { apiService.getCategories() } returns emptyList()
        coEvery { categoryDao.insertCategories(any()) } just Runs

        repository.getCategories().test {
            val categories = awaitItem()

            assertEquals(1, categories.size)
            assertEquals("Завтраки", categories[0].title)
            assertEquals("Утренние блюда", categories[0].description)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(timeout = 1_000) { apiService.getCategories() }
        coVerify(timeout = 1_000) { categoryDao.insertCategories(emptyList()) }
    }

    @Test
    fun `getCategories still emits data when api throws exception`() = runTest {
        val cachedCategory = CategoryEntity(
            id = 2,
            name = "Супы",
            description = "Горячие блюда",
            imageUrl = "soups.jpg"
        )
        every { categoryDao.getAllCategories() } returns flowOf(listOf(cachedCategory))
        coEvery { apiService.getCategories() } throws IOException("Network unavailable")

        repository.getCategories().test {
            val categories = awaitItem()

            assertEquals(1, categories.size)
            assertEquals(2, categories[0].id)
            assertEquals("Супы", categories[0].title)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(timeout = 1_000) { apiService.getCategories() }
    }

    @Test
    fun `getRecipesByCategory returns flow filtered by categoryId`() = runTest {
        val categoryId = 7
        val databaseRecipes = listOf(
            RecipeEntity(
                id = 10,
                title = "Паста",
                categoryId = categoryId,
                imageUrl = "pasta.jpg",
                ingredients = "[]",
                method = ""
            )
        )
        every { recipeDao.getRecipesByCategory(categoryId) } returns flowOf(databaseRecipes)
        coEvery { apiService.getRecipesByCategory(categoryId) } returns emptyList()
        coEvery { recipeDao.insertRecipes(any()) } just Runs

        repository.getRecipesByCategory(categoryId).test {
            val recipes = awaitItem()

            assertEquals(1, recipes.size)
            assertEquals(10, recipes[0].id)
            assertEquals("Паста", recipes[0].title)
            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 1) { recipeDao.getRecipesByCategory(categoryId) }
        coVerify(timeout = 1_000) { apiService.getRecipesByCategory(categoryId) }
        coVerify(timeout = 1_000) { recipeDao.insertRecipes(emptyList()) }
    }
}
