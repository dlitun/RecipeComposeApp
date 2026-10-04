package com.example.recipecomposeapp.features.favorites.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipecomposeapp.core.utils.FavoriteDataStoreManager
import com.example.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.example.recipecomposeapp.features.favorites.presentation.model.FavoritesUiState
import com.example.recipecomposeapp.features.recipes.presentation.model.toUiModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RecipesRepositoryStub()
    private val favoriteDataStoreManager = FavoriteDataStoreManager(application)

    val uiState: StateFlow<FavoritesUiState> = favoriteDataStoreManager
        .getFavoriteIdsFlow()
        .map { favoriteIds ->
            FavoritesUiState(
                favoriteRecipes = favoriteIds.mapNotNull { favoriteId ->
                    favoriteId.toIntOrNull()
                        ?.let(repository::getRecipeById)
                        ?.toUiModel()
                },
                isLoading = false
            )
        }
        .onStart { emit(FavoritesUiState(isLoading = true)) }
        .catch { exception ->
            emit(
                FavoritesUiState(
                    isLoading = false,
                    error = exception.message ?: "Не удалось загрузить избранное"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FavoritesUiState(isLoading = true)
        )
}
