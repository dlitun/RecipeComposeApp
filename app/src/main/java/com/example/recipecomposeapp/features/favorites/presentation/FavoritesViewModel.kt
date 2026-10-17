package com.example.recipecomposeapp.features.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipecomposeapp.core.utils.FavoriteDataStoreManager
import com.example.recipecomposeapp.data.repository.RecipesRepository
import com.example.recipecomposeapp.features.favorites.presentation.model.FavoritesUiState
import com.example.recipecomposeapp.features.recipes.presentation.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: RecipesRepository,
    private val favoriteDataStoreManager: FavoriteDataStoreManager
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<FavoritesUiState> = favoriteDataStoreManager
        .getFavoriteIdsFlow()
        .flatMapLatest { favoriteIds ->
            val recipeIds = favoriteIds.mapNotNull { favoriteId -> favoriteId.toIntOrNull() }
            if (recipeIds.isEmpty()) {
                flowOf(emptyList())
            } else {
                repository.getRecipesByIds(recipeIds)
            }
        }
        .map { recipes ->
            FavoritesUiState(
                favoriteRecipes = recipes.map { recipe -> recipe.toUiModel() },
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
