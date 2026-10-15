package com.example.recipecomposeapp.data.database.dao

import androidx.room3.Dao
import androidx.room3.Query
import com.example.recipecomposeapp.data.database.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<RecipeEntity>>
}
