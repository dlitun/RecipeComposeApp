package com.example.recipecomposeapp.ui.categories

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.recipecomposeapp.R
import com.example.recipecomposeapp.core.ui.ScreenHeader
import com.example.recipecomposeapp.core.ui.theme.Dimens
import com.example.recipecomposeapp.core.ui.theme.RecipesAppTheme

@Composable
fun CategoriesScreen() {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        ScreenHeader(
            painter = painterResource(id = R.drawable.placeholder_header),
            contentDescription = "Заголовок категории",
            text = "Категории"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.Space16)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoriesScreenPreview() {
    RecipesAppTheme {
        CategoriesScreen()
    }
}