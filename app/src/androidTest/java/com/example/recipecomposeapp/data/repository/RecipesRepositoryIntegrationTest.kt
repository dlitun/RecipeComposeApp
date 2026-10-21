package com.example.recipecomposeapp.data.repository

import android.content.Context
import androidx.room3.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.recipecomposeapp.core.network.api.RecipesApiService
import com.example.recipecomposeapp.data.database.RecipesDatabase
import com.example.recipecomposeapp.data.database.entity.CategoryEntity
import com.example.recipecomposeapp.data.dto.CategoryDto
import com.example.recipecomposeapp.data.dto.RecipeDto
import java.io.IOException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipesRepositoryIntegrationTest {

    private lateinit var database: RecipesDatabase
    private lateinit var apiService: TestRecipesApiService
    private lateinit var repository: RecipesRepositoryImpl

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, RecipesDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        apiService = TestRecipesApiService()
        repository = RecipesRepositoryImpl(apiService = apiService, database = database)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun savesDataToCacheAfterSuccessfulApiCall() = runTest {
        val apiCategories = listOf(
            CategoryDto(
                id = 1,
                title = "Завтраки",
                description = "Лёгкие блюда",
                imageUrl = "breakfast.jpg"
            )
        )

        repository.getCategories().test {
            assertTrue(awaitItem().isEmpty())
            apiService.categoriesResult.complete(Result.success(apiCategories))

            val loaded = awaitItem()
            assertEquals("Завтраки", loaded.single().title)
            cancelAndIgnoreRemainingEvents()
        }

        val cached = database.categoryDao().getAllCategories().first()
        assertEquals(1, cached.size)
        assertEquals("Завтраки", cached.single().name)
    }

    @Test
    fun returnsCachedDataWhenApiFails() = runTest {
        database.categoryDao().insertCategories(
            listOf(
                CategoryEntity(
                    id = 2,
                    name = "Супы",
                    description = "Горячие блюда",
                    imageUrl = "soups.jpg"
                )
            )
        )
        apiService.categoriesResult.complete(Result.failure(IOException("Network unavailable")))

        repository.getCategories().test {
            val cached = awaitItem()

            assertEquals(1, cached.size)
            assertEquals("Супы", cached.single().title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private class TestRecipesApiService : RecipesApiService {
        val categoriesResult = CompletableDeferred<Result<List<CategoryDto>>>()

        override suspend fun getCategories(): List<CategoryDto> =
            categoriesResult.await().getOrThrow()

        override suspend fun getRecipesByCategory(categoryId: Int): List<RecipeDto> =
            error("Not used in this test")

        override suspend fun getRecipe(recipeId: Int): RecipeDto =
            error("Not used in this test")
    }
}
