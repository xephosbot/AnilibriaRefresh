package com.xbot.player.ui

import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.text.CueGroup
import androidx.media3.common.util.UnstableApi

/**
 * Determines when the buffering indicator is shown.
 */
enum class ShowBuffering {
    /**
     * The buffering indicator is never shown.
     */
    Never,

    /**
     * The buffering indicator is shown when the player is in the [buffering][Player.STATE_BUFFERING]
     * state and [playWhenReady][Player.getPlayWhenReady] is true.
     */
    WhenPlaying,

    /**
     * The buffering indicator is always shown when the player is in the
     * [buffering][Player.STATE_BUFFERING] state.
     */
    Always,
}

/**
 * Composable component for [Player] media playbacks.
 *
 * @param state The state object to be used to control or observe the [Media] state.
 * @param modifier The modifier to apply to this layout.
 * @param surfaceType The type of surface view used for video playbacks.Using [SurfaceType.None] is
 * recommended for audio only applications, since creating the surface can be expensive. Using
 * [SurfaceType.SurfaceView] is recommended for video applications. Note, [SurfaceType.TextureView]
 * can only be used in a hardware accelerated window. When rendered in software, TextureView will
 * draw nothing.
 * @param resizeMode Controls how video and album art is resized.
 * @param shutterColor The color of the shutter, which used for hiding the currently displayed video
 * frame or media artwork when the player is reset and [keepContentOnPlayerReset] is false.
 * @param keepContentOnPlayerReset Whether the currently displayed video frame or media artwork is
 * kept visible when the player is reset. A player reset is defined to mean the player being
 * re-prepared with different media, the player transitioning to unprepared media or an empty list
 * of media items, or the player being changed.
 * If true is provided, the currently displayed video frame or media artwork will be kept visible
 * until the player has been successfully prepared with new media and loaded enough of it to have
 * determined the available tracks. Hence enabling this option allows transitioning from playing one
 * piece of media to another, or from using one player instance to another, without clearing the
 * content.
 * If false is provided, the currently displayed video frame or media artwork will be hidden as soon
 * as the player is reset.
 * @param useArtwork Whether artwork is used if available in audio streams.
 * @param defaultArtworkPainter The [Painter], which will be used to draw default artwork if no
 * artwork available in audio streams.
 * @param subtitles The subtitles. Default is null.
 * @param showBuffering Determines when the buffering indicator is shown.
 * @param buffering The buffering indicator, typically a circular progress indicator. Default is
 * null.
 * @param errorMessage The error message, which will be shown when an [error][PlaybackException]
 * occurred. Default is null.
 * @param overlay An overlay, which can be shown on top of the player. Default is null.
 * @param controllerHideOnTouch Whether the playback controls are hidden by touch. Default is true.
 * @param controllerAutoShow Whether the playback controls are automatically shown when playback
 * starts, pauses, ends, or fails.
 * @param controller The controller. Since a controller is always a subject to be customized,
 * default is null. The [Media] only provides logic for controller visibility controlling.
 */
