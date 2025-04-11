package com.xbot.shared.data.sources.remote.utils

import io.ktor.client.plugins.compression.ContentEncodingConfig

expect fun ContentEncodingConfig.brotli(quality: Float? = null)