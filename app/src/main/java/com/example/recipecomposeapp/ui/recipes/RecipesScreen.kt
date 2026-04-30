package com.example.recipecomposeapp.ui.recipes

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.recipecomposeapp.R
import com.example.recipecomposeapp.core.ui.ScreenHeader
import com.example.recipecomposeapp.core.ui.theme.Dimens
import com.example.recipecomposeapp.core.ui.theme.RecipesAppTheme
import com.example.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.example.recipecomposeapp.ui.recipes.model.RecipeUiModel
import com.example.recipecomposeapp.ui.recipes.model.toUiModel

@Composable
fun RecipesScreen(
    categoryId: Int,
    categoryTitle: String,
    onRecipeClick: (Int, RecipeUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val repository = remember { RecipesRepositoryStub() }
    var recipes by remember { mutableStateOf<List<RecipeUiModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(categoryId) {
        isLoading = true
        try {
            recipes = repository.getRecipesByCategoryId(categoryId).map { it.toUiModel() }
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        ScreenHeader(
            painter = painterResource(id = R.drawable.placeholder_header),
            contentDescription = "Заголовок экрана рецептов",
            text = categoryTitle,
            modifier = Modifier.fillMaxWidth()
        )

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            recipes.isEmpty() -> {
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

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(Dimens.Space16),
                    verticalArrangement = Arrangement.spacedBy(Dimens.Space8)
                ) {
                    items(
                        items = recipes,
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
}

@Preview(showBackground = true)
@Composable
private fun RecipesScreenPreview() {
    RecipesAppTheme {
        RecipesScreen(
            categoryId = 0,
            categoryTitle = "Бургеры",
            onRecipeClick = { _, _ -> }
        )
    }
}