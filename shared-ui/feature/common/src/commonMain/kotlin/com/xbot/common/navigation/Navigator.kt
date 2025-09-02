package com.xbot.common.navigation

import androidx.compose.runtime.Composable

interface Navigator<T> {
    @get:Composable
    val backstack: List<T?>
    @get:Composable
    val currentDestination: T?
    @get:Composable
    val currentTopLevelDestination: T?
    fun navigate(destination: Any, singleTop: Boolean = false)
    fun navigateBack()
}