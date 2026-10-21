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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
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

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, RecipesDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        apiService = TestRecipesApiService()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun savesDataToCacheAfterSuccessfulApiCall() = runTest {
        val repository = RecipesRepositoryImpl(
            apiService = apiService,
            database = database,
            refreshScope = this
        )
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
    @OptIn(ExperimentalCoroutinesApi::class)
    fun returnsCachedDataWhenApiFails() = runTest {
        val initialCache = listOf(
            CategoryEntity(
                id = 2,
                name = "Супы",
                description = "Горячие блюда",
                imageUrl = "soups.jpg"
            )
        )
        database.categoryDao().insertCategories(initialCache)
        val repository = RecipesRepositoryImpl(
            apiService = apiService,
            database = database,
            refreshScope = this
        )

        repository.getCategories().test {
            val cached = awaitItem()
            assertEquals(1, cached.size)
            assertEquals("Супы", cached.single().title)

            apiService.categoriesResult.complete(Result.failure(IOException("Network unavailable")))
            advanceUntilIdle()

            val cacheAfterFailedRefresh = database.categoryDao().getAllCategories().first()
            assertEquals(initialCache, cacheAfterFailedRefresh)
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
