package com.xbot.player

import android.app.PendingIntent
import android.app.RemoteAction
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Rect
import android.graphics.drawable.Icon
import android.os.Build
import android.util.Log
import android.util.Rational
import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.RetainedEffect
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toAndroidRectF
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.core.app.PictureInPictureParamsCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRect
import androidx.core.pip.PictureInPictureDelegate.Event
import androidx.core.pip.PictureInPictureDelegate.OnPictureInPictureEventListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.xbot.sharedui.feature.player.impl.R
import io.github.kdroidfilter.composemediaplayer.VideoPlayerState
import kotlinx.coroutines.flow.combine

@Composable
actual fun rememberPictureInPictureController(player: VideoPlayerState): PictureInPictureController {
    if (LocalInspectionMode.current) {
        return PictureInPictureControllerStub()
    }

    val context = LocalContext.current
    val activity = context.findActivity<ComponentActivity>()

    val controller = remember(activity, player) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PictureInPictureControllerImpl(activity, player)
        } else {
            PictureInPictureControllerStub()
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        (controller as? PictureInPictureControllerImpl)?.let { impl ->
            LaunchedEffect(impl, player) {
                combine(
                    snapshotFlow { player.isPlaying },
                    snapshotFlow { player.aspectRatio },
                ) { _, _ ->
                    impl.updateParams()
                }.collect { }
            }
        }
    }

    if (controller.isInPictureInPictureMode) {
        DisposableEffect(player) {
            val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    if ((intent == null) || (intent.action != ACTION_BROADCAST_CONTROL)) {
                        return
                    }

                    when (intent.getIntExtra(EXTRA_CONTROL_TYPE, 0)) {
                        EXTRA_CONTROL_PAUSE -> player.pause()
                        EXTRA_CONTROL_PLAY -> player.play()
                        EXTRA_CONTROL_REPLAY -> player.seekBackward(10000L)
                        EXTRA_CONTROL_FORWARD -> player.seekForward(10000L)
                    }
                }
            }
            ContextCompat.registerReceiver(
                context,
                broadcastReceiver,
                IntentFilter(ACTION_BROADCAST_CONTROL),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            onDispose {
                context.unregisterReceiver(broadcastReceiver)
            }
        }

        LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
            player.pause()
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        RetainedEffect(controller) {
            onRetire {
                (controller as? PictureInPictureControllerImpl)?.close()
            }
        }
    }

    return controller
}

