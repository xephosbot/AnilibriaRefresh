package com.xbot.sharedapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.xbot.common.navigation.NavKey
import com.xbot.common.navigation.Navigator
import com.xbot.common.navigation.TopLevelNavKey
import com.xbot.favorite.navigation.FavoriteRoute
import com.xbot.home.navigation.HomeRoute
import com.xbot.preference.navigation.PreferenceRoute

internal class AnilibriaNavigator(
    private val startNavKey: TopLevelNavKey
) : Navigator {

    private val topLevelStacks = linkedMapOf<TopLevelNavKey, SnapshotStateList<NavKey>>(
        startNavKey to mutableStateListOf(startNavKey)
    )

    private var _currentTopLevelDestination by mutableStateOf(startNavKey)
    override val currentTopLevelDestination: TopLevelNavKey get() = _currentTopLevelDestination

    private val _backStack = mutableStateListOf<NavKey>(startNavKey)
    override val backStack: List<NavKey> get() = _backStack

    override val currentDestination: NavKey? get() = _backStack.lastOrNull()

    override fun navigate(key: NavKey) {
        when (key) {
            is TopLevelNavKey -> addTopLevel(key)
            else -> add(key)
        }
        updateBackStack()
    }

    private fun addTopLevel(key: TopLevelNavKey) {
        if (key == startNavKey) {
            clearAllExceptStartStack()
        } else {
            // Get the existing stack or create a new one.
            val topLevelStack: SnapshotStateList<NavKey> = topLevelStacks.remove(key) ?: mutableStateListOf(key)
            clearAllExceptStartStack()

            topLevelStacks.put(key, topLevelStack)
        }
        _currentTopLevelDestination = key
    }

    private fun add(key: NavKey) {
        topLevelStacks[currentTopLevelDestination]?.add(key)
    }

    private fun clearAllExceptStartStack() {
        // Remove all other top level stacks, except the start stack
        val startStack: SnapshotStateList<NavKey> = topLevelStacks[startNavKey] ?: mutableStateListOf(startNavKey)
        topLevelStacks.clear()
        topLevelStacks.put(startNavKey, startStack)
    }

    override fun navigateBack() {
        if (backStack.size <= 1) {
            return
        }
        val removedKey = topLevelStacks[_currentTopLevelDestination]?.removeLastOrNull()
        // If the removed key was a top level key, remove the associated top level stack
        topLevelStacks.remove(removedKey)
        _currentTopLevelDestination = topLevelStacks.keys.last()
        updateBackStack()
    }

    private fun updateBackStack() =
        _backStack.apply {
            clear()
            addAll(topLevelStacks.values.flatten())
        }

    companion object {
        val topLevelDestinations: List<TopLevelNavKey> = listOf(HomeRoute, FavoriteRoute, PreferenceRoute)
    }
}