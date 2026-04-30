package com.example.recipecomposeapp.ui.details

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipecomposeapp.core.ui.theme.RecipesAppTheme
import com.example.recipecomposeapp.ui.recipes.model.IngredientUiModel
import com.example.recipecomposeapp.ui.recipes.model.RecipeUiModel

@Composable
fun RecipeDetailsScreen(
    recipe: RecipeUiModel,
    modifier: Modifier = Modifier
) {
    Text(
        text = recipe.title,
        style = MaterialTheme.typography.headlineMedium,
        modifier = modifier.padding(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun RecipeDetailsScreenPreview() {
    RecipesAppTheme {
        RecipeDetailsScreen(
            recipe = RecipeUiModel(
                id = 1,
                title = "Классический бургер",
                imageUrl = "",
                ingredients = listOf(
                    IngredientUiModel(
                        name = "Фарш",
                        quantity = "500",
                        unitOfMeasure = "г"
                    )
                ),
                method = listOf("Сформировать котлеты", "Обжарить"),
                isFavorite = false
            )
        )
    }
}

