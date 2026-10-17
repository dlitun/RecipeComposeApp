package com.example.recipecomposeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipecomposeapp.core.utils.FavoriteDataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class RecipesAppViewModel @Inject constructor(
    favoriteDataStoreManager: FavoriteDataStoreManager
) : ViewModel() {
    val favoriteCount: StateFlow<Int> = favoriteDataStoreManager
        .getFavoriteCountFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )
}
