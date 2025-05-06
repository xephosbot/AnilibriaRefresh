package com.xbot.api.utils

import io.ktor.client.plugins.compression.ContentEncodingConfig
import io.ktor.util.ContentEncoder
import io.ktor.util.Encoder
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.jvm.javaio.toByteReadChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import org.brotli.dec.BrotliInputStream
import kotlin.coroutines.CoroutineContext

internal val Brotli: Encoder = object : Encoder {
    override fun encode(source: ByteReadChannel, coroutineContext: CoroutineContext): ByteReadChannel =
        error("BrotliOutputStream not available (<https://github.com/google/brotli/issues/715>)")

    override fun encode(source: ByteWriteChannel, coroutineContext: CoroutineContext): ByteWriteChannel =
        error("BrotliOutputStream not available (<https://github.com/google/brotli/issues/715>)")

    override fun decode(source: ByteReadChannel, coroutineContext: CoroutineContext): ByteReadChannel =
        BrotliInputStream(source.toInputStream()).toByteReadChannel()
}

internal object BrotliEncoder : ContentEncoder, Encoder by Brotli {
    override val name: String = "br"
}

actual fun ContentEncodingConfig.brotli(quality: Float?) {
    customEncoder(BrotliEncoder, quality)
}