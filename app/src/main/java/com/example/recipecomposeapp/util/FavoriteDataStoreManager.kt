package com.example.recipecomposeapp.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteDataStoreManager(private val context: Context) {

    fun getFavoriteIdsFlow(): Flow<Set<String>> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.FAVORITE_RECIPE_IDS].orEmpty()
        }
    }

    fun isFavoriteFlow(recipeId: Int): Flow<Boolean> {
        return getFavoriteIdsFlow().map { favoriteIds ->
            recipeId.toString() in favoriteIds
        }
    }

    fun getFavoriteCountFlow(): Flow<Int> {
        return getFavoriteIdsFlow().map { favoriteIds ->
            favoriteIds.size
        }
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
