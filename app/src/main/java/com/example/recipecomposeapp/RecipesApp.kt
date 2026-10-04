package com.example.recipecomposeapp

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.recipecomposeapp.core.ui.navigation.BottomNavigation
import com.example.recipecomposeapp.core.ui.navigation.Destination
import com.example.recipecomposeapp.features.categories.ui.CategoriesScreen
import com.example.recipecomposeapp.features.details.ui.RecipeDetailsScreen
import com.example.recipecomposeapp.features.favorites.ui.FavoritesScreen
import com.example.recipecomposeapp.features.recipes.ui.RecipesScreen
import com.example.recipecomposeapp.core.ui.theme.RecipesAppTheme
import com.example.recipecomposeapp.core.utils.Constants
import com.example.recipecomposeapp.core.utils.FavoriteDataStoreManager
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.delay

@Composable
fun RecipesApp(deepLinkIntent: Intent? = null) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val favoriteDataStoreManager = remember(context) {
        FavoriteDataStoreManager(context.applicationContext)
    }
    val favoriteCountFlow = remember(favoriteDataStoreManager) {
        favoriteDataStoreManager.getFavoriteCountFlow()
    }
    val favoriteCount by favoriteCountFlow.collectAsState(initial = 0)

    Scaffold(
        bottomBar = {
            BottomNavigation(
                favoriteCount = favoriteCount,
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
                deepLinkIntent = deepLinkIntent
            )
        }
    }
}

@Composable
private fun AppNavHost(
    navController: androidx.navigation.NavHostController,
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
                        onCategoryClick = { categoryId, categoryTitle, categoryImageUrl ->
                            navController.navigate(
                                Destination.Recipes.createRecipesRoute(
                                    categoryId = categoryId,
                                    categoryTitle = categoryTitle,
                                    categoryImageUrl = categoryImageUrl
                                )
                            )
                        }
                    )
                }

                composable(route = Destination.Favorites.route) {
                    FavoritesScreen(
                        onRecipeClick = { recipeId ->
                            navController.navigate(Destination.RecipeDetails.createRoute(recipeId))
                        }
                    )
                }

                composable(
                    route = Destination.Recipes.route,
                    arguments = listOf(
                        navArgument(Constants.CATEGORY_ID) { type = NavType.IntType },
                        navArgument(Constants.CATEGORY_TITLE) { type = NavType.StringType },
                        navArgument(Constants.CATEGORY_IMAGE_URL) { type = NavType.StringType }
                    )
                ) {
                    RecipesScreen(
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
                ) {
                    RecipeDetailsScreen()
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
