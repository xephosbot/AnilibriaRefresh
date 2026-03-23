package com.xbot.player

import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.retain.RetainedEffect
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Modifier
import androidx.compose.ui.keepScreenOn
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.designsystem.utils.AnilibriaPreview
import com.xbot.fixtures.data.episodeMocks
import com.xbot.player.ui.VideoPlayerController
import com.xbot.player.ui.VideoPlayerLayout
import io.github.kdroidfilter.composemediaplayer.InitialPlayerState
import io.github.kdroidfilter.composemediaplayer.PreviewableVideoPlayerState
import io.github.kdroidfilter.composemediaplayer.VideoPlayerState
import io.github.kdroidfilter.composemediaplayer.createVideoPlayerState
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun PlayerScreen(
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SystemBarsEffect()

    PlayerScreenContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
        onBackClick = onBackClick
    )
}

@Composable
internal fun rememberVideoPlayerState(): VideoPlayerState {
    if (LocalInspectionMode.current) {
        return remember { PreviewableVideoPlayerState() }
    }

    val playerState = retain { createVideoPlayerState() }
    RetainedEffect(Unit) {
        onRetire {
            playerState.stop()
            playerState.dispose()
        }
    }
    return playerState
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun PlayerScreenContent(
    modifier: Modifier,
    state: PlayerScreenState,
    onAction: (PlayerScreenAction) -> Unit,
    onBackClick: () -> Unit
) {
    val player = rememberVideoPlayerState()
    val pipController = rememberPictureInPictureController(player)

    var pendingSeek by remember { mutableStateOf<Float?>(null) }
    var previousEpisodeId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(state.currentEpisode, state.videoUri) {
        val uri = state.videoUri
        val currentEpisodeId = state.currentEpisode?.id

        if (!uri.isNullOrEmpty()) {
            val isQualityChange = previousEpisodeId == currentEpisodeId

            val initialState = if (isQualityChange && player.hasMedia) {
                pendingSeek = player.sliderPos
                if (player.isPlaying) InitialPlayerState.PLAY else InitialPlayerState.PAUSE
            } else {
                InitialPlayerState.PLAY
            }

            player.openUri(uri, initialState)
            previousEpisodeId = currentEpisodeId
        }
    }

    LaunchedEffect(player.isLoading) {
        if (!player.isLoading && pendingSeek != null) {
            player.seekTo(pendingSeek!!)
            pendingSeek = null
        }
    }

    VideoPlayerLayout(
        modifier = modifier.keepScreenOn().then(pipController.modifier),
        player = player,
        controls = {
            if (!pipController.isInPictureInPictureMode && !pipController.isTransitioningToPip) {
                VideoPlayerController(
                    player = player,
                    title = state.currentEpisode?.name.orEmpty(),
                    episodes = state.episodes,
                    selectedEpisode = state.currentEpisode,
                    selectedQuality = state.quality,
                    availableQualities = state.availableQualities,
                    onEpisodeClick = {
                        onAction(PlayerScreenAction.OnEpisodeChange(it))
                    },
                    onQualityChange = {
                        onAction(PlayerScreenAction.OnQualityChange(it))
                    },
                    buffering = {
                        ContainedLoadingIndicator()
                    },
                    onClickBack = onBackClick
                )
            }
        },
    )
}

@Preview(device = "spec:parent=pixel_5,orientation=landscape")
@Composable
private fun PlayerScreenPreview(
    @PreviewParameter(PlayerScreenStateProvider::class) state: PlayerScreenState
) {
    AnilibriaPreview {
        PlayerScreenContent(
            modifier = Modifier,
            state = state,
            onAction = {},
            onBackClick = {}
        )
    }
}

private class PlayerScreenStateProvider : PreviewParameterProvider<PlayerScreenState> {
    override val values = sequenceOf(
        PlayerScreenState(
            isLoading = true
        ),
        PlayerScreenState(
            isLoading = false,
            episodes = episodeMocks,
            currentEpisode = episodeMocks.first(),
            quality = VideoQuality.FHD,
        )
    )
}
