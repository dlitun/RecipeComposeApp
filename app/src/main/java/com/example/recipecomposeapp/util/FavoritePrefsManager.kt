package com.example.recipecomposeapp.util

import android.content.Context
import androidx.core.content.edit

class FavoritePrefsManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    fun isFavorite(recipeId: Int): Boolean {
        return recipeId.toString() in getAllFavorites()
    }

    fun addToFavorites(recipeId: Int) {
        updateFavorites { add(recipeId.toString()) }
    }

    fun removeFromFavorites(recipeId: Int) {
        updateFavorites { remove(recipeId.toString()) }
    }

    fun getAllFavorites(): Set<String> {
        return sharedPreferences
            .getStringSet(FAVORITE_RECIPE_IDS_KEY, emptySet())
            .orEmpty()
            .toSet()
    }

    private fun updateFavorites(update: MutableSet<String>.() -> Unit) {
        val favorites = getAllFavorites().toMutableSet()
        favorites.update()

        sharedPreferences.edit {
            putStringSet(FAVORITE_RECIPE_IDS_KEY, favorites)
        }
    }

    private companion object {
        const val PREFS_NAME = "recipe_app_prefs"
        const val FAVORITE_RECIPE_IDS_KEY = "favorite_recipe_ids"
    }
}
