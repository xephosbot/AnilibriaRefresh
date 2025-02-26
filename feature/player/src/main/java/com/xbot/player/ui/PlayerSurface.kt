package com.xbot.player.ui

import android.view.Surface
import android.view.SurfaceView
import android.view.TextureView
import androidx.compose.foundation.AndroidEmbeddedExternalSurface
import androidx.compose.foundation.AndroidExternalSurface
import androidx.compose.foundation.AndroidExternalSurfaceScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.media3.common.Player

/**
 * Provides a dedicated drawing [Surface] for media playbacks using a [Player].
 *
 * The player's video output is displayed with either a [SurfaceView]/[AndroidExternalSurface] or a
 * [TextureView]/[AndroidEmbeddedExternalSurface].
 *
 * [Player] takes care of attaching the rendered output to the [Surface] and clearing it, when it is
 * destroyed.
 *
 * See
 * [Choosing a surface type](https://developer.android.com/media/media3/ui/playerview#surfacetype)
 * for more information.
 */
@Composable
internal fun PlayerSurface(
    modifier: Modifier = Modifier,
    state: MediaState,
    surfaceType: SurfaceType,
) {
    val player = state.player ?: return
    val onSurfaceCreated: (Surface) -> Unit = { surface -> player.setVideoSurface(surface) }
    val onSurfaceDestroyed: () -> Unit = { player.setVideoSurface(null) }
    val onSurfaceInitialized: AndroidExternalSurfaceScope.() -> Unit = {
        onSurface { surface, _, _ ->
            onSurfaceCreated(surface)
            surface.onDestroyed { onSurfaceDestroyed() }
        }
    }

    when (surfaceType) {
        SurfaceType.SurfaceView ->
            AndroidExternalSurface(modifier = modifier, onInit = onSurfaceInitialized)
        SurfaceType.TextureView ->
            AndroidEmbeddedExternalSurface(modifier = modifier, onInit = onSurfaceInitialized)
    }
}

enum class SurfaceType {
    SurfaceView,
    TextureView,
}
