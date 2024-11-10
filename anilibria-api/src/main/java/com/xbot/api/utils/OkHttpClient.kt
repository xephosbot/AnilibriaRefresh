package com.xbot.api.utils

import okhttp3.OkHttpClient

internal fun OkHttpClient.Builder.conditional(
    condition: Boolean,
    builder: OkHttpClient.Builder.() -> OkHttpClient.Builder,
) = when (condition) {
    true -> builder()
    else -> this
}
