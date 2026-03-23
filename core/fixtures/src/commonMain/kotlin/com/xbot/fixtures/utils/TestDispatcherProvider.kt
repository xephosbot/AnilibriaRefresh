package com.xbot.fixtures.utils

import com.xbot.domain.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Singleton

@Singleton
class TestDispatcherProvider(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : DispatcherProvider {
    override val main: CoroutineDispatcher get() = dispatcher
    override val io: CoroutineDispatcher get() = dispatcher
    override val default: CoroutineDispatcher get() = dispatcher
    override val unconfined: CoroutineDispatcher get() = Dispatchers.Unconfined
}
