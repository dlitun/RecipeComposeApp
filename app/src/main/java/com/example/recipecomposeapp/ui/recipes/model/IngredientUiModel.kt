package com.example.recipecomposeapp.ui.recipes.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.example.recipecomposeapp.data.dto.IngredientDto
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class IngredientUiModel(
    val name: String,
    val quantity: String,
    val unitOfMeasure: String
) : Parcelable

fun IngredientDto.toUiModel(): IngredientUiModel {
    return IngredientUiModel(
        name = description,
        quantity = quantity,
        unitOfMeasure = unitOfMeasure
    )
}