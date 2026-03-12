package com.example.recipecomposeapp.core.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.recipecomposeapp.ui.theme.Dimens

@Composable
fun BottomNavigation(
    onCategoriesClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.Space16, vertical = Dimens.Space8),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Space8)
    ) {
        Button(
            onClick = onCategoriesClick,
            modifier = Modifier
                       .weight(1f)
                       .height(Dimens.ButtonHeight),
            shape = RoundedCornerShape(Dimens.CornerLarge),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            )
        ) {
            Text(
                text = "Категории"
            )
        }

        Button(
            onClick = onFavoriteClick,
            modifier = Modifier
                .weight(1f)
                .height(Dimens.ButtonHeight),
            shape = RoundedCornerShape(Dimens.CornerLarge),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text(
                text = "Избранное"
            )
        }
    }
}