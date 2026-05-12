package com.example.recipecomposeapp.core

import android.content.Context
import android.content.Intent
import com.example.recipecomposeapp.core.ui.navigation.Destination

fun shareRecipe(context: Context, recipeId: Int, recipeTitle: String) {
    val shareLink = Destination.RecipeDetails.createRecipeDeepLink(recipeId)
    val shareText = "Попробуй этот рецепт: $recipeTitle\n$shareLink"

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, shareText)
    }

    context.startActivity(Intent.createChooser(shareIntent, "Поделиться рецептом"))
}

