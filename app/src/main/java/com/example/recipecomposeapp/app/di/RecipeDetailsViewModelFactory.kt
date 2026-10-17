package com.example.recipecomposeapp.app.di

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.example.recipecomposeapp.data.repository.RecipesRepository
import com.example.recipecomposeapp.features.details.presentation.RecipeDetailsViewModel

class RecipeDetailsViewModelFactory(
    private val application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val repository: RecipesRepository
) : Factory<RecipeDetailsViewModel> {
    override fun create(): RecipeDetailsViewModel = RecipeDetailsViewModel(
        application = application,
        savedStateHandle = savedStateHandle,
        repository = repository
    )
}
