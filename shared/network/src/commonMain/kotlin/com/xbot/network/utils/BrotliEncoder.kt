package com.xbot.network.utils

import io.ktor.client.plugins.compression.ContentEncodingConfig

expect fun ContentEncodingConfig.brotli(quality: Float? = null)