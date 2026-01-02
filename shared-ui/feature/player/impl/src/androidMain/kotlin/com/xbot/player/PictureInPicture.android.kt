package com.xbot.player

import android.app.PendingIntent
import android.app.PictureInPictureParams
import android.app.PictureInPictureUiState
import android.app.RemoteAction
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.retain.RetainedEffect
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toAndroidRectF
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.PictureInPictureModeChangedInfo
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRect
import androidx.core.util.Consumer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import io.github.kdroidfilter.composemediaplayer.VideoPlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn

@Composable
fun rememberIsInPipMode(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val activity = LocalContext.current.findActivity<ComponentActivity>()
        var pipMode by remember { mutableStateOf(activity.isInPictureInPictureMode) }
        DisposableEffect(activity) {
            val observer = Consumer<PictureInPictureModeChangedInfo> { info ->
                pipMode = info.isInPictureInPictureMode
            }
            activity.addOnPictureInPictureModeChangedListener(observer)
            onDispose {
                activity.removeOnPictureInPictureModeChangedListener(observer)
            }
        }
        return pipMode
    } else {
        return false
    }
}

@Composable
fun rememberIsTransitioningToPip(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
        val activity = LocalContext.current.findActivity<ComponentActivity>()
        var uiState by remember { mutableStateOf<PictureInPictureUiState?>(null) }
        DisposableEffect(activity) {
            val observer = Consumer<PictureInPictureUiState> { state ->
                uiState = state
            }
            (activity as? OnPictureInPictureUiStateChangedProvider)?.addOnPictureInPictureUiStateChangedListener(observer)
            onDispose {
                (activity as? OnPictureInPictureUiStateChangedProvider)?.removeOnPictureInPictureUiStateChangedListener(observer)
            }
        }
        return uiState?.isTransitioningToPip ?: false
    } else {
        return false
    }
}

