package com.example.recipecomposeapp.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.compose.rememberConstraintsSizeResolver
import coil.request.ImageRequest
import com.example.recipecomposeapp.R

@Composable
fun RecipeImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    val sizeResolver = rememberConstraintsSizeResolver()
    var isLoading by remember(imageUrl) { mutableStateOf(false) }
    val imageRequest = remember(context, imageUrl, sizeResolver) {
        ImageRequest.Builder(context)
            .data(imageUrl)
            .size(sizeResolver)
            .crossfade(true)
            .build()
    }

    Box(
        modifier = modifier.then(sizeResolver),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = imageRequest,
            contentDescription = contentDescription,
            placeholder = painterResource(id = R.drawable.placeholder_header),
            error = painterResource(id = R.drawable.placeholder_error),
            contentScale = contentScale,
            modifier = Modifier.fillMaxSize(),
            onLoading = { isLoading = true },
            onSuccess = { isLoading = false },
            onError = { isLoading = false }
        )

        if (isLoading) {
            CircularProgressIndicator()
        }
    }
}
