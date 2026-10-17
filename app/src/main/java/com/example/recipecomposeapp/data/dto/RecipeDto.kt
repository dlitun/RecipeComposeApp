package com.example.recipecomposeapp.data.dto

import com.example.recipecomposeapp.data.database.converter.Converters
import com.example.recipecomposeapp.data.database.entity.RecipeEntity
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class RecipeDto(
    val id: Int,
    val title: String,
    val ingredients: List<IngredientDto>,
    val method: List<String>,
    val imageUrl: String
)

private val converters = Converters()

fun RecipeDto.toEntity(categoryId: Int): RecipeEntity = RecipeEntity(
    id = id,
    title = title,
    categoryId = categoryId,
    imageUrl = imageUrl,
    ingredients = Json.encodeToString(ingredients),
    method = converters.fromList(method)
)

fun RecipeEntity.toDto(): RecipeDto = RecipeDto(
    id = id,
    title = title,
    ingredients = if (ingredients.isEmpty()) {
        emptyList()
    } else {
        Json.decodeFromString(ingredients)
    },
    method = converters.fromString(method),
    imageUrl = imageUrl
)
