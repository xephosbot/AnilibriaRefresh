package com.xbot.player

import android.graphics.Rect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toAndroidRectF
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.core.app.PictureInPictureProvider
import androidx.core.graphics.toRect
import androidx.core.pip.BasicPictureInPicture
import java.util.concurrent.Executor

internal class BoundsTracker {
    fun interface OnBoundsChangedListener {
        fun onBoundsChanged(newBounds: Rect?)
    }

    private val listeners = mutableListOf<OnBoundsChangedListener>()
    private var lastBounds: Rect? = null

    val modifier: Modifier = Modifier.onGloballyPositioned { coordinates ->
        val newBounds = coordinates.boundsInWindow().toAndroidRectF().toRect()
        if (lastBounds != newBounds) {
            lastBounds = newBounds
            listeners.forEach { it.onBoundsChanged(newBounds) }
        }
    }

    fun addListener(listener: OnBoundsChangedListener) {
        listeners.add(listener)
        lastBounds?.let { listener.onBoundsChanged(it) }
    }

    fun removeListener(listener: OnBoundsChangedListener) {
        listeners.remove(listener)
    }

    fun release() {
        listeners.clear()
    }
}

internal class ComposePictureInPicture(
    private val provider: PictureInPictureProvider,
    executor: Executor
) : BasicPictureInPicture(provider, executor), AutoCloseable {

    private val boundsChangedListener: BoundsTracker.OnBoundsChangedListener =
        BoundsTracker.OnBoundsChangedListener { newBounds ->
            pictureInPictureParamsBuilder.setSourceRectHint(newBounds)
            commit()
        }

    private var boundsTracker: BoundsTracker? = null

    init {
        pictureInPictureParamsBuilder.setSeamlessResizeEnabled(true)
    }

    fun setBoundsTracker(tracker: BoundsTracker) {
        close()

        boundsTracker = tracker
        boundsTracker?.addListener(boundsChangedListener)
    }

    fun enter() {
        provider.enterPictureInPictureMode(pictureInPictureParamsBuilder.build())
    }

    override fun close() {
        boundsTracker?.removeListener(boundsChangedListener)
        boundsTracker?.release()
        boundsTracker = null
        pictureInPictureParamsBuilder.setSourceRectHint(null)
        commit()
    }
}
