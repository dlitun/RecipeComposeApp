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
}