@OptIn(UnstableApi::class)
@Composable
fun Media(
    state: MediaState,
    modifier: Modifier = Modifier,
    surfaceType: SurfaceType = SurfaceType.SurfaceView,
    resizeMode: ResizeMode = ResizeMode.Fit,
    shutterColor: Color = Color.Black,
    keepContentOnPlayerReset: Boolean = false,
    useArtwork: Boolean = true,
    defaultArtworkPainter: Painter? = null,
    subtitles: @Composable ((CueGroup) -> Unit)? = null, // TODO
    showBuffering: ShowBuffering = ShowBuffering.Never,
    buffering: @Composable (() -> Unit)? = null,
    errorMessage: @Composable ((PlaybackException) -> Unit)? = null,
    controllerHideOnTouch: Boolean = true,
    controllerAutoShow: Boolean = true,
    controller: @Composable ((MediaState, Boolean) -> Unit)? = { mediaState, isBuffering ->
        PlayerController(
            mediaState = mediaState,
            isBuffering = isBuffering,
        )
    },
) {
    if (showBuffering != ShowBuffering.Never) {
        require(buffering != null) {
            "buffering should not be null if showBuffering is 'ShowBuffering.$showBuffering'"
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { state.contentAspectRatioRaw }
            .collect { contentAspectRatioRaw ->
                if (contentAspectRatioRaw != 0f) {
                    state.contentAspectRatio = contentAspectRatioRaw
                }
            }
    }

    SideEffect {
        state.controllerAutoShow = controllerAutoShow
    }

    Box(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = null,
            ) {
                if (controller != null && state.player != null) {
                    state.controllerVisibility = when (state.controllerVisibility) {
                        ControllerVisibility.Visible -> {
                            if (controllerHideOnTouch) {
                                ControllerVisibility.Invisible
                            } else {
                                ControllerVisibility.Visible
                            }
                        }
                        ControllerVisibility.PartiallyVisible -> ControllerVisibility.Visible
                        ControllerVisibility.Invisible -> ControllerVisibility.Visible
                    }
                }
            },
    ) {
        // video
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .then(
                    if (state.contentAspectRatio <= 0) {
                        Modifier.fillMaxSize()
                    } else {
                        Modifier.resize(state.contentAspectRatio, resizeMode)
                    },
                ),
        ) {
            PlayerSurface(
                modifier = Modifier
                    .fillMaxSize(),
                state = state,
                surfaceType = surfaceType
            )

            // shutter
            if (state.closeShutter) {
                Spacer(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(shutterColor),
                )
            }
            LaunchedEffect(keepContentOnPlayerReset) {
                snapshotFlow { state.isVideoTrackSelected }
                    .collect { isVideoTrackSelected ->
                        when (isVideoTrackSelected) {
                            // non video track is selected, so the shutter must be closed
                            false -> state.closeShutter = true
                            // no track
                            // If keepContentOnPlayerReset is false, close shutter
                            // Otherwise, open it
                            null -> state.closeShutter = !keepContentOnPlayerReset
                            true -> {}
                        }
                    }
            }
            LaunchedEffect(Unit) {
                snapshotFlow { state.player }
                    .collect { player ->
                        val isNewPlayer = player != null
                        if (isNewPlayer && !keepContentOnPlayerReset) {
                            // hide any video from the previous player.
                            state.closeShutter = true
                        }
                    }
            }
        }

        // artwork in audio stream
        val artworkPainter = when {
            // non video track is selected, can use artwork
            state.isVideoTrackSelected == false -> {
                if (!useArtwork) {
                    null
                } else {
                    state.artworkPainter ?: defaultArtworkPainter
                }
            }
            keepContentOnPlayerReset -> state.usingArtworkPainter
            else -> null
        }
        if (artworkPainter != null) {
            Image(
                painter = artworkPainter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = resizeMode.contentScale,
            )
        }
        SideEffect {
            state.usingArtworkPainter = artworkPainter
        }

        // subtitles
        if (subtitles != null) {
            val cues = state.playerState?.cues ?: CueGroup.EMPTY_TIME_ZERO
            subtitles(cues)
        }

        // error message
        if (errorMessage != null) {
            state.playerError?.run { errorMessage(this) }
        }

        val isBufferingShowing by remember(showBuffering) {
            derivedStateOf {
                state.playerState?.run {
                    playbackState == Player.STATE_BUFFERING &&
                        (
                            showBuffering == ShowBuffering.Always ||
                                (showBuffering == ShowBuffering.WhenPlaying && playWhenReady)
                        )
                } ?: false
            }
        }

        // controller
        if (controller != null) {
            LaunchedEffect(Unit) {
                snapshotFlow { state.player }.collect { player ->
                    if (player != null) {
                        state.maybeShowController()
                    }
                }
            }
            controller(state, isBufferingShowing)
        }

        // buffering
        if (isBufferingShowing) buffering?.invoke()
    }
}
