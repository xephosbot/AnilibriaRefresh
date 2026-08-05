package com.xbot.player

import android.app.PictureInPictureParams
import android.graphics.Rect
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toAndroidRectF
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
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

@RequiresApi(Build.VERSION_CODES.O)
internal class ComposePictureInPicture(
    private val activity: ComponentActivity,
    executor: Executor
) : BasicPictureInPicture(activity, executor), AutoCloseable {

    private val boundsChangedListener: BoundsTracker.OnBoundsChangedListener =
        BoundsTracker.OnBoundsChangedListener { newBounds ->
            if (newBounds == null || !activity.isInPictureInPictureMode) {
                pictureInPictureParamsBuilder.setSourceRectHint(newBounds)
                commit()
            }
        }

    private var boundsTracker: BoundsTracker? = null

    init {
        pictureInPictureParamsBuilder.setSeamlessResizeEnabled(true)
    }

    fun setBoundsTracker(tracker: BoundsTracker) {
        boundsTracker = tracker
        boundsTracker?.addListener(boundsChangedListener)
    }

    fun enter() {
        setPictureInPictureParams(pictureInPictureParamsBuilder.build())
        activity.enterPictureInPictureMode(PictureInPictureParams.Builder().build())
    }

    override fun close() {
        boundsChangedListener.onBoundsChanged(null)
        boundsTracker?.release()
        boundsTracker = null
    }
}
