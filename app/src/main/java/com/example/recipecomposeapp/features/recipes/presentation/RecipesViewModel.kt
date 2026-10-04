package com.example.recipecomposeapp.features.recipes.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipecomposeapp.core.utils.Constants
import com.example.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.example.recipecomposeapp.features.recipes.presentation.model.RecipesUiState
import com.example.recipecomposeapp.features.recipes.presentation.model.toUiModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipesViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repository = RecipesRepositoryStub()

    private val categoryId: Int = requireNotNull(savedStateHandle[Constants.CATEGORY_ID]) {
        "Параметр categoryId обязателен"
    }

    private val categoryTitle: String = decodeRouteParameter(
        requireNotNull(savedStateHandle[Constants.CATEGORY_TITLE]) {
            "Параметр categoryTitle обязателен"
        }
    )

    private val categoryImageUrl: String = decodeRouteParameter(
        requireNotNull(savedStateHandle[Constants.CATEGORY_IMAGE_URL]) {
            "Параметр categoryImageUrl обязателен"
        }
    )

    private val _uiState = MutableStateFlow(
        RecipesUiState(
            categoryTitle = categoryTitle,
            categoryImageUrl = categoryImageUrl
        )
    )
    val uiState: StateFlow<RecipesUiState> = _uiState.asStateFlow()

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isLoading = true, error = null)
            }

            try {
                val recipes = repository
                    .getRecipesByCategoryId(categoryId)
                    .map { recipe -> recipe.toUiModel() }

                _uiState.update { currentState ->
                    currentState.copy(
                        recipes = recipes,
                        isLoading = false
                    )
                }
            } catch (exception: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = exception.message ?: "Не удалось загрузить рецепты"
                    )
                }
            }
        }
    }

    private fun decodeRouteParameter(value: String): String {
        return URLDecoder.decode(value, StandardCharsets.UTF_8.toString())
    }
}
