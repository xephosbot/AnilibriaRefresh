package com.xbot.network.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

interface NetworkObserver {
    val isConnected: Flow<Boolean>
}

internal suspend fun NetworkObserver.awaitConnection() {
    isConnected.filter { it }.first()
}
