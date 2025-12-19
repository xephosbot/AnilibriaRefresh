package com.xbot.common.navigation

import androidx.compose.runtime.compositionLocalOf

interface Navigator {
    val backStack: List<NavKey>
    val currentDestination: NavKey?
    val currentTopLevelDestination: TopLevelNavKey?
    fun navigate(key: NavKey)
    fun navigateBack()
}

inline fun <reified T : NavKey> Navigator.replace(key: NavKey) {
    if (currentDestination == key) return
    if (currentDestination is T) {
        navigateBack()
    }
    navigate(key)
}

val LocalNavigator = compositionLocalOf<Navigator> { error("No navigator provided") }
