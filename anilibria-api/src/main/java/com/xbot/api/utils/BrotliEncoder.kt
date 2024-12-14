package com.xbot.api.utils

import io.ktor.client.plugins.compression.*
import io.ktor.util.*
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import org.brotli.dec.BrotliInputStream
import kotlin.coroutines.CoroutineContext

val Brotli: Encoder = object : Encoder {
    override fun encode(source: ByteReadChannel, coroutineContext: CoroutineContext): ByteReadChannel =
        error("BrotliOutputStream not available (<https://github.com/google/brotli/issues/715>)")

    override fun encode(source: ByteWriteChannel, coroutineContext: CoroutineContext): ByteWriteChannel =
        error("BrotliOutputStream not available (<https://github.com/google/brotli/issues/715>)")

    override fun decode(source: ByteReadChannel, coroutineContext: CoroutineContext): ByteReadChannel =
        BrotliInputStream(source.toInputStream()).toByteReadChannel()
}

object BrotliEncoder : ContentEncoder, Encoder by Brotli {
    override val name: String = "br"
}

fun ContentEncodingConfig.brotli(quality: Float? = null) {
    customEncoder(BrotliEncoder, quality)
}