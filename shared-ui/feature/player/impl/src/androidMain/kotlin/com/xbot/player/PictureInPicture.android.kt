package com.xbot.player

import android.app.PendingIntent
import android.app.PictureInPictureParams
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.retain.RetainedEffect
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
import io.github.kdroidfilter.composemediaplayer.VideoPlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

@Composable
actual fun rememberPictureInPictureController(
    player: VideoPlayerState
): PictureInPictureController {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity<ComponentActivity>() }
    val scope = rememberCoroutineScope()

    val controller = remember(activity, player, scope) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PictureInPictureControllerImpl(activity, player, scope)
        } else {
            PictureInPictureControllerStub()
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
    private val scope: CoroutineScope,
) : PictureInPictureController, AutoCloseable {

    private val _isInPipMode = mutableStateOf(false)
    override val isInPictureInPictureMode: Boolean
        get() = _isInPipMode.value

    private var broadcastReceiver: BroadcastReceiver? = null
    private var lastBounds: Rect? = null
    private var observingJob: Job? = null
    private var pipListener: Consumer<PictureInPictureModeChangedInfo>? = null

    init {
        startObserving()
        setupPipListener()
        setupBroadcastReceiver()
    }

    private fun startObserving() {
        observingJob = scope.launch {
            combine(
                snapshotFlow { playerState.isPlaying },
                snapshotFlow { playerState.aspectRatio }
            ) { isPlaying, aspectRatio ->
                Log.d(LOG_TAG, "Params changed - isPlaying: ${isPlaying}, aspectRatio: ${aspectRatio}")
                updatePipParams(isPlaying, aspectRatio)
            }.launchIn(this)
        }
    }

    private fun updatePipParams(isPlaying: Boolean, aspectRatio: Float) {
        val builder = PictureInPictureParams.Builder()

        if (!isInPictureInPictureMode && lastBounds != null) {
            builder.setSourceRectHint(calculateVideoRect(lastBounds!!, aspectRatio))
        } else {
            builder.setSourceRectHint(null)
        }

        // Aspect ratio
        val rational = if (aspectRatio > 0) {
            Rational((aspectRatio * 100).toInt(), 100)
        } else {
            Rational(16, 9)
        }
        builder.setAspectRatio(rational)
        builder.setActions(buildRemoteActions(isPlaying, activity))

        // Android 12+ features
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            builder.setAutoEnterEnabled(isPlaying)
            builder.setSeamlessResizeEnabled(true)
        }

        try {
            activity.setPictureInPictureParams(builder.build())
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Failed to update params", e)
        }
    }

    private fun setupPipListener() {
        pipListener = Consumer { info ->
            _isInPipMode.value = info.isInPictureInPictureMode

            Log.d(LOG_TAG, "isInPictureInPictureMode: ${info.isInPictureInPictureMode}")

            if (info.isInPictureInPictureMode) {
                registerBroadcastReceiver()
            } else {
                unregisterBroadcastReceiver()
            }
        }

        pipListener?.let { activity.addOnPictureInPictureModeChangedListener(it) }
    }

    private fun setupBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action != ACTION_BROADCAST_CONTROL) return

                when (intent.getIntExtra(EXTRA_CONTROL_TYPE, 0)) {
                    EXTRA_CONTROL_PAUSE -> playerState.pause()
                    EXTRA_CONTROL_PLAY -> playerState.play()
                }
            }
        }
    }

    private fun registerBroadcastReceiver() {
        broadcastReceiver?.let { receiver ->
            ContextCompat.registerReceiver(
                activity,
                receiver,
                IntentFilter(ACTION_BROADCAST_CONTROL),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }
    }

    private fun unregisterBroadcastReceiver() {
        try {
            broadcastReceiver?.let { activity.unregisterReceiver(it) }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Failed to unregister broadcast receiver", e)
        }
    }

    override val modifier: Modifier = Modifier.onGloballyPositioned { coordinates ->
        val bounds = coordinates.boundsInWindow().toAndroidRectF().toRect()
        lastBounds = bounds

        updatePipParams(playerState.isPlaying, playerState.aspectRatio)
    }

    override fun enterPictureInPictureMode() {
        try {
            val params = PictureInPictureParams.Builder()
                .setAspectRatio(
                    if (playerState.aspectRatio > 0) {
                        Rational((playerState.aspectRatio * 100).toInt(), 100)
                    } else {
                        Rational(16, 9)
                    }
                )
                .setActions(buildRemoteActions(playerState.isPlaying, activity))
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        setSeamlessResizeEnabled(true)
                    }
                }
                .build()

            activity.enterPictureInPictureMode(params)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun close() {
        Log.d(LOG_TAG, "Cleanup started")

        observingJob?.cancel()
        observingJob = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            try {
                val builder = PictureInPictureParams.Builder()
                    .setAutoEnterEnabled(false)
                    .setSeamlessResizeEnabled(false)

                activity.setPictureInPictureParams(builder.build())
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Failed to disable AutoEnter", e)
            }
        }

        pipListener?.let { activity.removeOnPictureInPictureModeChangedListener(it) }
        pipListener = null

        unregisterBroadcastReceiver()
        broadcastReceiver = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

    private fun calculateVideoRect(containerBounds: Rect, videoAspectRatio: Float): Rect {
        val containerWidth = containerBounds.width()
        val containerHeight = containerBounds.height()
        val containerAspectRatio = containerWidth / containerHeight

        var videoWidth = containerWidth.toFloat()
        var videoHeight = containerHeight.toFloat()

        if (containerAspectRatio > videoAspectRatio) {
            videoWidth = containerHeight * videoAspectRatio
        } else {
            videoHeight = containerWidth / videoAspectRatio
        }

        val left = containerBounds.left + (containerWidth - videoWidth) / 2
        val top = containerBounds.top + (containerHeight - videoHeight) / 2

        return Rect(
            left.toInt(),
            top.toInt(),
            (left + videoWidth).toInt(),
            (top + videoHeight).toInt()
        )
    }

    companion object {
        // Broadcast action constants
        private const val ACTION_BROADCAST_CONTROL = "broadcast_control"
        private const val EXTRA_CONTROL_TYPE = "control_type"
        private const val EXTRA_CONTROL_PLAY = 1
        private const val EXTRA_CONTROL_PAUSE = 2
        private const val REQUEST_PLAY = 3
        private const val REQUEST_PAUSE = 4

        private const val LOG_TAG = "PictureInPicture"
    }
}

/**
 * Stub implementation for Android versions below O (API 26)
 */
private class PictureInPictureControllerStub : PictureInPictureController {
    override val modifier: Modifier = Modifier
    override val isInPictureInPictureMode: Boolean = false
    override fun enterPictureInPictureMode() {}
}
