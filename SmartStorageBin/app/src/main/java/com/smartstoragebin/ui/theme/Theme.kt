package com.smartstoragebin.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    error = md_theme_light_error,
    onError = md_theme_light_onError,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
)

private val DarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
)

data class CustomColors(
    val statusActive: Color,
    val statusExpiring: Color,
    val statusExpired: Color,
    val categoryDocuments: Color,
    val categoryImages: Color,
    val categoryVideos: Color,
    val categoryAudio: Color,
    val categoryArchives: Color,
    val categoryApps: Color,
    val categoryOther: Color
)

val LocalCustomColors = staticCompositionLocalOf {
    CustomColors(
        statusActive = Color.Unspecified,
        statusExpiring = Color.Unspecified,
        statusExpired = Color.Unspecified,
        categoryDocuments = Color.Unspecified,
        categoryImages = Color.Unspecified,
        categoryVideos = Color.Unspecified,
        categoryAudio = Color.Unspecified,
        categoryArchives = Color.Unspecified,
        categoryApps = Color.Unspecified,
        categoryOther = Color.Unspecified
    )
}

@Composable
fun SmartStorageBinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    val customColors = CustomColors(
        statusActive = StatusActiveColor,
        statusExpiring = StatusExpiringColor,
        statusExpired = StatusExpiredColor,
        categoryDocuments = CategoryDocumentsColor,
        categoryImages = CategoryImagesColor,
        categoryVideos = CategoryVideosColor,
        categoryAudio = CategoryAudioColor,
        categoryArchives = CategoryArchivesColor,
        categoryApps = CategoryAppsColor,
        categoryOther = CategoryOtherColor
    )

    CompositionLocalProvider(LocalCustomColors provides customColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}
