package com.example.recipecomposeapp.app.di

import com.example.recipecomposeapp.data.repository.RecipesRepository
import com.example.recipecomposeapp.features.categories.presentation.CategoriesViewModel

class CategoriesViewModelFactory(
    private val repository: RecipesRepository
) : Factory<CategoriesViewModel> {
    override fun create(): CategoriesViewModel = CategoriesViewModel(repository)
}
