package com.xbot.network.utils

import io.ktor.client.plugins.compression.ContentEncodingConfig

actual fun ContentEncodingConfig.brotli(quality: Float?) {
    //error("There is no available Brotli implementation for non JVM")
}