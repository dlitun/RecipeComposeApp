package com.example.recipecomposeapp.features.categories.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipecomposeapp.data.repository.RecipesRepository
import com.example.recipecomposeapp.features.categories.presentation.model.CategoriesUiState
import com.example.recipecomposeapp.features.categories.presentation.model.toUiModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoriesViewModel(
    private val repository: RecipesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isLoading = true, error = null)
            }

            try {
                repository.getCategories().collect { categories ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            categories = categories.map { category -> category.toUiModel() },
                            isLoading = false,
                            error = null
                        )
                    }
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = exception.message ?: "Не удалось загрузить категории"
                    )
                }
            }
        }
    }
}
