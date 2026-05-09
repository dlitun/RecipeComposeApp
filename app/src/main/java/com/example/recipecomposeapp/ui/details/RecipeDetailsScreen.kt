package com.example.recipecomposeapp.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.recipecomposeapp.R
import com.example.recipecomposeapp.core.ui.ScreenHeader
import com.example.recipecomposeapp.core.ui.theme.Dimens
import com.example.recipecomposeapp.core.ui.theme.RecipesAppTheme
import com.example.recipecomposeapp.ui.recipes.model.IngredientUiModel
import com.example.recipecomposeapp.ui.recipes.model.RecipeUiModel
import kotlin.math.abs
import kotlin.math.roundToInt

private const val DEFAULT_PORTIONS = 3
private const val MIN_PORTIONS = 1f
private const val MAX_PORTIONS = 12f

@Composable
fun RecipeDetailsScreen(
    recipe: RecipeUiModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val headerPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(recipe.imageUrl)
            .crossfade(true)
            .build(),
        placeholder = painterResource(id = R.drawable.placeholder_header),
        error = painterResource(id = R.drawable.placeholder_header)
    )

    var currentPortions by remember { mutableStateOf(DEFAULT_PORTIONS) }
    val scaledIngredients = remember(recipe.ingredients, currentPortions) {
        scaleIngredients(
            ingredients = recipe.ingredients,
            currentPortions = currentPortions,
            basePortions = DEFAULT_PORTIONS
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ScreenHeader(
            painter = headerPainter,
            contentDescription = recipe.title,
            text = recipe.title
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Space16),
            verticalArrangement = Arrangement.spacedBy(Dimens.Space16)
        ) {
            Text(
                text = "Ингредиенты",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Порции: $currentPortions",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Slider(
                value = currentPortions.toFloat(),
                onValueChange = { sliderValue ->
                    currentPortions = sliderValue.roundToInt()
                },
                valueRange = MIN_PORTIONS..MAX_PORTIONS,
                steps = 10,
                modifier = Modifier.fillMaxWidth()
            )

            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = Dimens.ElevationSmall,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(Dimens.Space16)) {
                    if (scaledIngredients.isEmpty()) {
                        Text(
                            text = "Список ингредиентов пуст",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        scaledIngredients.forEachIndexed { index, ingredient ->
                            IngredientItem(ingredient = ingredient)

                            if (index < scaledIngredients.lastIndex) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = Dimens.Space8))
                            }
                        }
                    }
                }
            }

            Text(
                text = "Инструкция",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            if (recipe.method.isEmpty()) {
                Text(
                    text = "Инструкция пока не добавлена",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                recipe.method.forEachIndexed { index, step ->
                    Text(
                        text = "${index + 1}. $step",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

private fun scaleIngredients(
    ingredients: List<IngredientUiModel>,
    currentPortions: Int,
    basePortions: Int
): List<IngredientUiModel> {
    val multiplier = currentPortions.toDouble() / basePortions

    return ingredients.map { ingredient ->
        val baseAmount = ingredient.quantity.replace(",", ".").toDoubleOrNull()

        if (baseAmount == null) {
            ingredient
        } else {
            ingredient.copy(quantity = formatScaledAmount(baseAmount * multiplier))
        }
    }
}

private fun formatScaledAmount(amount: Double): String {
    val roundedToInt = amount.roundToInt().toDouble()
    if (abs(amount - roundedToInt) < 0.01) {
        return roundedToInt.toInt().toString()
    }

    return "%.2f".format(amount)
        .trimEnd('0')
        .trimEnd('.')
        .replace('.', ',')
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

