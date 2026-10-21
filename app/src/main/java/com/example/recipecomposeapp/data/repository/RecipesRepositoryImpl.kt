package com.example.recipecomposeapp.data.repository

import android.util.Log
import com.example.recipecomposeapp.core.network.api.RecipesApiService
import com.example.recipecomposeapp.data.database.RecipesDatabase
import com.example.recipecomposeapp.data.dto.CategoryDto
import com.example.recipecomposeapp.data.dto.RecipeDto
import com.example.recipecomposeapp.data.dto.toDto
import com.example.recipecomposeapp.data.dto.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecipesRepositoryImpl internal constructor(
    private val apiService: RecipesApiService,
    database: RecipesDatabase,
    private val refreshScope: CoroutineScope
) : RecipesRepository {
    private val categoryDao = database.categoryDao()
    private val recipeDao = database.recipeDao()

    @Inject
    constructor(
        apiService: RecipesApiService,
        database: RecipesDatabase
    ) : this(
        apiService = apiService,
        database = database,
        refreshScope = CoroutineScope(Dispatchers.IO)
    )

    override fun getCategories(): Flow<List<CategoryDto>> {
        refreshScope.launch {
            try {
                val categories = apiService.getCategories()
                categoryDao.insertCategories(categories.map { category -> category.toEntity() })
            } catch (exception: Exception) {
                Log.e(TAG, "Не удалось обновить категории", exception)
            }
        }

        return categoryDao.getAllCategories().map { categories ->
            categories.map { category -> category.toDto() }
        }
    }

    override fun getRecipesByCategory(categoryId: Int): Flow<List<RecipeDto>> {
        refreshScope.launch {
            try {
                val recipes = apiService.getRecipesByCategory(categoryId)
                recipeDao.insertRecipes(
                    recipes.map { recipe -> recipe.toEntity(categoryId) }
                )
            } catch (exception: Exception) {
                Log.e(TAG, "Не удалось обновить рецепты категории $categoryId", exception)
            }
        }

        return recipeDao.getRecipesByCategory(categoryId).map { recipes ->
            recipes.map { recipe -> recipe.toDto() }
        }
    }

    override fun getRecipesByIds(ids: List<Int>): Flow<List<RecipeDto>> =
        recipeDao.getRecipesByIds(ids).map { recipes ->
            recipes.map { recipe -> recipe.toDto() }
        }

    override fun getRecipe(recipeId: Int): Flow<RecipeDto?> = flow {
        emit(apiService.getRecipe(recipeId))
    }

    private companion object {
        const val TAG = "RecipesRepository"
    }
}
