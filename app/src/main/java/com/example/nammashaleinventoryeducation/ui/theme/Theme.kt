package com.example.nammashaleinventoryeducation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Blue50,
    onPrimary = Color.White,
    primaryContainer = Blue90,
    onPrimaryContainer = Blue10,
    secondary = Green40,
    onSecondary = Color.White,
    secondaryContainer = Green90,
    onSecondaryContainer = Green10,
    tertiary = Amber40,
    onTertiary = Color.White,
    tertiaryContainer = Amber90,
    onTertiaryContainer = Color(0xFF3B2600),
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Color(0xFF410002),
    background = Neutral99,
    onBackground = Neutral10,
    surface = Color.White,
    onSurface = Neutral10,
    surfaceVariant = Neutral95,
    onSurfaceVariant = Neutral40,
    outline = Neutral70,
    outlineVariant = Neutral90
)

private val DarkColorScheme = darkColorScheme(
    primary = Blue70,
    onPrimary = Blue20,
    primaryContainer = Blue30,
    onPrimaryContainer = Blue90,
    secondary = Green60,
    onSecondary = Green20,
    secondaryContainer = Green30,
    onSecondaryContainer = Green90,
    tertiary = Amber60,
    onTertiary = Color(0xFF3B2600),
    tertiaryContainer = Color(0xFF5C3D00),
    onTertiaryContainer = Amber90,
    error = Red60,
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Red90,
    background = Neutral10,
    onBackground = Neutral90,
    surface = Color(0xFF1A1A1A),
    onSurface = Neutral90,
    surfaceVariant = Neutral30,
    onSurfaceVariant = Neutral70,
    outline = Neutral50,
    outlineVariant = Neutral30
)

@Composable
fun NammaShaleInventoryEducationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
