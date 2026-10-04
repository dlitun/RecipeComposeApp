package com.example.recipecomposeapp.features.details.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipecomposeapp.core.utils.FavoriteDataStoreManager
import com.example.recipecomposeapp.features.details.presentation.model.MAX_PORTIONS
import com.example.recipecomposeapp.features.details.presentation.model.MIN_PORTIONS
import com.example.recipecomposeapp.features.details.presentation.model.RecipeDetailsUiState
import com.example.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val favoriteDataStoreManager = FavoriteDataStoreManager(application)

    private val _uiState = MutableStateFlow(RecipeDetailsUiState())
    val uiState: StateFlow<RecipeDetailsUiState> = _uiState.asStateFlow()

    private var favoriteUpdatesJob: Job? = null

    fun initializeWithRecipe(recipe: RecipeUiModel) {
        if (_uiState.value.recipe?.id == recipe.id) return

        _uiState.value = RecipeDetailsUiState(recipe = recipe)

        favoriteUpdatesJob?.cancel()
        favoriteUpdatesJob = favoriteDataStoreManager
            .isFavoriteFlow(recipe.id)
            .onEach { isFavorite ->
                _uiState.update { currentState ->
                    currentState.copy(
                        recipe = currentState.recipe?.copy(isFavorite = isFavorite),
                        isLoading = false,
                        error = null
                    )
                }
            }
            .catch { exception ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = exception.message ?: "Не удалось проверить избранное"
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun toggleFavorite() {
        val recipe = _uiState.value.recipe ?: return

        viewModelScope.launch {
            try {
                if (recipe.isFavorite) {
                    favoriteDataStoreManager.removeFavorite(recipe.id)
                } else {
                    favoriteDataStoreManager.addFavorite(recipe.id)
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        error = exception.message ?: "Не удалось изменить избранное"
                    )
                }
            }
        }
    }

    fun updatePortions(portions: Int) {
        _uiState.update { currentState ->
            currentState.copy(portions = portions.coerceIn(MIN_PORTIONS, MAX_PORTIONS))
        }
    }
}
