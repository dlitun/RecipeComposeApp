package com.example.recipecomposeapp.features.details.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.recipecomposeapp.R
import com.example.recipecomposeapp.core.ui.ScreenHeader
import com.example.recipecomposeapp.core.ui.theme.Dimens
import com.example.recipecomposeapp.core.ui.theme.RecipesAppTheme
import com.example.recipecomposeapp.core.utils.shareRecipe
import com.example.recipecomposeapp.features.details.presentation.RecipeDetailsViewModel
import com.example.recipecomposeapp.features.details.presentation.model.MAX_PORTIONS
import com.example.recipecomposeapp.features.details.presentation.model.MIN_PORTIONS
import com.example.recipecomposeapp.features.details.presentation.model.RecipeDetailsUiState
import com.example.recipecomposeapp.features.recipes.presentation.model.IngredientUiModel
import com.example.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel
import kotlin.math.roundToInt

@Composable
fun RecipeDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: RecipeDetailsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    RecipeDetailsContent(
        uiState = uiState,
        onFavoriteToggle = viewModel::toggleFavorite,
        onPortionsChange = viewModel::updatePortions,
        modifier = modifier
    )
}

@Composable
private fun RecipeDetailsContent(
    uiState: RecipeDetailsUiState,
    onFavoriteToggle: () -> Unit,
    onPortionsChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val recipe = uiState.recipe

    when {
        uiState.isLoading -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        recipe == null -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = uiState.error ?: "Рецепт не найден",
                color = MaterialTheme.colorScheme.error
            )
        }

        else -> RecipeDetails(
            recipe = recipe,
            portions = uiState.portions,
            scaledIngredients = uiState.scaledIngredients,
            error = uiState.error,
            onFavoriteToggle = onFavoriteToggle,
            onPortionsChange = onPortionsChange,
            modifier = modifier
        )
    }
}

@Composable
private fun RecipeDetails(
    recipe: RecipeUiModel,
    portions: Int,
    scaledIngredients: List<IngredientUiModel>,
    error: String?,
    onFavoriteToggle: () -> Unit,
    onPortionsChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageRequest = remember(context, recipe.imageUrl) {
        ImageRequest.Builder(context)
            .data(recipe.imageUrl)
            .crossfade(true)
            .build()
    }
    val headerPainter = rememberAsyncImagePainter(
        model = imageRequest,
        placeholder = painterResource(id = R.drawable.placeholder_header),
        error = painterResource(id = R.drawable.placeholder_header)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ScreenHeader(
            painter = headerPainter,
            contentDescription = recipe.title,
            text = recipe.title,
            showShareButton = true,
            showFavoriteButton = true,
            isFavorite = recipe.isFavorite,
            onFavoriteToggle = onFavoriteToggle,
            onShareClick = { shareRecipe(context, recipe.id, recipe.title) }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Space16),
            verticalArrangement = Arrangement.spacedBy(Dimens.Space16)
        ) {
            error?.let { message ->
                Text(text = message, color = MaterialTheme.colorScheme.error)
            }

            Text(
                text = "Ингредиенты",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Порции: $portions",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Slider(
                value = portions.toFloat(),
                onValueChange = { onPortionsChange(it.roundToInt()) },
                valueRange = MIN_PORTIONS.toFloat()..MAX_PORTIONS.toFloat(),
                steps = MAX_PORTIONS - MIN_PORTIONS - 1,
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

@Preview(showBackground = true)
@Composable
private fun RecipeDetailsScreenPreview() {
    RecipesAppTheme {
        RecipeDetailsContent(
            uiState = RecipeDetailsUiState(
                recipe = RecipeUiModel(
                    id = 1,
                    title = "Классический бургер",
                    imageUrl = "",
                    ingredients = listOf(
                        IngredientUiModel(name = "Фарш", quantity = "500", unitOfMeasure = "г")
                    ),
                    method = listOf("Сформировать котлеты", "Обжарить"),
                    isFavorite = false
                ),
                isLoading = false
            ),
            onFavoriteToggle = {},
            onPortionsChange = {}
        )
    }
}
