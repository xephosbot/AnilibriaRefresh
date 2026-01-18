package com.xbot.player

import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.RetainedEffect
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Modifier
import androidx.compose.ui.keepScreenOn
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.designsystem.utils.AnilibriaPreview
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.domain.di.domainModule
import com.xbot.fixtures.di.fixturesModule
import com.xbot.player.navigation.PlayerRoute
import com.xbot.player.ui.VideoPlayerController
import com.xbot.player.ui.VideoPlayerLayout
import io.github.kdroidfilter.composemediaplayer.InitialPlayerState
import io.github.kdroidfilter.composemediaplayer.PreviewableVideoPlayerState
import io.github.kdroidfilter.composemediaplayer.VideoPlayerState
import io.github.kdroidfilter.composemediaplayer.createVideoPlayerState
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

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

    LaunchedEffect(state.currentEpisode) {
        state.currentEpisode?.let { episode ->
            val uri = episode.hls1080 ?: episode.hls720 ?: episode.hls480 ?: ""
            if (uri.isNotEmpty()) {
                player.openUri(uri, InitialPlayerState.PLAY)
            }
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
                    onEpisodeClick = {
                        onAction(PlayerScreenAction.OnEpisodeChange(it))
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
private fun PlayerScreenPreview() {
    AnilibriaPreview {
        KoinApplicationPreview(
            application = {
                modules(
                    domainModule,
                    fixturesModule,
                    module {
                        single { SnackbarManager }
                        single { PlayerRoute(releaseId = 1, episodeOrdinal = 0) }
                        viewModelOf(::PlayerViewModel)
                    }
                )
            }
        ) {
            PlayerScreen(
                onBackClick = {}
            )
        }
    }
}
