package com.example.recipecomposeapp.app.di

import android.app.Application
import com.example.recipecomposeapp.data.repository.RecipesRepository
import com.example.recipecomposeapp.features.favorites.presentation.FavoritesViewModel

class FavoritesViewModelFactory(
    private val application: Application,
    private val repository: RecipesRepository
) : Factory<FavoritesViewModel> {
    override fun create(): FavoritesViewModel = FavoritesViewModel(application, repository)
}
