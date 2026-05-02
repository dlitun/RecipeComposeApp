package com.example.recipecomposeapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.recipecomposeapp.core.ui.navigation.BottomNavigation
import com.example.recipecomposeapp.core.ui.navigation.Destination
import com.example.recipecomposeapp.ui.categories.CategoriesScreen
import com.example.recipecomposeapp.ui.details.RecipeDetailsScreen
import com.example.recipecomposeapp.ui.favorites.FavoritesScreen
import com.example.recipecomposeapp.ui.recipes.RecipesScreen
import com.example.recipecomposeapp.ui.recipes.model.RecipeUiModel
import com.example.recipecomposeapp.core.ui.theme.RecipesAppTheme
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun RecipesApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigation(
                onCategoriesClick = {
                    navController.navigate(Destination.Categories.route)
                },
                onFavoriteClick = {
                    navController.navigate(Destination.Favorites.route)
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = Destination.Categories.route
            ) {
                composable(route = Destination.Categories.route) {
                    CategoriesScreen(
                        onCategoryClick = { categoryId, categoryTitle ->
                            navController.navigate(
                                Destination.Recipes.createRoute(
                                    categoryId = categoryId,
                                    categoryTitle = categoryTitle
                                )
                            )
                        }
                    )
                }

                composable(route = Destination.Favorites.route) {
                    FavoritesScreen()
                }

                composable(
                    route = Destination.Recipes.route,
                    arguments = listOf(
                        navArgument("categoryId") { type = NavType.IntType },
                        navArgument("categoryTitle") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: return@composable
                    val encodedCategoryTitle = backStackEntry.arguments?.getString("categoryTitle") ?: ""

                    RecipesScreen(
                        categoryId = categoryId,
                        categoryTitle = Destination.Recipes.decodeTitle(encodedCategoryTitle),
                        onRecipeClick = { recipeId, recipe ->
                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set(Destination.RecipeDetails.KEY_RECIPE_OBJECT, recipe)

                            navController.navigate(Destination.RecipeDetails.createRoute(recipeId))
                        }
                    )
                }

                composable(
                    route = Destination.RecipeDetails.route,
                    arguments = listOf(
                        navArgument("recipeId") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: return@composable
                    val recipe = navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.get<RecipeUiModel>(Destination.RecipeDetails.KEY_RECIPE_OBJECT)

                    if (recipe != null && recipe.id == recipeId) {
                        RecipeDetailsScreen(recipe = recipe)
                    } else {
                        Text(text = "Рецепт не найден")
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