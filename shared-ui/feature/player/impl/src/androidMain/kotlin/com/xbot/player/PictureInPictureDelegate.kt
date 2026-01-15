@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.xbot.player

import android.content.res.Configuration
import android.os.Build
import androidx.core.app.PictureInPictureModeChangedInfo
import androidx.core.app.PictureInPictureParamsCompat
import androidx.core.app.PictureInPictureProvider
import androidx.core.app.PictureInPictureUiStateCompat
import androidx.core.pip.PictureInPictureDelegate.Event
import androidx.core.pip.PictureInPictureDelegate.OnPictureInPictureEventListener
import androidx.core.pip.PictureInPictureParamsValidator
import androidx.core.util.Consumer
import java.lang.ref.WeakReference
import java.util.concurrent.Executor

/**
 * A delegate class to help setup PiP (Picture-in-Picture) functionalities on behalf of the given
 * [PictureInPictureProvider] instance.
 *
 * @param pictureInPictureProvider [PictureInPictureProvider] instance that this delegate will call
 *   into for actual Picture-in-Picture functionalities.
 */
class PictureInPictureDelegate(pictureInPictureProvider: PictureInPictureProvider) {

    private var pictureInPictureProviderRef: WeakReference<PictureInPictureProvider> =
        WeakReference(pictureInPictureProvider)

    private var pictureInPictureParamsCompat = PictureInPictureParamsCompat(isEnabled = false)

    /**
     * Explicitly calls [PictureInPictureProvider.enterPictureInPictureMode] on behalf of
     * [pictureInPictureProviderRef] if it's in PiP-able state and autoEnter is not available.
     */
    private val onUserLeaveHint: Runnable = Runnable {
        if (pictureInPictureParamsCompat.isEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // autoEnter is available, skip onUserLeaveHint callback.
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // autoEnter is not available, enter PiP with params.
                pictureInPictureProviderRef
                    .get()
                    ?.enterPictureInPictureMode(pictureInPictureParamsCompat)
            }
        }
    }

    private var lastReportedIsTransitioningToPip: Boolean = false
    private var lastReportedIsStashed: Boolean = false

    private val onPictureInPictureModeChanged: Consumer<PictureInPictureModeChangedInfo> =
        Consumer { pictureInPictureModeChangedInfo ->
            // Clear the internal trackers any time the Activity fully enters or exits PiP mode.

            //lastReportedIsTransitioningToPip = false
            lastReportedIsStashed = false
            val event =
                if (pictureInPictureModeChangedInfo.isInPictureInPictureMode) Event.ENTERED
                else Event.EXITED
            val config: Configuration? =
                if (Build.VERSION.SDK_INT >= 26) pictureInPictureModeChangedInfo.newConfig else null
            for ((listener, executor) in mOnPictureInPictureEventListeners) {
                executor.execute { listener.onPictureInPictureEvent(event, config) }
            }
        }

    private val mOnPictureInPictureUiEventChanged: Consumer<PictureInPictureUiStateCompat> =
        Consumer { pictureInPictureUiStateCompat ->
            var event: Event? = null
            if (lastReportedIsStashed && !pictureInPictureUiStateCompat.isStashed) {
                event = Event.UNSTASHED
            } else if (!lastReportedIsStashed && pictureInPictureUiStateCompat.isStashed) {
                event = Event.STASHED
            } else if (
                lastReportedIsTransitioningToPip &&
                !pictureInPictureUiStateCompat.isTransitioningToPip
            ) {
                event = Event.ENTER_ANIMATION_END
            } else if (
                !lastReportedIsTransitioningToPip &&
                pictureInPictureUiStateCompat.isTransitioningToPip
            ) {
                event = Event.ENTER_ANIMATION_START
            }
            if (event != null) {
                for ((listener, executor) in mOnPictureInPictureEventListeners) {
                    executor.execute { listener.onPictureInPictureEvent(event, null) }
                }
            }
            lastReportedIsStashed = pictureInPictureUiStateCompat.isStashed
            lastReportedIsTransitioningToPip = pictureInPictureUiStateCompat.isTransitioningToPip
        }

    private val mOnPictureInPictureEventListeners =
        mutableMapOf<OnPictureInPictureEventListener, Executor>()

    init {
        pictureInPictureProvider.addOnUserLeaveHintListener(onUserLeaveHint)
        pictureInPictureProvider.addOnPictureInPictureModeChangedListener(
            onPictureInPictureModeChanged
        )
        pictureInPictureProvider.addOnPictureInPictureUiStateChangedListener(
            mOnPictureInPictureUiEventChanged
        )
    }

    /**
     * Sets the [PictureInPictureParamsCompat] instance for PiP.
     *
     * @param pictureInPictureParamsCompat [PictureInPictureParamsCompat] instance to set, and it's
     *   subjected to be changed. For instance, the aspectRatio would be capped in between the
     *   minimal and maximum allowed aspectRatio; and the sourceRectHint would be center cropped to
     *   match the aspectRatio.
     */
    fun setPictureInPictureParams(
        pictureInPictureParamsCompat: PictureInPictureParamsCompat
    ) {
        val validatedParams = PictureInPictureParamsValidator.validate(pictureInPictureParamsCompat)
        this@PictureInPictureDelegate.pictureInPictureParamsCompat = validatedParams
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pictureInPictureProviderRef.get()?.setPictureInPictureParams(validatedParams)
        }
    }

    /** Adds [OnPictureInPictureEventListener] for events sent from system. */
    fun addOnPictureInPictureEventListener(
        executor: Executor,
        listener: OnPictureInPictureEventListener,
    ) {
        mOnPictureInPictureEventListeners[listener] = executor
    }

    /** Removes [OnPictureInPictureEventListener] for events sent from system. */
    fun removeOnPictureInPictureEventListener(listener: OnPictureInPictureEventListener) {
        mOnPictureInPictureEventListeners.remove(listener)
    }
}
