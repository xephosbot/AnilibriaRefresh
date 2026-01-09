package com.xbot.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.materialkolor.ktx.animateColorScheme
import com.materialkolor.rememberDynamicMaterialThemeState

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnilibriaDynamicTheme(
    seedColor: Color?,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val state = seedColor?.let {
        rememberDynamicMaterialThemeState(
            seedColor = it,
            isDark = darkTheme,
        )
    }

    val defaultScheme = MaterialTheme.colorScheme
    val colorScheme = state?.colorScheme ?: defaultScheme
    val scheme = animateColorScheme(
        colorScheme = colorScheme,
        animationSpec = { MaterialTheme.motionScheme.defaultEffectsSpec() }
    )

    MaterialTheme(
        colorScheme = scheme,
        content = content,
    )
}
