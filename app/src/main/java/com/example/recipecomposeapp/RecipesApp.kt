package com.example.recipecomposeapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.recipecomposeapp.core.ui.navigation.BottomNavigation
import com.example.recipecomposeapp.ui.categories.CategoriesScreen
import com.example.recipecomposeapp.ui.favorites.FavoritesScreen
import com.example.recipecomposeapp.ui.recipes.RecipesScreen
import com.example.recipecomposeapp.core.ui.theme.RecipesAppTheme

@Composable
fun RecipesApp() {
    var currentScreen by remember {
        mutableStateOf(ScreenId.CATEGORIES)
    }
    var selectedCategoryId by remember {
        mutableStateOf<Int?>(null)
    }
    var selectedCategoryTitle by remember {
        mutableStateOf("")
    }
    val onCategorySelected: (Int, String) -> Unit = { categoryId, categoryTitle ->
        selectedCategoryId = categoryId
        selectedCategoryTitle = categoryTitle
        currentScreen = ScreenId.RECIPES
    }

    Scaffold(
        bottomBar = {
            BottomNavigation(
                onCategoriesClick = {
                    currentScreen = ScreenId.CATEGORIES
                },
                onFavoriteClick = {
                    currentScreen = ScreenId.FAVORITES
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                ScreenId.CATEGORIES -> CategoriesScreen(onCategoryClick = onCategorySelected)
                ScreenId.FAVORITES -> FavoritesScreen()
                ScreenId.RECIPES -> {
                    val categoryId = selectedCategoryId
                    if (categoryId != null) {
                        RecipesScreen(
                            categoryId = categoryId,
                            categoryTitle = selectedCategoryTitle,
                            onRecipeClick = {}
                        )
                    } else {
                        CategoriesScreen(onCategoryClick = onCategorySelected)
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RecipesAppPreview() {
    RecipesAppTheme {
        RecipesApp()
    }
}