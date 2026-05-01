package com.xbot.player

import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.RetainedEffect
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.keepScreenOn
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.xbot.common.AsyncResult
import com.xbot.common.getOrElse
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.xbot.designsystem.utils.AnilibriaPreviewWrapper
import com.xbot.designsystem.utils.MessageAction
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.domain.fixtures.EpisodeFixtures
import com.xbot.resources.StringResource
import com.xbot.formatters.localizedMessage
import com.xbot.player.ui.VideoPlayerController
import com.xbot.player.ui.VideoPlayerLayout
import com.xbot.resources.Res
import com.xbot.resources.button_retry
import io.github.kdroidfilter.composemediaplayer.InitialPlayerState
import io.github.kdroidfilter.composemediaplayer.PreviewableVideoPlayerState
import io.github.kdroidfilter.composemediaplayer.VideoPlayerState
import io.github.kdroidfilter.composemediaplayer.createVideoPlayerState
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun PlayerScreen(
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is PlayerScreenSideEffect.ShowErrorMessage -> {
                SnackbarManager.showMessage(
                    title = sideEffect.error.localizedMessage(),
                    action = MessageAction(
                        title = StringResource.Text(Res.string.button_retry),
                        action = sideEffect.onRetry,
                    )
                )
            }
        }
    }

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
                    episodes = state.episodes.getOrElse { emptyList() },
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
@PreviewWrapper(AnilibriaPreviewWrapper::class)
@Composable
private fun PlayerScreenPreview(
    @PreviewParameter(PlayerScreenStateProvider::class) state: PlayerScreenState
) {
    PlayerScreenContent(
        modifier = Modifier,
        state = state,
        onAction = {},
        onBackClick = {}
    )
}

private class PlayerScreenStateProvider : PreviewParameterProvider<PlayerScreenState> {
    override val values = sequenceOf(
        PlayerScreenState(),
        PlayerScreenState(
            episodes = AsyncResult.Success(EpisodeFixtures.all),
            currentEpisode = EpisodeFixtures.all.first(),
            quality = VideoQuality.FHD,
        )
    )
}
