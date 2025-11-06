package com.xbot.common.navigation

interface Navigator {
    val backStack: List<NavKey>
    val currentDestination: NavKey?
    val currentTopLevelDestination: TopLevelNavKey?
    fun navigate(key: NavKey)
    fun navigateBack()
}