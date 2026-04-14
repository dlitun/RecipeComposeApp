package com.example.recipecomposeapp.ui.recipes.model

import androidx.compose.runtime.Immutable
import com.example.recipecomposeapp.data.dto.IngredientDto

@Immutable
data class IngredientUiModel(
    val name: String,
    val quantity: String,
    val unitOfMeasure: String
)

fun IngredientDto.toUiModel(): IngredientUiModel {
    return IngredientUiModel(
        name = description,
        quantity = quantity,
        unitOfMeasure = unitOfMeasure
    )
}