/*
 * Copyright (C) 2024-2025 OpenAni and contributors.
 *
 * Use of this source code is governed by the GNU GENERAL PUBLIC LICENSE version 3 license, which can be found at the following link.
 *
 * https://github.com/open-ani/mediamp/blob/main/LICENSE
 */

package org.openani.mediamp.vlc

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.ImageInfo
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.embedded.videosurface.CallbackVideoSurface
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurface
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapters
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.format.RV32BufferFormat
import java.nio.ByteBuffer
import javax.swing.SwingUtilities

class SkiaBitmapVideoSurface : VideoSurface(VideoSurfaceAdapters.getVideoSurfaceAdapter()) {

    private val _enableRendering = MutableStateFlow(false)
    private val _composeBitmap = mutableStateOf<ImageBitmap?>(null)
    private val allowedDrawFrames = atomic(0)

    private val videoSurface = SkiaBitmapVideoSurface()

    private val skiaBitmap = Bitmap()
    private lateinit var imageInfo: ImageInfo
    private lateinit var frameBytes: ByteArray

    val enableRendering = _enableRendering.asStateFlow()
    val bitmap: ImageBitmap? by _composeBitmap

    fun setRenderingEnabled(enabled: Boolean) {
        _enableRendering.value = enabled
    }

    fun setAllowedDrawFrames(count: Int) {
        allowedDrawFrames.value = count
    }

    fun clearBitmap() {
        _composeBitmap.value = null
    }

    override fun attach(mediaPlayer: MediaPlayer) {
        videoSurface.attach(mediaPlayer)
    }

    private inner class SkiaBitmapBufferFormatCallback : BufferFormatCallback {
        private var sourceWidth: Int = 0
        private var sourceHeight: Int = 0

        override fun getBufferFormat(sourceWidth: Int, sourceHeight: Int): BufferFormat {
            this.sourceWidth = sourceWidth
            this.sourceHeight = sourceHeight
            return RV32BufferFormat(sourceWidth, sourceHeight)
        }

        override fun newFormatSize(
            bufferWidth: Int,
            bufferHeight: Int,
            displayWidth: Int,
            displayHeight: Int
        ) {
            // Not implemented yet, but could handle resizing logic
        }

        override fun allocatedBuffers(buffers: Array<ByteBuffer>) {
            frameBytes = buffers[0].run { ByteArray(remaining()).also(::get) }
            imageInfo = ImageInfo(
                sourceWidth,
                sourceHeight,
                ColorType.BGRA_8888,
                ColorAlphaType.PREMUL,
            )
        }
    }

    private inner class SkiaBitmapRenderCallback : RenderCallback {
        override fun display(
            mediaPlayer: MediaPlayer,
            nativeBuffers: Array<ByteBuffer>,
            bufferFormat: BufferFormat,
            displayWidth: Int,
            displayHeight: Int
        ) {
            val allowedDrawFramesValue = allowedDrawFrames.value

            // Skip rendering if disabled and no allowed frames remain
            if (!enableRendering.value) {
                if (allowedDrawFramesValue <= 0) {
                    return
                }
                if (allowedDrawFrames.decrementAndGet() < 0) return
            }

            SwingUtilities.invokeLater {
                nativeBuffers[0].rewind()
                nativeBuffers[0].get(frameBytes)
                skiaBitmap.installPixels(imageInfo, frameBytes, bufferFormat.width * 4)
                _composeBitmap.value = skiaBitmap.asComposeImageBitmap()
            }
        }

        override fun lock(mediaPlayer: MediaPlayer?) {
            // Not implemented yet
        }

        override fun unlock(mediaPlayer: MediaPlayer?) {
            // Not implemented yet
        }
    }

    private inner class SkiaBitmapVideoSurface : CallbackVideoSurface(
        SkiaBitmapBufferFormatCallback(),
        SkiaBitmapRenderCallback(),
        true,
        videoSurfaceAdapter,
    )
}