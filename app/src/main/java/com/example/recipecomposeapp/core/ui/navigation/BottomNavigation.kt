package com.example.recipecomposeapp.core.ui.navigation

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recipecomposeapp.R
import com.example.recipecomposeapp.core.ui.theme.Dimens
import com.example.recipecomposeapp.core.ui.theme.RecipesAppTheme

private val NavigationButtonVisualHeight = 36.dp
private val NavigationButtonTouchHeight = 48.dp
private val NavigationBarVerticalPadding = 2.dp
private val NavigationButtonSpacing = 4.dp

@Composable
fun BottomNavigation(
    favoriteCount: Int,
    onCategoriesClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Dimens.Space16,
                vertical = NavigationBarVerticalPadding
            ),
        horizontalArrangement = Arrangement.spacedBy(NavigationButtonSpacing)
    ) {
        NavigationButton(
            onClick = onCategoriesClick,
            containerColor = MaterialTheme.colorScheme.tertiary
        ) {
            Text(
                text = "Категории".uppercase(),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 14.sp,
                    lineHeight = 16.sp
                )
            )
        }

        NavigationButton(
            onClick = onFavoriteClick,
            containerColor = MaterialTheme.colorScheme.error
        ) {
            Text(
                text = "Избранное".uppercase(),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 14.sp,
                    lineHeight = 16.sp
                )
            )

            Spacer(
                modifier = Modifier.width(Dimens.Space10)
            )

            BadgedBox(
                badge = {
                    if (favoriteCount > 0) {
                        Badge {
                            Text(text = favoriteCount.toString())
                        }
                    }
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_heart_empty),
                    contentDescription = "Избранное",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun RowScope.NavigationButton(
    onClick: () -> Unit,
    containerColor: Color,
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .weight(1f)
            .height(NavigationButtonTouchHeight)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick
            )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(NavigationButtonVisualHeight)
                .align(Alignment.Center),
            color = containerColor,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = RoundedCornerShape(Dimens.CornerSmall)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .indication(interactionSource, LocalIndication.current),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomNavigationPreview() {
    RecipesAppTheme {
        BottomNavigation(
            favoriteCount = 3,
            onCategoriesClick = {},
            onFavoriteClick = {}
        )
    }
}
