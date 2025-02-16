package com.xbot.common.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy


// https://issuetracker.google.com/issues/360354551
@SuppressLint("RestrictedApi")
fun NavDestination?.hasRoute(route: Any) =
    this?.hierarchy?.any { it.hasRoute(route::class) } == true

/**
 * Это временное решение, пока не будет доступен публичный API.
 * Используется внутренний API Jetpack Compose Navigations, отсюда и подавление предупреждений.
 *
 * @return состояние, представляющее currentBackStack.
 */
@SuppressLint("RestrictedApi")
@Composable
fun NavController.currentBackStackAsState(): State<List<NavBackStackEntry>?> {
    return currentBackStack.collectAsState(null)
}

/**
 * Это обходной путь для устранения дублирования событий навигации.
 * @see <a href="https://github.com/android/compose-samples/blob/081721ad44dfb29b55b1bc34f83d693b6b8dc9dd/Jetsnack/app/src/main/java/com/example/jetsnack/ui/JetsnackAppState.kt#L141-L147">Jetsnack Sample</a>
 */
fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED
