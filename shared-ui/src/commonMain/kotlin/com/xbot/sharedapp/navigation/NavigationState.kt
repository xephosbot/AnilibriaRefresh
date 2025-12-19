package com.xbot.sharedapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.serialization.NavBackStackSerializer
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import com.xbot.common.navigation.NavKey
import com.xbot.common.navigation.TopLevelNavKey
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.modules.SerializersModule

@Composable
internal fun rememberNavigationState(
    startRoute: TopLevelNavKey,
    topLevelRoutes: Set<TopLevelNavKey>,
    serializersModule: SerializersModule
): NavigationState {
    val topLevelRoute = rememberSerializable(
        startRoute, topLevelRoutes, serializersModule,
        configuration = SavedStateConfiguration { this.serializersModule = serializersModule },
        serializer = MutableStateSerializer(PolymorphicSerializer(NavKey::class))
    ) {
        mutableStateOf(startRoute)
    }

    // Create a back stack for each top level route.
    val backStacks = topLevelRoutes.associateWith { key ->
        rememberNavBackStack<NavKey>(
            configuration = SavedStateConfiguration { this.serializersModule = serializersModule },
            key
        )
    }

    return remember(startRoute, topLevelRoutes, backStacks, topLevelRoute) {
        @Suppress("UNCHECKED_CAST")
        NavigationState(
            startRoute = startRoute,
            topLevelRoute = topLevelRoute as MutableState<TopLevelNavKey>,
            backStacks = backStacks
        )
    }
}

@Composable
internal inline fun <reified T : NavKey> rememberNavBackStack(
    configuration: SavedStateConfiguration,
    vararg elements: T,
): NavBackStack<T> {
    require(configuration.serializersModule != SavedStateConfiguration.DEFAULT.serializersModule) {
        "You must pass a `SavedStateConfiguration.serializersModule` configured to handle " +
                "`NavKey` open polymorphism. Define it with: `polymorphic(NavKey::class) { ... }`"
    }
    return rememberSerializable(
        configuration = configuration,
        serializer = NavBackStackSerializer(PolymorphicSerializer(T::class)),
    ) {
        NavBackStack(*elements)
    }
}

internal class NavigationState(
    val startRoute: TopLevelNavKey,
    topLevelRoute: MutableState<TopLevelNavKey>,
    val backStacks: Map<TopLevelNavKey, NavBackStack<NavKey>>
) {
    var topLevelRoute: TopLevelNavKey by topLevelRoute
    val topLevelRoutesInUse: List<TopLevelNavKey>
        get() = if (topLevelRoute == startRoute) {
            listOf(startRoute)
        } else {
            listOf(startRoute, topLevelRoute)
        }
}
