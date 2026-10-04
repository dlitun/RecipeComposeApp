package com.example.recipecomposeapp.features.details.presentation.model

import com.example.recipecomposeapp.features.recipes.presentation.model.IngredientUiModel
import com.example.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel
import java.math.BigDecimal
import java.math.RoundingMode

const val DEFAULT_PORTIONS = 3
const val MIN_PORTIONS = 1
const val MAX_PORTIONS = 12

data class RecipeDetailsUiState(
    val recipe: RecipeUiModel? = null,
    val portions: Int = DEFAULT_PORTIONS,
    val isLoading: Boolean = true,
    val error: String? = null
) {
    val scaledIngredients: List<IngredientUiModel>
        get() = recipe?.ingredients.orEmpty().map { ingredient ->
            ingredient.scale(portions = portions, basePortions = DEFAULT_PORTIONS)
        }
}

private fun IngredientUiModel.scale(portions: Int, basePortions: Int): IngredientUiModel {
    val baseAmount = quantity.replace(',', '.').toDoubleOrNull() ?: return this
    val scaledAmount = baseAmount * portions / basePortions
    return copy(quantity = scaledAmount.toDisplayString())
}

private fun Double.toDisplayString(): String = BigDecimal.valueOf(this)
    .setScale(2, RoundingMode.HALF_UP)
    .stripTrailingZeros()
    .toPlainString()
    .replace('.', ',')
