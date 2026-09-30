package com.example.recipecomposeapp.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first

class FavoriteDataStoreManager(private val context: Context) {

    suspend fun isFavorite(recipeId: Int): Boolean {
        val preferences = context.dataStore.data.first()
        val favoriteIds = preferences[PreferencesKeys.FAVORITE_RECIPE_IDS].orEmpty()
        return recipeId.toString() in favoriteIds
    }

    suspend fun addFavorite(recipeId: Int) {
        context.dataStore.edit { preferences ->
            val favoriteIds = preferences[PreferencesKeys.FAVORITE_RECIPE_IDS].orEmpty()
            preferences[PreferencesKeys.FAVORITE_RECIPE_IDS] = favoriteIds + recipeId.toString()
        }
    }

    suspend fun removeFavorite(recipeId: Int) {
        context.dataStore.edit { preferences ->
            val favoriteIds = preferences[PreferencesKeys.FAVORITE_RECIPE_IDS].orEmpty()
            preferences[PreferencesKeys.FAVORITE_RECIPE_IDS] = favoriteIds - recipeId.toString()
        }
    }
}
