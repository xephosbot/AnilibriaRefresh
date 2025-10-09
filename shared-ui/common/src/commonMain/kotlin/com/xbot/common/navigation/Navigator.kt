package com.xbot.common.navigation

interface Navigator<T : NavKey> {
    val backstack: List<T>
    val currentDestination: T?
    val currentTopLevelDestination: T?
    fun navigate(key: NavKey)
    fun navigateBack()
}