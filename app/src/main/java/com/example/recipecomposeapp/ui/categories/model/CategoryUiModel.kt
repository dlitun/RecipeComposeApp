package com.example.recipecomposeapp.ui.categories.model

import androidx.compose.runtime.Immutable
import com.example.recipecomposeapp.core.Constants
import com.example.recipecomposeapp.data.dto.CategoryDto

@Immutable
data class CategoryUiModel(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String
)

fun CategoryDto.toUiModel(): CategoryUiModel {
    return CategoryUiModel(
        id = id,
        title = title,
        description = description,
        imageUrl = if (imageUrl.startsWith("http")) imageUrl else Constants.ASSETS_URI_PREFIX + imageUrl
    )
}