@Composable
actual fun rememberPictureInPictureController(player: VideoPlayerState): PictureInPictureController {
    val context = LocalContext.current
    val activity = context.findActivity<ComponentActivity>()

    val isInPipMode = rememberIsInPipMode()
    val currentIsInPipMode = rememberUpdatedState(isInPipMode)

    val isTransitioningToPip = rememberIsTransitioningToPip()
    val currentIsTransitioningToPip = rememberUpdatedState(isTransitioningToPip)

    val controller = remember(activity, player) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PictureInPictureControllerImpl(activity, player, currentIsInPipMode, currentIsTransitioningToPip)
        } else {
            PictureInPictureControllerStub()
        }
    }

    if (isInPipMode) {
        DisposableEffect(player) {
            val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    if ((intent == null) || (intent.action != ACTION_BROADCAST_CONTROL)) {
                        return
                    }

                    when (intent.getIntExtra(EXTRA_CONTROL_TYPE, 0)) {
                        EXTRA_CONTROL_PAUSE -> player.pause()
                        EXTRA_CONTROL_PLAY -> player.play()
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

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
        DisposableEffect(activity, controller) {
            val onUserLeaveHint = Runnable {
                controller.enterPictureInPictureMode()
            }
            activity.addOnUserLeaveHintListener(onUserLeaveHint)
            onDispose {
                activity.removeOnUserLeaveHintListener(onUserLeaveHint)
            }
        }
    }

    RetainedEffect(controller) {
        onRetire {
            (controller as? AutoCloseable)?.close()
        }
    }

    return controller
}

@RequiresApi(Build.VERSION_CODES.O)
internal class PictureInPictureControllerImpl(
    private val activity: ComponentActivity,
    private val playerState: VideoPlayerState,
    private val pipModeState: State<Boolean>,
    private val pipModeTransition: State<Boolean>,
) : PictureInPictureController, AutoCloseable {

    override val isInPictureInPictureMode: Boolean
        get() = pipModeState.value

    override val isTransitioningToPip: Boolean
        get() = pipModeTransition.value

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var lastBounds: Rect? by mutableStateOf(null)

    init {
        startObserving()
    }

    private fun startObserving() {
        combine(
            snapshotFlow { playerState.isPlaying },
            snapshotFlow { playerState.aspectRatio },
        ) { isPlaying, aspectRatio ->
            updatePipParams(isPlaying, aspectRatio, lastBounds)
        }.launchIn(scope)
    }

    private fun updatePipParams(isPlaying: Boolean, aspectRatio: Float, bounds: Rect?) {
        Log.d(LOG_TAG, "Params changed - isPlaying: ${isPlaying}, aspectRatio: ${aspectRatio}, lastBounds: ${bounds?.toShortString()}")
        try {
            activity.setPictureInPictureParams(buildPipParams(isPlaying, aspectRatio, bounds))
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Failed to update params", e)
        }
    }

    private fun buildPipParams(
        isPlaying: Boolean,
        aspectRatio: Float,
        bounds: Rect?
    ): PictureInPictureParams = PictureInPictureParams.Builder()
        .setSourceRectHint(bounds?.calculateVideoRect(aspectRatio))
        .apply {
            val rational = if (aspectRatio > 0) {
                Rational((aspectRatio * 100).toInt(), 100)
            } else {
                Rational(16, 9)
            }
            setAspectRatio(rational)
        }
        .setActions(buildRemoteActions(isPlaying, activity))
        .apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                setAutoEnterEnabled(isPlaying)
                setSeamlessResizeEnabled(true)
            }
        }
        .build()

    override val modifier: Modifier = Modifier.onGloballyPositioned { coordinates ->
        lastBounds = coordinates.boundsInWindow().toAndroidRectF().toRect()
        updatePipParams(playerState.isPlaying, playerState.aspectRatio, lastBounds)
    }

    override fun enterPictureInPictureMode() {
        try {
            val params = buildPipParams(playerState.isPlaying, playerState.aspectRatio, lastBounds)
            activity.enterPictureInPictureMode(params)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Failed to enter picture in picture mode", e)
        }
    }

    override fun close() {
        scope.cancel()
        updatePipParams(false, 0f, null)
    }

    private fun buildRemoteActions(
        isPlaying: Boolean,
        context: Context
    ): List<RemoteAction> {
        return listOf(
            if (isPlaying) {
                buildRemoteAction(
                    iconResId = android.R.drawable.ic_media_pause,
                    title = "Pause",
                    requestCode = REQUEST_PAUSE,
                    controlType = EXTRA_CONTROL_PAUSE,
                    context = context
                )
            } else {
                buildRemoteAction(
                    iconResId = android.R.drawable.ic_media_play,
                    title = "Play",
                    requestCode = REQUEST_PLAY,
                    controlType = EXTRA_CONTROL_PLAY,
                    context = context
                )
            }
        )
    }

    private fun Rect.calculateVideoRect(videoAspectRatio: Float): Rect {
        val containerWidth = this.width()
        val containerHeight = this.height()
        val containerAspectRatio = containerWidth / containerHeight

        var videoWidth = containerWidth.toFloat()
        var videoHeight = containerHeight.toFloat()

        if (containerAspectRatio > videoAspectRatio) {
            videoWidth = containerHeight * videoAspectRatio
        } else {
            videoHeight = containerWidth / videoAspectRatio
        }

        val left = this.left + (containerWidth - videoWidth) / 2
        val top = this.top + (containerHeight - videoHeight) / 2

        return Rect(
            left.toInt(),
            top.toInt(),
            (left + videoWidth).toInt(),
            (top + videoHeight).toInt()
        )
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

private const val ACTION_BROADCAST_CONTROL = "broadcast_control"
private const val EXTRA_CONTROL_TYPE = "control_type"
private const val EXTRA_CONTROL_PLAY = 1
private const val EXTRA_CONTROL_PAUSE = 2
private const val REQUEST_PLAY = 3
private const val REQUEST_PAUSE = 4
