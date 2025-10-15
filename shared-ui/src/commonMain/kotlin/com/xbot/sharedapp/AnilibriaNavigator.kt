package com.xbot.sharedapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.xbot.common.navigation.NavKey
import com.xbot.common.navigation.Navigator
import com.xbot.common.navigation.TopLevelNavKey
import com.xbot.favorite.navigation.FavoriteRoute
import com.xbot.home.navigation.HomeRoute
import com.xbot.preference.navigation.PreferenceRoute

@Composable
internal fun rememberAnilibriaNavigator(
    startNavKey: NavKey = HomeRoute,
): AnilibriaNavigator {
    return remember(startNavKey) { AnilibriaNavigator(startNavKey) }
}

internal class AnilibriaNavigator(startNavKey: NavKey) : Navigator<NavKey> {

    private val topLevelStacks : LinkedHashMap<NavKey, SnapshotStateList<NavKey>> = linkedMapOf(
        startNavKey to mutableStateListOf(startNavKey)
    )

    private val _backstack = mutableStateListOf(startNavKey)
    override val backstack: List<NavKey> get() = _backstack

    override val currentDestination: NavKey?
        get() = _backstack.lastOrNull()

    private var _currentTopLevelDestination: NavKey by mutableStateOf(startNavKey)

    override val currentTopLevelDestination: NavKey?
        get() = _currentTopLevelDestination

    private fun updateBackStack() =
        _backstack.apply {
            clear()
            addAll(topLevelStacks.flatMap { it.value })
        }

    override fun navigate(key: NavKey) {
        when (key) {
            is TopLevelNavKey -> {
                // If the top level doesn't exist, add it
                if (topLevelStacks[key] == null){
                    topLevelStacks.put(key, mutableStateListOf(key))
                } else {
                    // Otherwise just move it to the end of the stacks
                    topLevelStacks.apply {
                        remove(key)?.let {
                            put(key, it)
                        }
                    }
                }
                _currentTopLevelDestination = key
                updateBackStack()
            }
            else -> {
                topLevelStacks[_currentTopLevelDestination]?.add(key)
                updateBackStack()
            }
        }
    }

    override fun navigateBack() {
        val removedKey = topLevelStacks[_currentTopLevelDestination]?.removeLastOrNull()
        // If the removed key was a top level key, remove the associated top level stack
        topLevelStacks.remove(removedKey)
        _currentTopLevelDestination = topLevelStacks.keys.last()
        updateBackStack()
    }

    companion object {
        val topLevelDestinations: List<TopLevelNavKey> = listOf(HomeRoute, FavoriteRoute, PreferenceRoute)
    }
}