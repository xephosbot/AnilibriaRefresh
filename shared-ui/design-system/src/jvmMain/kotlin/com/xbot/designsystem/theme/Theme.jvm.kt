package com.xbot.designsystem.theme

import androidx.compose.material3.ExperimentalMaterial3ComponentOverrideApi
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LocalNavigationBarOverride
import androidx.compose.material3.LocalNavigationRailOverride
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.xbot.designsystem.components.AnilibriaNavigationBar
import com.xbot.designsystem.components.AnilibriaNavigationRail

@OptIn(ExperimentalMaterial3ComponentOverrideApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
actual fun AnilibriaTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable (() -> Unit)
) {
    val colorScheme = if (darkTheme) darkScheme else lightScheme

    CompositionLocalProvider(
        LocalNavigationBarOverride provides AnilibriaNavigationBar,
        LocalNavigationRailOverride provides AnilibriaNavigationRail
    ) {
        MaterialExpressiveTheme(
            colorScheme = colorScheme,
            motionScheme = MotionScheme.expressive(),
            typography = AnilibriaTypography(),
            content = content,
        )
    }
}