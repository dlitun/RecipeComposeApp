package com.example.recipecomposeapp.features.favorites.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipecomposeapp.R
import com.example.recipecomposeapp.core.ui.ScreenHeader
import com.example.recipecomposeapp.core.ui.theme.Dimens
import com.example.recipecomposeapp.core.ui.theme.RecipesAppTheme
import com.example.recipecomposeapp.features.favorites.presentation.FavoritesViewModel
import com.example.recipecomposeapp.features.favorites.presentation.model.FavoritesUiState
import com.example.recipecomposeapp.features.recipes.ui.RecipeItem

@Composable
fun FavoritesScreen(
    onRecipeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    FavoritesContent(uiState = uiState, onRecipeClick = onRecipeClick, modifier = modifier)
}

@Composable
private fun FavoritesContent(
    uiState: FavoritesUiState,
    onRecipeClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        ScreenHeader(
            painter = painterResource(id = R.drawable.bcg_favorites),
            contentDescription = "Заголовок экрана избранного",
            text = "Избранное"
        )

        when {
            uiState.isLoading -> FavoritesLoadingState()
            uiState.error != null -> FavoritesErrorState(uiState.error)
            uiState.isEmpty -> EmptyFavoritesState()
            else -> FavoritesList(
                uiState = uiState,
                onRecipeClick = onRecipeClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun FavoritesLoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun FavoritesErrorState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.Space16),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
    }
}

@Composable
private fun EmptyFavoritesState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.Space16),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Пока нет избранных рецептов",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun FavoritesList(
    uiState: FavoritesUiState,
    onRecipeClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(Dimens.Space16),
        verticalArrangement = Arrangement.spacedBy(Dimens.Space8)
    ) {
        items(uiState.favoriteRecipes, key = { recipe -> recipe.id }) { recipe ->
            RecipeItem(recipe = recipe, onClick = onRecipeClick)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesScreenPreview() {
    RecipesAppTheme {
        FavoritesContent(
            uiState = FavoritesUiState(isLoading = false),
            onRecipeClick = {}
        )
    }
}
