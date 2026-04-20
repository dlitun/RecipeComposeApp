package com.example.recipecomposeapp.ui.recipes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.recipecomposeapp.R
import com.example.recipecomposeapp.core.ui.theme.Dimens
import com.example.recipecomposeapp.core.ui.theme.RecipesAppTheme
import com.example.recipecomposeapp.ui.recipes.model.IngredientUiModel
import com.example.recipecomposeapp.ui.recipes.model.RecipeUiModel

@Composable
fun RecipeItem(
    recipe: RecipeUiModel,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageRequest = remember(context, recipe.imageUrl) {
        ImageRequest.Builder(context)
            .data(recipe.imageUrl)
            .crossfade(true)
            .build()
    }

    Card(
        onClick = { onClick(recipe.id) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.CornerLarge),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.ElevationMedium),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            AsyncImage(
                model = imageRequest,
                contentDescription = recipe.title,
                placeholder = painterResource(id = R.drawable.placeholder_header),
                error = painterResource(id = R.drawable.placeholder_header),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.4f)
            )

            Text(
                text = recipe.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(Dimens.Space16)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipeItemPreview() {
    RecipesAppTheme {
        RecipeItem(
            recipe = RecipeUiModel(
                id = 1,
                title = "Классический бургер с говядиной",
                imageUrl = "",
                ingredients = listOf(
                    IngredientUiModel(name = "Фарш", quantity = "0.5", unitOfMeasure = "кг")
                ),
                method = listOf("Смешать ингредиенты", "Обжарить котлеты"),
                isFavorite = false
            ),
            onClick = {}
        )
    }
}

