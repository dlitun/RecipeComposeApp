package com.example.recipecomposeapp.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipecomposeapp.R
import com.example.recipecomposeapp.core.ui.theme.Dimens
import com.example.recipecomposeapp.core.ui.theme.RecipesAppTheme

@Composable
fun ScreenHeader(
    painter: Painter,
    contentDescription: String,
    text: String,
    modifier: Modifier = Modifier,
    showShareButton: Boolean = false,
    onShareClick: (() -> Unit)? = null,
    showFavoriteButton: Boolean = false,
    isFavorite: Boolean = false,
    onFavoriteToggle: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.HeaderHeight)
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(Dimens.Space16),
            color = MaterialTheme.colorScheme.background,
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = text.uppercase(),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(Dimens.Space10)
                )
        }

        if (showFavoriteButton) {
            IconButton(
                onClick = onFavoriteToggle,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(Dimens.Space16)
            ) {
                Crossfade(
                    targetState = isFavorite,
                    animationSpec = tween(durationMillis = 300),
                    label = "favorite_animation"
                ) { isCurrentlyFavorite ->
                    val heartIcon = rememberVectorPainter(
                        image = ImageVector.vectorResource(
                            id = if (isCurrentlyFavorite) R.drawable.ic_heart else R.drawable.ic_heart_empty
                        )
                    )

                    Icon(
                        painter = heartIcon,
                        contentDescription = "Favorite",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        if (showShareButton && onShareClick != null) {
            TextButton(
                onClick = onShareClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(Dimens.Space16)
            ) {
                Text(text = "Поделиться")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenHeaderPreview() {
    RecipesAppTheme {
        ScreenHeader(
            painter = painterResource(id = R.drawable.placeholder_header),
            contentDescription = "Превью заголовка",
            text = "Категории"
        )
    }
}
