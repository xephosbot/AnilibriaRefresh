package com.xbot.designsystem.theme

import androidx.compose.animation.core.spring
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.kmpalette.palette.graphics.Palette
import com.materialkolor.ktx.animateColorScheme
import com.materialkolor.rememberDynamicMaterialThemeState

@Composable
fun AnilibriaDynamicTheme(
    palette: Palette?,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val seedColor = remember(palette) {
        palette?.dominantSwatch?.let { Color(it.rgb) }
            ?: palette?.vibrantSwatch?.let { Color(it.rgb) }
            ?: palette?.mutedSwatch?.let { Color(it.rgb) }
    }

    val state = seedColor?.let {
        rememberDynamicMaterialThemeState(
            seedColor = it,
            isDark = darkTheme,
        )
    }

    val defaultScheme = MaterialTheme.colorScheme
    val colorScheme = state?.colorScheme ?: defaultScheme
    val scheme = animateColorScheme(colorScheme = colorScheme, animationSpec = { spring() })

    MaterialTheme(
        colorScheme = scheme,
        content = content,
    )
}
