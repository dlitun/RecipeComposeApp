package com.example.recipecomposeapp

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.recipecomposeapp.core.ui.navigation.BottomNavigation
import com.example.recipecomposeapp.core.ui.navigation.Destination
import com.example.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.example.recipecomposeapp.ui.categories.CategoriesScreen
import com.example.recipecomposeapp.ui.details.RecipeDetailsScreen
import com.example.recipecomposeapp.ui.favorites.FavoritesScreen
import com.example.recipecomposeapp.ui.recipes.RecipesScreen
import com.example.recipecomposeapp.ui.recipes.model.toUiModel
import com.example.recipecomposeapp.core.ui.theme.RecipesAppTheme
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.delay

@Composable
fun RecipesApp(deepLinkIntent: Intent? = null) {
    val navController = rememberNavController()
    val repository = remember { RecipesRepositoryStub() }

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
            AppNavHost(
                navController = navController,
                repository = repository,
                deepLinkIntent = deepLinkIntent
            )
        }
    }
}

@Composable
private fun AppNavHost(
    navController: androidx.navigation.NavHostController,
    repository: RecipesRepositoryStub,
    deepLinkIntent: Intent?
) {
    LaunchedEffect(deepLinkIntent) {
        val uri = deepLinkIntent?.data ?: return@LaunchedEffect
        val recipeId = parseRecipeIdFromDeepLink(uri) ?: return@LaunchedEffect

        delay(100)
        navController.navigate(Destination.RecipeDetails.createRoute(recipeId)) {
            launchSingleTop = true
        }
    }

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
                        onRecipeClick = { recipeId ->
                            navController.navigate(Destination.RecipeDetails.createRoute(recipeId))
                        }
                    )
                }

                composable(
                    route = Destination.RecipeDetails.route,
                    arguments = listOf(
                        navArgument(Destination.RecipeDetails.PARAM_RECIPE_ID) { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    val recipeId = backStackEntry.arguments
                        ?.getInt(Destination.RecipeDetails.PARAM_RECIPE_ID)
                        ?: return@composable
                    val recipe = repository.getRecipeById(recipeId)?.toUiModel()

                    if (recipe != null) {
                        RecipeDetailsScreen(recipe = recipe)
                    } else {
                        Text(text = "Рецепт не найден")
                    }
                }
            }
}

private fun parseRecipeIdFromDeepLink(uri: Uri): Int? {
    val deepLinkHost = Uri.parse(Destination.RecipeDetails.DEEP_LINK_BASE_URL).host

    return when (uri.scheme) {
        Destination.RecipeDetails.DEEP_LINK_SCHEME -> {
            if (uri.host == "recipe" && uri.pathSegments.isNotEmpty()) {
                uri.pathSegments[0].toIntOrNull()
            } else {
                null
            }
        }

        "https", "http" -> {
            if (uri.host == deepLinkHost &&
                uri.pathSegments.size >= 2 &&
                uri.pathSegments[0] == "recipe"
            ) {
                uri.pathSegments[1].toIntOrNull()
            } else {
                null
            }
        }

        else -> null
    }
}


@Preview(showBackground = true)
@Composable
fun RecipesAppPreview() {
    RecipesAppTheme {
        RecipesApp()
    }
}