package com.example.recipecomposeapp.ui.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
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
import com.example.recipecomposeapp.R
import com.example.recipecomposeapp.core.ui.ScreenHeader
import com.example.recipecomposeapp.core.ui.theme.Dimens
import com.example.recipecomposeapp.core.ui.theme.RecipesAppTheme
import com.example.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.example.recipecomposeapp.ui.recipes.RecipeItem
import com.example.recipecomposeapp.ui.recipes.model.toUiModel
import com.example.recipecomposeapp.util.FavoriteDataStoreManager
import kotlinx.coroutines.flow.map

@Composable
fun FavoritesScreen(
    repository: RecipesRepositoryStub,
    favoriteDataStoreManager: FavoriteDataStoreManager,
    onRecipeClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val favoriteRecipesFlow = remember(repository, favoriteDataStoreManager) {
        favoriteDataStoreManager.getFavoriteIdsFlow().map { favoriteIds ->
            favoriteIds.mapNotNull { favoriteId ->
                val recipeId = favoriteId.toIntOrNull() ?: return@mapNotNull null

                try {
                    repository.getRecipeById(recipeId)?.toUiModel()
                } catch (_: Exception) {
                    null
                }
            }
        }
    }
    val favoriteRecipes by favoriteRecipesFlow.collectAsState(initial = emptyList())

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        ScreenHeader(
            painter = painterResource(id = R.drawable.bcg_favorites),
            contentDescription = "Заголовок экрана избранного",
            text = "Избранное"
        )

        if (favoriteRecipes.isEmpty()) {
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
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(Dimens.Space16),
                verticalArrangement = Arrangement.spacedBy(Dimens.Space8)
            ) {
                items(
                    items = favoriteRecipes,
                    key = { recipe -> recipe.id }
                ) { recipe ->
                    RecipeItem(
                        recipe = recipe,
                        onClick = onRecipeClick
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesScreenPreview() {
    val context = LocalContext.current
    val repository = remember { RecipesRepositoryStub() }
    val favoriteDataStoreManager = remember(context) {
        FavoriteDataStoreManager(context.applicationContext)
    }

    RecipesAppTheme {
        FavoritesScreen(
            repository = repository,
            favoriteDataStoreManager = favoriteDataStoreManager,
            onRecipeClick = { }
        )
    }
}
