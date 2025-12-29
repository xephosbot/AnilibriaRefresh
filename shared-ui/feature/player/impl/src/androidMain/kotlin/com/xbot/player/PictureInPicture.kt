package com.xbot.player

import android.app.PendingIntent
import android.app.PictureInPictureParams
import android.app.RemoteAction
import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContextWrapper
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect as ComposeRect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.PictureInPictureModeChangedInfo
import androidx.core.content.ContextCompat
import androidx.core.util.Consumer
import io.github.kdroidfilter.composemediaplayer.VideoPlayerState

// Constants for broadcast receiver
private const val ACTION_BROADCAST_CONTROL = "broadcast_control"
private const val EXTRA_CONTROL_TYPE = "control_type"
private const val EXTRA_CONTROL_PLAY = 1
private const val EXTRA_CONTROL_PAUSE = 2
private const val REQUEST_CODE_PLAY = 1
private const val REQUEST_CODE_PAUSE = 2

@Composable
actual fun rememberPictureInPictureController(player: VideoPlayerState): PictureInPictureController {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        return remember {
            object : PictureInPictureController {
                override val isInPictureInPictureMode: Boolean = false
                override val modifier: Modifier = Modifier
            }
        }
    }

    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    val isInPipMode = rememberIsInPipMode()
    val shouldEnterPipMode = player.isPlaying

    PipCleanupEffect(activity)

    PlayerBroadcastReceiver(player, isInPipMode)

    PipListenerPreAPI12(shouldEnterPipMode, activity)

    val pipModifier = rememberPipParamsAndModifier(player, shouldEnterPipMode, activity)

    return object : PictureInPictureController {
        override val isInPictureInPictureMode: Boolean = isInPipMode
        override val modifier: Modifier = pipModifier
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun rememberPipParamsAndModifier(
    player: VideoPlayerState,
    shouldEnterPipMode: Boolean,
    activity: ComponentActivity
): Modifier {
    val context = LocalContext.current
    
    // Explicitly read state to trigger recomposition when these change
    val metadata = player.metadata
    val aspectRatioValue = player.aspectRatio
    
    return Modifier.onGloballyPositioned { layoutCoordinates ->
        val builder = PictureInPictureParams.Builder()

        // Aspect Ratio & Source Rect Hint
        // We use the values captured in the closure (which update on recomposition)
        // or read directly from player if stable.
        val width = metadata.width
        val height = metadata.height

        val aspectRatio = if (width != null && height != null && width > 0 && height > 0) {
            width.toFloat() / height.toFloat()
        } else {
            aspectRatioValue
        }

        if (aspectRatio > 0) {
            val clampedRatio = aspectRatio.coerceIn(0.41841004184f, 2.39f)
            
            // Use exact rational if possible and within bounds
            if (width != null && height != null && width > 0 && height > 0 && clampedRatio == aspectRatio) {
                 builder.setAspectRatio(Rational(width, height))
            } else {
                 builder.setAspectRatio(Rational((clampedRatio * 10000).toInt(), 10000))
            }

            // Source Rect Hint
            // Only update source rect hint if NOT in PiP mode to avoid exit animation glitches
            if (!activity.isInPictureInPictureMode) {
                val bounds = layoutCoordinates.boundsInWindow()
                builder.setSourceRectHint(calculateVideoRect(bounds, clampedRatio))
            } else {
                builder.setSourceRectHint(null)
            }
        }

        // Auto Enter (Android 12+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            builder.setAutoEnterEnabled(shouldEnterPipMode)
        }

        // Remote Actions
        builder.setActions(listOfRemoteActions(shouldEnterPipMode, context))

        try {
            activity.setPictureInPictureParams(builder.build())
        } catch (e: Exception) {
            Log.e("PiP", "Failed to set PiP params", e)
        }
    }
}

@Composable
private fun PipCleanupEffect(activity: ComponentActivity) {
    DisposableEffect(Unit) {
        onDispose {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                try {
                    activity.setPictureInPictureParams(
                        PictureInPictureParams.Builder().setAutoEnterEnabled(false).build()
                    )
                } catch (e: Exception) {
                    Log.e("PiP", "Failed to reset PiP params", e)
                }
            }
        }
    }
}

@Composable
private fun PlayerBroadcastReceiver(player: VideoPlayerState, isInPipMode: Boolean) {
    if (isInPipMode) {
        val context = LocalContext.current
        DisposableEffect(player) {
            val broadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    if (intent?.action == ACTION_BROADCAST_CONTROL) {
                        when (intent.getIntExtra(EXTRA_CONTROL_TYPE, 0)) {
                            EXTRA_CONTROL_PLAY -> player.play()
                            EXTRA_CONTROL_PAUSE -> player.pause()
                        }
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
    }
}

@Composable
private fun PipListenerPreAPI12(shouldEnterPipMode: Boolean, activity: ComponentActivity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
        Build.VERSION.SDK_INT < Build.VERSION_CODES.S
    ) {
        val currentShouldEnterPipMode by rememberUpdatedState(shouldEnterPipMode)
        DisposableEffect(activity) {
            val onUserLeaveBehavior = Runnable {
                if (currentShouldEnterPipMode) {
                    try {
                        activity.enterPictureInPictureMode(PictureInPictureParams.Builder().build())
                    } catch (e: Exception) {
                        Log.e("PiP", "Failed to enter PiP mode", e)
                    }
                }
            }
            activity.addOnUserLeaveHintListener(onUserLeaveBehavior)
            onDispose { activity.removeOnUserLeaveHintListener(onUserLeaveBehavior) }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun listOfRemoteActions(isPlaying: Boolean, context: Context): List<RemoteAction> {
    val iconId = if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
    val title = if (isPlaying) "Pause" else "Play"
    val controlType = if (isPlaying) EXTRA_CONTROL_PAUSE else EXTRA_CONTROL_PLAY
    val requestCode = if (isPlaying) REQUEST_CODE_PAUSE else REQUEST_CODE_PLAY

    return listOf(
        buildRemoteAction(iconId, title, requestCode, controlType, context)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
private fun buildRemoteAction(
    @DrawableRes iconResId: Int,
    title: String,
    requestCode: Int,
    controlType: Int,
    context: Context,
): RemoteAction {
    val intent = Intent(ACTION_BROADCAST_CONTROL).apply {
        setPackage(context.packageName)
        putExtra(EXTRA_CONTROL_TYPE, controlType)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
    return RemoteAction(
        Icon.createWithResource(context, iconResId),
        title,
        title,
        pendingIntent
    )
}

@Composable
fun rememberIsInPipMode(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val activity = LocalContext.current.findActivity()
        var pipMode by remember { mutableStateOf(activity.isInPictureInPictureMode) }
        DisposableEffect(activity) {
            val observer = Consumer<PictureInPictureModeChangedInfo> { info ->
                pipMode = info.isInPictureInPictureMode
            }
            activity.addOnPictureInPictureModeChangedListener(observer)
            onDispose { activity.removeOnPictureInPictureModeChangedListener(observer) }
        }
        return pipMode
    }
    return false
}

private fun calculateVideoRect(containerBounds: ComposeRect, videoAspectRatio: Float): Rect {
    val containerWidth = containerBounds.width
    val containerHeight = containerBounds.height
    val containerAspectRatio = containerWidth / containerHeight

    var videoWidth = containerWidth
    var videoHeight = containerHeight

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

internal fun Context.findActivity(): ComponentActivity {
    var context = this
    while (context is ContextWrapper) {
        if (context is ComponentActivity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Picture in picture should be called in the context of an Activity")
}
