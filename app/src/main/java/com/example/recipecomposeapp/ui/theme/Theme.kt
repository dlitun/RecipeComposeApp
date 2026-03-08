package com.example.recipecomposeapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val RecipesAppLightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = SurfaceColor,
    primaryContainer = PrimaryColor,
    onPrimaryContainer = SurfaceColor,
    secondary = PrimaryColor,
    onSecondary = SurfaceColor,
    tertiary = AccentBlue,
    onTertiary = SurfaceColor,
    tertiaryContainer = SliderTrackColor,
    onTertiaryContainer = TextPrimaryColor,
    error = AccentColor,
    onError = SurfaceColor,
    background = BackgroundColor,
    onBackground = TextPrimaryColor,
    surface = SurfaceColor,
    onSurface = TextPrimaryColor,
    surfaceVariant = SurfaceVariantColor,
    onSurfaceVariant = TextSecondaryColor,
    outline = DividerColor
)

val RecipesAppDarkColorScheme = darkColorScheme(
    primary = PrimaryColorDark,
    onPrimary = BackgroundColorDark,
    primaryContainer = PrimaryColorDark,
    onPrimaryContainer = BackgroundColorDark,
    secondary = PrimaryColorDark,
    onSecondary = BackgroundColorDark,
    tertiary = AccentBlueDark,
    onTertiary = BackgroundColorDark,
    tertiaryContainer = SliderTrackColorDark,
    onTertiaryContainer = TextPrimaryColorDark,
    error = AccentColorDark,
    onError = BackgroundColorDark,
    background = BackgroundColorDark,
    onBackground = TextPrimaryColorDark,
    surface = SurfaceColorDark,
    onSurface = TextPrimaryColorDark,
    surfaceVariant = SurfaceVariantColorDark,
    onSurfaceVariant = TextSecondaryColorDark,
    outline = DividerColorDark
)

@Composable
fun RecipesAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        RecipesAppDarkColorScheme
    } else {
        RecipesAppLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}