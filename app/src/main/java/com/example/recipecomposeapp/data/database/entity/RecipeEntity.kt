package com.example.recipecomposeapp.data.database.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val categoryId: Int,
    val imageUrl: String,
    val ingredients: List<String>,
    val method: List<String>
)
