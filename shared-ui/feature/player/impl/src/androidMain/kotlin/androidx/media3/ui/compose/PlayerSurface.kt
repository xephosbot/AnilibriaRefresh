/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.media3.ui.compose

import android.content.Context
import android.view.SurfaceView
import android.view.TextureView
import android.view.View
import androidx.annotation.IntDef
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Provides a dedicated drawing [android.view.Surface] for media playbacks using a [Player].
 *
 * The player's video output is displayed with either a [SurfaceView] or a
 * [TextureView].
 *
 * [Player] takes care of attaching the rendered output to the [android.view.Surface] and clearing
 * it, when it is destroyed.
 *
 * See
 * [Choosing a surface type](https://developer.android.com/media/media3/ui/playerview#surfacetype)
 * for more information.
 */
@UnstableApi
@Composable
fun PlayerSurface(
    player: Player?,
    modifier: Modifier = Modifier,
    surfaceType: @SurfaceType Int = SURFACE_TYPE_SURFACE_VIEW,
) {
    when (surfaceType) {
        SURFACE_TYPE_SURFACE_VIEW ->
            PlayerSurfaceInternal(
                player,
                modifier,
                createView = ::SurfaceView,
                setVideoView = Player::setVideoSurfaceView,
                clearVideoView = Player::clearVideoSurfaceView,
            )
        SURFACE_TYPE_TEXTURE_VIEW ->
            PlayerSurfaceInternal(
                player,
                modifier,
                createView = ::TextureView,
                setVideoView = Player::setVideoTextureView,
                clearVideoView = Player::clearVideoTextureView,
            )
        else -> throw IllegalArgumentException("Unrecognized surface type: $surfaceType")
    }
}

@Composable
private fun <T : View> PlayerSurfaceInternal(
    player: Player?,
    modifier: Modifier,
    createView: (Context) -> T,
    setVideoView: Player.(T) -> Unit,
    clearVideoView: Player.(T) -> Unit,
) {
    var view by remember { mutableStateOf<T?>(null) }
    var registeredPlayer by remember { mutableStateOf<Player?>(null) }

    AndroidView(factory = { createView(it).apply { view = this } }, onReset = {}, modifier = modifier)

    view?.let { view ->
        LaunchedEffect(view, player) {
            if (player != null) {
                registeredPlayer?.let { previousPlayer ->
                    if (previousPlayer.isCommandAvailable(Player.COMMAND_SET_VIDEO_SURFACE))
                        previousPlayer.clearVideoView(view)
                    registeredPlayer = null
                }
                if (player.isCommandAvailable(Player.COMMAND_SET_VIDEO_SURFACE)) {
                    player.setVideoView(view)
                    registeredPlayer = player
                }
            } else {
                // Now that our player got null'd, we are not in a rush to get the old view from the
                // previous player. Instead, we schedule clearing of the view for later on the main thread,
                // since that player might have a new view attached to it in the meantime. This will avoid
                // unnecessarily creating a Surface placeholder.
                withContext(Dispatchers.Main) {
                    registeredPlayer?.let { previousPlayer ->
                        if (previousPlayer.isCommandAvailable(Player.COMMAND_SET_VIDEO_SURFACE))
                            previousPlayer.clearVideoView(view)
                        registeredPlayer = null
                    }
                }
            }
        }
    }
}

/**
 * The type of surface used for media playbacks. One of [SURFACE_TYPE_SURFACE_VIEW] or
 * [SURFACE_TYPE_TEXTURE_VIEW].
 */
@UnstableApi
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.TYPE_PARAMETER)
@IntDef(SURFACE_TYPE_SURFACE_VIEW, SURFACE_TYPE_TEXTURE_VIEW)
annotation class SurfaceType

/** Surface type to create [SurfaceView]. */
@UnstableApi const val SURFACE_TYPE_SURFACE_VIEW = 1
/** Surface type to create [TextureView]. */
@UnstableApi const val SURFACE_TYPE_TEXTURE_VIEW = 2