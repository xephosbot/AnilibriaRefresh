package com.xbot.designsystem.theme

import androidx.compose.material3.ExperimentalMaterial3ComponentOverrideApi
import androidx.compose.material3.LocalNavigationBarComponentOverride
import androidx.compose.material3.LocalNavigationRailComponentOverride
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.xbot.designsystem.components.AnilibriaNavigationBar
import com.xbot.designsystem.components.AnilibriaNavigationRail

@OptIn(ExperimentalMaterial3ComponentOverrideApi::class)
@Composable
actual fun AnilibriaTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable (() -> Unit)
) {
    val colorScheme = if (darkTheme) darkScheme else lightScheme

    CompositionLocalProvider(
        LocalNavigationBarComponentOverride provides AnilibriaNavigationBar,
        LocalNavigationRailComponentOverride provides AnilibriaNavigationRail
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AnilibriaTypography(),
            content = content,
        )
    }
}
