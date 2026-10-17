package com.example.recipecomposeapp.data.dto

import com.example.recipecomposeapp.data.database.converter.Converters
import com.example.recipecomposeapp.data.database.entity.RecipeEntity
import kotlinx.serialization.Serializable

@Serializable
data class RecipeDto(
    val id: Int,
    val title: String,
    val ingredients: List<IngredientDto>,
    val method: List<String>,
    val imageUrl: String
)

private const val INGREDIENT_PARTS_SEPARATOR = "|"
private val converters = Converters()

fun RecipeDto.toEntity(categoryId: Int): RecipeEntity = RecipeEntity(
    id = id,
    title = title,
    categoryId = categoryId,
    imageUrl = imageUrl,
    ingredients = converters.fromList(
        ingredients.map { ingredient ->
            listOf(
                ingredient.quantity,
                ingredient.unitOfMeasure,
                ingredient.description
            ).joinToString(INGREDIENT_PARTS_SEPARATOR)
        }
    ),
    method = converters.fromList(method)
)

fun RecipeEntity.toDto(): RecipeDto = RecipeDto(
    id = id,
    title = title,
    ingredients = converters.fromString(ingredients).map { ingredient ->
        val parts = ingredient.split(INGREDIENT_PARTS_SEPARATOR, limit = 3)
        IngredientDto(
            quantity = parts.getOrElse(0) { "" },
            unitOfMeasure = parts.getOrElse(1) { "" },
            description = parts.getOrElse(2) { "" }
        )
    },
    method = converters.fromString(method),
    imageUrl = imageUrl
)
