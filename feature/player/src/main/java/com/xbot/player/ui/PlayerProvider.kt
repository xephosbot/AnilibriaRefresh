package com.xbot.player.ui

import android.content.ComponentName
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

@Composable
inline fun <reified T: MediaSessionService> rememberPlayer(): State<Player?> {
    val context = LocalContext.current
    val playerProvider = remember(context) { PlayerProvider.create<T>(context) }

    LifecycleStartEffect(Unit) {
        playerProvider.initialize()
        onStopOrDispose {
            playerProvider.release()
        }
    }

    return playerProvider.player
}

@Stable
class PlayerProvider @PublishedApi internal constructor(
    context: Context,
    componentClass: Class<*>,
): RememberObserver {
    private val applicationContext: Context = context.applicationContext
    private val sessionToken: SessionToken = SessionToken(context, ComponentName(applicationContext, componentClass))
    private var controllerFuture: ListenableFuture<MediaController>? = null

    var player = mutableStateOf<Player?>(null)
        private set

    init {
        initialize()
    }

    override fun onAbandoned() = release()

    override fun onForgotten() = release()

    override fun onRemembered() = Unit

    @PublishedApi
    internal fun initialize() {
        if (controllerFuture == null || controllerFuture?.isDone == true) {
            controllerFuture = MediaController.Builder(applicationContext, sessionToken).buildAsync()
        }
        controllerFuture?.addListener(
            {
                player.value = controllerFuture?.let { controller ->
                    if (controller.isDone) controller.get() else null
                }
            },
            MoreExecutors.directExecutor()
        )
    }

    @PublishedApi
    internal fun release() {
        controllerFuture?.let { controller ->
            MediaController.releaseFuture(controller)
            player.value = null
        }
        controllerFuture = null
    }

    companion object {
        @PublishedApi
        @Volatile
        internal var instance: PlayerProvider? = null

        inline fun <reified T : MediaSessionService> create(context: Context): PlayerProvider {
            return instance ?: synchronized(this) {
                PlayerProvider(context, T::class.java).also { instance = it }
            }
        }
    }
}