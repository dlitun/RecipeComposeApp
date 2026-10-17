package com.example.recipecomposeapp.data.repository

import com.example.recipecomposeapp.data.dto.CategoryDto
import com.example.recipecomposeapp.data.dto.RecipeDto
import kotlinx.coroutines.flow.Flow

interface RecipesRepository {
    fun getCategories(): Flow<List<CategoryDto>>

    fun getRecipesByCategory(categoryId: Int): Flow<List<RecipeDto>>

    fun getRecipesByIds(ids: List<Int>): Flow<List<RecipeDto>>

    fun getRecipe(recipeId: Int): Flow<RecipeDto?>
}
