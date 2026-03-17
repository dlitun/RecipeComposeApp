package com.example.recipecomposeapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import com.example.recipecomposeapp.ui.theme.Dimens

@Composable
fun ScreenHeader(
    painter: Painter,
    contentDescription: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.height(Dimens.HeaderHeight)
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(Dimens.Space16),
            tonalElevation = (Dimens.ElevationMedium),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(
                    horizontal = (Dimens.Space16),
                    vertical = (Dimens.Space8)
                )
            )
        }
    }
}