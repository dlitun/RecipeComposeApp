package com.example.recipecomposeapp.data.dto

import com.example.recipecomposeapp.data.database.entity.CategoryEntity
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String
)

fun CategoryDto.toEntity(): CategoryEntity = CategoryEntity(
    id = id,
    name = title,
    description = description,
    imageUrl = imageUrl
)

fun CategoryEntity.toDto(): CategoryDto = CategoryDto(
    id = id,
    title = name,
    description = description,
    imageUrl = imageUrl
)
