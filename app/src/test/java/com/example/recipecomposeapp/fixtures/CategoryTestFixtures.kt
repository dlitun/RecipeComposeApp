package com.example.recipecomposeapp.fixtures

import com.example.recipecomposeapp.data.dto.CategoryDto

object CategoryTestFixtures {

    fun createCategoryDto(
        id: Int = 1,
        title: String = "Завтраки",
        description: String = "Утренние блюда",
        imageUrl: String = "breakfast.jpg"
    ) = CategoryDto(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl
    )

    fun createCategoryDtoList(count: Int = 3) = List(count) { index ->
        createCategoryDto(
            id = index + 1,
            title = "Категория ${index + 1}"
        )
    }
}
