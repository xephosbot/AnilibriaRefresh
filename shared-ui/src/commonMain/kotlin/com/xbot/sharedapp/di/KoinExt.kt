package com.xbot.sharedapp.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.koin.compose.currentKoinScope
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

@Composable
internal inline fun <reified T> koinLazyInject(
    qualifier: Qualifier? = null,
    scope: Scope = currentKoinScope()
): Lazy<T> {
    return remember(qualifier, scope) {
        scope.inject(qualifier)
    }
}
