package com.example.recipecomposeapp.features.recipes.ui

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
import coil.compose.rememberAsyncImagePainter
import com.example.recipecomposeapp.R
import com.example.recipecomposeapp.core.ui.ScreenHeader
import com.example.recipecomposeapp.core.ui.theme.Dimens
import com.example.recipecomposeapp.core.ui.theme.RecipesAppTheme
import com.example.recipecomposeapp.features.recipes.presentation.RecipesViewModel
import com.example.recipecomposeapp.features.recipes.presentation.model.RecipesUiState

@Composable
fun RecipesScreen(
    onRecipeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecipesViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    RecipesContent(
        uiState = uiState,
        onRecipeClick = onRecipeClick,
        modifier = modifier
    )
}

@Composable
private fun RecipesContent(
    uiState: RecipesUiState,
    onRecipeClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryImagePainter = rememberAsyncImagePainter(
        model = uiState.categoryImageUrl,
        placeholder = painterResource(id = R.drawable.placeholder_header),
        error = painterResource(id = R.drawable.placeholder_header)
    )

    Column(modifier = modifier.fillMaxSize()) {
        ScreenHeader(
            painter = categoryImagePainter,
            contentDescription = "Изображение категории ${uiState.categoryTitle}",
            text = uiState.categoryTitle,
            modifier = Modifier.fillMaxWidth()
        )

        when {
            uiState.isLoading -> LoadingState()
            uiState.error != null -> ErrorState(message = uiState.error)
            uiState.isEmpty -> EmptyState()
            uiState.isNotEmpty -> RecipesList(
                uiState = uiState,
                onRecipeClick = onRecipeClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.Space16),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.Space16),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Для этой категории пока нет рецептов",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun RecipesList(
    uiState: RecipesUiState,
    onRecipeClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(Dimens.Space16),
        verticalArrangement = Arrangement.spacedBy(Dimens.Space8)
    ) {
        items(
            items = uiState.recipes,
            key = { recipe -> recipe.id }
        ) { recipe ->
            RecipeItem(
                recipe = recipe,
                onClick = onRecipeClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipesScreenPreview() {
    RecipesAppTheme {
        RecipesContent(
            uiState = RecipesUiState(categoryTitle = "Бургеры"),
            onRecipeClick = { }
        )
    }
}
