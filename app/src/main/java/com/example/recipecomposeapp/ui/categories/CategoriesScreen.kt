package com.example.recipecomposeapp.ui.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.recipecomposeapp.R
import com.example.recipecomposeapp.core.ui.ScreenHeader
import com.example.recipecomposeapp.core.ui.theme.Dimens
import com.example.recipecomposeapp.core.ui.theme.RecipesAppTheme
import com.example.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.example.recipecomposeapp.ui.categories.model.toUiModel

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    onCategoryClick: (Int, String) -> Unit
) {
    val categories = remember {
        RecipesRepositoryStub().getCategories().map { it.toUiModel() }
    }
    val gridState = rememberLazyGridState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        ScreenHeader(
            painter = painterResource(id = R.drawable.placeholder_header),
            contentDescription = "Заголовок категории",
            text = "Категории",
            modifier = Modifier.fillMaxWidth()
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = gridState,
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Space8),
            verticalArrangement = Arrangement.spacedBy(Dimens.Space8),
            contentPadding = PaddingValues(Dimens.Space16)
        ) {
            items(categories, key = { it.id }) { category ->
                CategoryItem(
                    category = category,
                    onClick = { onCategoryClick(category.id, category.title) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoriesScreenPreview() {
    RecipesAppTheme {
        CategoriesScreen(
            onCategoryClick = { _, _ -> }
        )
    }
}



