package com.example.recipecomposeapp.features.details.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.recipecomposeapp.core.ui.navigation.Destination
import com.example.recipecomposeapp.core.utils.FavoriteDataStoreManager
import com.example.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.example.recipecomposeapp.features.details.presentation.model.DEFAULT_PORTIONS
import com.example.recipecomposeapp.features.details.presentation.model.MAX_PORTIONS
import com.example.recipecomposeapp.features.details.presentation.model.MIN_PORTIONS
import com.example.recipecomposeapp.features.details.presentation.model.RecipeDetailsUiState
import com.example.recipecomposeapp.features.recipes.presentation.model.toUiModel
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

class RecipeDetailsViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    private val repository = RecipesRepositoryStub()
    private val favoriteDataStoreManager = FavoriteDataStoreManager(application)

    private val recipeId: Int = savedStateHandle[Destination.RecipeDetails.PARAM_RECIPE_ID]
        ?: throw IllegalArgumentException("recipeId is required")

    private val _uiState = MutableStateFlow(
        RecipeDetailsUiState(
            portions = savedStateHandle[KEY_PORTIONS] ?: DEFAULT_PORTIONS
        )
    )
    val uiState: StateFlow<RecipeDetailsUiState> = _uiState.asStateFlow()

    private var favoriteUpdatesJob: Job? = null

    init {
        loadRecipe(recipeId)
    }

    private fun loadRecipe(recipeId: Int) {
        val recipe = repository.getRecipeById(recipeId)?.toUiModel()
        if (recipe == null) {
            _uiState.update { currentState ->
                currentState.copy(
                    recipe = null,
                    isLoading = false,
                    error = "Рецепт не найден"
                )
            }
            return
        }

        _uiState.update { currentState ->
            currentState.copy(recipe = recipe, isLoading = true, error = null)
        }

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
        val updatedPortions = portions.coerceIn(MIN_PORTIONS, MAX_PORTIONS)
        savedStateHandle[KEY_PORTIONS] = updatedPortions
        _uiState.update { currentState ->
            currentState.copy(portions = updatedPortions)
        }
    }

    private companion object {
        const val KEY_PORTIONS = "portions"
    }
}
