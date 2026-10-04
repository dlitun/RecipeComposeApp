package com.example.recipecomposeapp.core.ui.navigation

import com.example.recipecomposeapp.core.utils.Constants
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Destination(val route: String) {
    data object Categories : Destination("categories")

    data object Favorites : Destination("favorites")

    data object Recipes : Destination(
        "recipes/{${Constants.CATEGORY_ID}}/{${Constants.CATEGORY_TITLE}}/{${Constants.CATEGORY_IMAGE_URL}}"
    ) {
        private const val PATH = "recipes"

        fun createRecipesRoute(
            categoryId: Int,
            categoryTitle: String,
            categoryImageUrl: String
        ): String {
            val charset = StandardCharsets.UTF_8.toString()
            val encodedTitle = URLEncoder.encode(categoryTitle, charset)
            val encodedImageUrl = URLEncoder.encode(categoryImageUrl, charset)

            return "$PATH/$categoryId/$encodedTitle/$encodedImageUrl"
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

