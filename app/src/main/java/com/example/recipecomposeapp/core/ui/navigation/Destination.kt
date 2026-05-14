package com.example.recipecomposeapp.core.ui.navigation

import android.net.Uri

sealed class Destination(val route: String) {
    data object Categories : Destination("categories")

    data object Favorites : Destination("favorites")

    data object Recipes : Destination("recipes/{categoryId}/{categoryTitle}") {
        private const val PATH = "recipes"

        fun createRoute(categoryId: Int, categoryTitle: String): String {
            return "$PATH/$categoryId/${Uri.encode(categoryTitle)}"
        }

        fun decodeTitle(encodedTitle: String): String {
            return Uri.decode(encodedTitle)
        }
    }

    data object RecipeDetails : Destination("recipe/{recipeId}") {
        const val KEY_RECIPE_OBJECT = "recipe_object"
        const val PARAM_RECIPE_ID = "recipeId"
        const val DEEP_LINK_SCHEME = "recipeapp"
        const val DEEP_LINK_BASE_URL = "https://recipes.androidsprint.ru"
        private const val PATH = "recipe"

        fun createRoute(recipeId: Int): String {
            return "$PATH/$recipeId"
        }

        fun createRecipeDeepLink(recipeId: Int): String {
            return "$DEEP_LINK_BASE_URL/$PATH/$recipeId"
        }
    }
}