@RequiresApi(Build.VERSION_CODES.O)
internal class PictureInPictureControllerImpl(
    private val activity: ComponentActivity,
    private val playerState: VideoPlayerState,
) : PictureInPictureController, OnPictureInPictureEventListener {

    private val delegate = PictureInPictureDelegate(activity)

    private var _isInPictureInPictureMode by mutableStateOf(activity.isInPictureInPictureMode)
    override val isInPictureInPictureMode: Boolean get() = _isInPictureInPictureMode

    private var _isTransitioningToPip by mutableStateOf(false)
    override val isTransitioningToPip: Boolean get() = _isTransitioningToPip

    private var lastBounds: Rect? by mutableStateOf(null)

    init {
        delegate.addOnPictureInPictureEventListener(ContextCompat.getMainExecutor(activity), this)
    }

    override fun onPictureInPictureEvent(
        event: Event,
        config: Configuration?
    ) {
        Log.d(LOG_TAG, "Event: $event, config: $config")
        when (event) {
            Event.ENTERED -> _isInPictureInPictureMode = true
            Event.EXITED -> _isInPictureInPictureMode = false
            Event.ENTER_ANIMATION_START -> _isTransitioningToPip = true
            Event.ENTER_ANIMATION_END -> _isTransitioningToPip = false
            else -> {}
        }
    }

    fun updateParams() {
        updatePipParams(playerState.isPlaying, playerState.aspectRatio, lastBounds)
    }

    private fun updatePipParams(isPlaying: Boolean, aspectRatio: Float, bounds: Rect?) {
        Log.d(LOG_TAG, "Params changed - isPlaying: ${isPlaying}, aspectRatio: ${aspectRatio}, lastBounds: ${bounds?.toShortString()}")
        try {
            val params = buildPipParams(isPlaying, aspectRatio, bounds)
            delegate.setPictureInPictureParams(params)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Failed to update params", e)
        }
    }

    private fun buildPipParams(
        isPlaying: Boolean,
        aspectRatio: Float,
        bounds: Rect?
    ): PictureInPictureParamsCompat = PictureInPictureParamsCompat.Builder()
        .setSourceRectHint(bounds)
        .setAspectRatio(
            if (aspectRatio > 0) {
                Rational((aspectRatio * 100).toInt(), 100)
            } else {
                Rational(16, 9)
            }
        )
        .setActions(buildRemoteActions(isPlaying, activity))
        .setEnabled(isPlaying)
        .setSeamlessResizeEnabled(true)
        .build()

    override val modifier: Modifier = Modifier.onGloballyPositioned { coordinates ->
        lastBounds = coordinates.boundsInWindow().toAndroidRectF().toRect()
        updateParams()
    }

    override fun enterPictureInPictureMode() {
        try {
            val params = buildPipParams(playerState.isPlaying, playerState.aspectRatio, lastBounds)
            activity.enterPictureInPictureMode(params)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Failed to enter picture in picture mode", e)
        }
    }

    fun close() {
        delegate.removeOnPictureInPictureEventListener(this)
        updatePipParams(false, 0f, null)
    }

    private fun buildRemoteActions(
        isPlaying: Boolean,
        context: Context
    ): List<RemoteAction> {
        val actions = mutableListOf<RemoteAction>()

        actions.add(
            buildRemoteAction(
                iconResId = R.drawable.replay_10_24px,
                title = "Replay 10s",
                requestCode = REQUEST_REPLAY,
                controlType = EXTRA_CONTROL_REPLAY,
                context = context
            )
        )

        if (isPlaying) {
            actions.add(
                buildRemoteAction(
                    iconResId = R.drawable.pause_24px,
                    title = "Pause",
                    requestCode = REQUEST_PAUSE,
                    controlType = EXTRA_CONTROL_PAUSE,
                    context = context
                )
            )
        } else {
            actions.add(
                buildRemoteAction(
                    iconResId = R.drawable.play_arrow_24px,
                    title = "Play",
                    requestCode = REQUEST_PLAY,
                    controlType = EXTRA_CONTROL_PLAY,
                    context = context
                )
            )
        }

        actions.add(
            buildRemoteAction(
                iconResId = R.drawable.forward_10_24px,
                title = "Forward 10s",
                requestCode = REQUEST_FORWARD,
                controlType = EXTRA_CONTROL_FORWARD,
                context = context
            )
        )

        return actions
    }

    companion object {
        private const val LOG_TAG = "PictureInPicture"
    }
}

/**
 * Stub implementation for Android versions below O (API 26)
 */
private class PictureInPictureControllerStub : PictureInPictureController {
    override val modifier: Modifier = Modifier
    override val isInPictureInPictureMode: Boolean = false
    override val isTransitioningToPip: Boolean = false
    override fun enterPictureInPictureMode() {}
}

@RequiresApi(Build.VERSION_CODES.O)
private fun buildRemoteAction(
    @DrawableRes iconResId: Int,
    title: String,
    requestCode: Int,
    controlType: Int,
    context: Context
): RemoteAction {
    return RemoteAction(
        Icon.createWithResource(context, iconResId),
        title,
        title,
        PendingIntent.getBroadcast(
            context,
            requestCode,
            Intent(ACTION_BROADCAST_CONTROL)
                .setPackage(context.packageName)
                .putExtra(EXTRA_CONTROL_TYPE, controlType),
            PendingIntent.FLAG_IMMUTABLE
        )
    )
}

private fun VideoPlayerState.seekForward(amount: Long) {
    val durationSec = metadata.duration ?: 0
    if (durationSec > 0) {
        val delta = (amount.toFloat() / durationSec) * 1000f
        seekTo((sliderPos + delta).coerceIn(0f, 1000f))
    }
}

private fun VideoPlayerState.seekBackward(amount: Long) {
    val durationSec = metadata.duration ?: 0
    if (durationSec > 0) {
        val delta = (amount.toFloat() / durationSec) * 1000f
        seekTo((sliderPos - delta).coerceIn(0f, 1000f))
    }
}

private const val ACTION_BROADCAST_CONTROL = "broadcast_control"
private const val EXTRA_CONTROL_TYPE = "control_type"
private const val EXTRA_CONTROL_PLAY = 1
private const val EXTRA_CONTROL_PAUSE = 2
private const val EXTRA_CONTROL_REPLAY = 3
private const val EXTRA_CONTROL_FORWARD = 4
private const val REQUEST_PLAY = 1
private const val REQUEST_PAUSE = 2
private const val REQUEST_REPLAY = 3
private const val REQUEST_FORWARD = 4
