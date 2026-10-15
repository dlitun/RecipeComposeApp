package com.example.recipecomposeapp.data.repository

import com.example.recipecomposeapp.data.dto.CategoryDto
import com.example.recipecomposeapp.data.dto.RecipeDto

interface RecipesRepository {
    suspend fun getCategories(): List<CategoryDto>

    suspend fun getRecipesByCategory(categoryId: Int): List<RecipeDto>

    suspend fun getRecipe(recipeId: Int): RecipeDto
}
