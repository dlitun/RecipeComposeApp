package com.example.recipecomposeapp.data.repository

import android.util.Log
import com.example.recipecomposeapp.core.network.api.RecipesApiService
import com.example.recipecomposeapp.data.dto.CategoryDto
import com.example.recipecomposeapp.data.dto.RecipeDto
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipesRepositoryImpl(
    private val apiService: RecipesApiService
) : RecipesRepository {

    override suspend fun getCategories(): List<CategoryDto> = withContext(Dispatchers.IO) {
        try {
            apiService.getCategories()
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Log.e(TAG, "Не удалось загрузить категории", exception)
            emptyList()
        }
    }

    override suspend fun getRecipesByCategory(categoryId: Int): List<RecipeDto> =
        withContext(Dispatchers.IO) {
            try {
                apiService.getRecipesByCategory(categoryId)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                Log.e(TAG, "Не удалось загрузить рецепты категории $categoryId", exception)
                emptyList()
            }
        }

    override suspend fun getRecipe(recipeId: Int): RecipeDto = withContext(Dispatchers.IO) {
        try {
            apiService.getRecipe(recipeId)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Log.e(TAG, "Не удалось загрузить рецепт $recipeId", exception)
            throw exception
        }
    }

    private companion object {
        const val TAG = "RecipesRepository"
    }
}
