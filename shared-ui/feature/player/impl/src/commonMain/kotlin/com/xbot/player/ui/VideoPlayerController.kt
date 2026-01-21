package com.xbot.player.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.EpisodeListItem
import com.xbot.designsystem.components.Header
import com.xbot.designsystem.components.section
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.ArrowBack
import com.xbot.designsystem.icons.Forward10
import com.xbot.designsystem.icons.Pause
import com.xbot.designsystem.icons.PlayArrow
import com.xbot.designsystem.icons.PlaylistPlay
import com.xbot.designsystem.icons.Replay10
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.domain.models.Episode
import io.github.kdroidfilter.composemediaplayer.VideoPlayerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerController(
    player: VideoPlayerState,
    title: String,
    episodes: List<Episode>,
    selectedEpisode: Episode?,
    onEpisodeClick: (Episode) -> Unit,
    buffering: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isControllerVisible by rememberSaveable { mutableStateOf(true) }
    val shimmer = rememberShimmer(ShimmerBounds.Window)

    ProvideShimmer(shimmer) {
        PlaylistDrawer(
            drawerState = drawerState,
            modifier = modifier,
            drawerContent = {
                PlaylistContent(
                    episodes = episodes,
                    selectedEpisode = selectedEpisode,
                    onEpisodeClick = { episode ->
                        onEpisodeClick(episode)
                        scope.launch { drawerState.close() }
                    }
                )
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                isControllerVisible = !isControllerVisible
                            }
                        )
                    },
                contentAlignment = Alignment.Center,
            ) {
                ControllerOverlay(
                    isVisible = isControllerVisible,
                    playerState = player,
                    title = title,
                    onClickBack = onClickBack,
                    onPlayPause = {
                        if (player.isPlaying) {
                            player.pause()
                        } else {
                            player.play()
                        }
                    },
                    onSeekBack = {
                        player.seekBackward(10000L)
                    },
                    onSeekForward = {
                        player.seekForward(10000L)
                    },
                    onTimeout = { isControllerVisible = false },
                    onOpenPlaylist = {
                        scope.launch { drawerState.open() }
                    }
                )

                if (player.isLoading) {
                    buffering()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaylistDrawer(
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    drawerContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            modifier = modifier,
            drawerState = drawerState,
            gesturesEnabled = drawerState.isOpen,
            drawerContent = {
                ModalDrawerSheet(
                    drawerState = drawerState,
                    modifier = Modifier
                        .windowInsetsPadding(DrawerDefaults.windowInsets.only(WindowInsetsSides.Vertical)),
                    windowInsets = DrawerDefaults.windowInsets.only(WindowInsetsSides.Horizontal)
                ) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        drawerContent()
                    }
                }
            }
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                content()
            }
        }
    }
}

@Composable
private fun PlaylistContent(
    episodes: List<Episode>,
    selectedEpisode: Episode?,
    onEpisodeClick: (Episode) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        item {
            Header(
                title = { Text(text = "Episodes") }
            )
        }
        itemsIndexed(episodes) { index, episode ->
            EpisodeListItem(
                modifier = Modifier.section(index, episodes.size),
                episode = episode,
                selected = episode == selectedEpisode,
                onClick = { onEpisodeClick(episode) }
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ControllerOverlay(
    isVisible: Boolean,
    playerState: VideoPlayerState,
    title: String,
    onClickBack: () -> Unit,
    onPlayPause: () -> Unit,
    onSeekBack: () -> Unit,
    onSeekForward: () -> Unit,
    onOpenPlaylist: () -> Unit,
    onTimeout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = isVisible,
        modifier = modifier,
        label = "controller_visibility"
    ) { shouldShow ->
        if (shouldShow) {
            AutoHidingController(
                playerState = playerState,
                title = title,
                onClickBack = onClickBack,
                onPlayPause = onPlayPause,
                onSeekBack = onSeekBack,
                onSeekForward = onSeekForward,
                onOpenPlaylist = onOpenPlaylist,
                onTimeout = onTimeout,
            )
        }
    }
}

@Composable
private fun AutoHidingController(
    playerState: VideoPlayerState,
    title: String,
    onClickBack: () -> Unit,
    onPlayPause: () -> Unit,
    onSeekBack: () -> Unit,
    onSeekForward: () -> Unit,
    onOpenPlaylist: () -> Unit,
    onTimeout: () -> Unit,
) {
    var hideControllerKey by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(hideControllerKey, playerState.isPlaying, playerState.userDragging) {
        if (playerState.isPlaying && !playerState.userDragging) {
            delay(CONTROLLER_HIDE_DELAY_MS)
            onTimeout()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CONTROLLER_OVERLAY_COLOR)
            .symmetricInsetsPadding(),
        contentAlignment = Alignment.Center
    ) {
        VideoPlayerTopBar(
            title = title,
            onClickBack = onClickBack,
            onOpenPlaylist = onOpenPlaylist,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        if (!playerState.isLoading) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SeekBackButton(
                    onClick = {
                        onSeekBack()
                        hideControllerKey++
                    }
                )

                PlayPauseButton(
                    state = playerState,
                    onClick = {
                        onPlayPause()
                        hideControllerKey++
                    }
                )

                SeekForwardButton(
                    onClick = {
                        onSeekForward()
                        hideControllerKey++
                    }
                )
            }
        }

        TimelineControls(
            playerState = playerState,
            onInteraction = { hideControllerKey++ },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun TimelineControls(
    playerState: VideoPlayerState,
    onInteraction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Slider(
            value = playerState.sliderPos,
            onValueChange = {
                playerState.sliderPos = it
                playerState.userDragging = true
                onInteraction()
            },
            onValueChangeFinished = {
                playerState.userDragging = false
                playerState.seekTo(playerState.sliderPos)
                onInteraction()
            },
            valueRange = 0f..1000f,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = playerState.positionText,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
            Text(
                text = playerState.durationText,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VideoPlayerTopBar(
    title: String,
    onClickBack: () -> Unit,
    onOpenPlaylist: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onClickBack) {
                Icon(
                    imageVector = AnilibriaIcons.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        actions = {
            IconButton(onClick = onOpenPlaylist) {
                Icon(
                    imageVector = AnilibriaIcons.PlaylistPlay,
                    contentDescription = null,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        windowInsets = WindowInsets(0)
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun PlayPauseButton(
    state: VideoPlayerState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val icon = if (state.isPlaying) {
        AnilibriaIcons.Filled.Pause
    } else {
        AnilibriaIcons.Filled.PlayArrow
    }

    FilledTonalIconToggleButton(
        modifier = modifier.size(IconButtonDefaults.mediumContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide)),
        checked = state.isPlaying,
        onCheckedChange = { onClick() },
        shapes = IconButtonDefaults.toggleableShapes()
    ) {
        Icon(
            modifier = Modifier.size(IconButtonDefaults.mediumIconSize),
            imageVector = icon,
            contentDescription = null,
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SeekBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilledIconButton(
        modifier = modifier.size(IconButtonDefaults.mediumContainerSize(IconButtonDefaults.IconButtonWidthOption.Narrow)),
        shapes = IconButtonDefaults.shapes(IconButtonDefaults.mediumSquareShape),
        onClick = onClick,
    ) {
        Icon(
            modifier = Modifier.size(IconButtonDefaults.mediumIconSize),
            imageVector = AnilibriaIcons.Replay10,
            contentDescription = null,
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SeekForwardButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilledIconButton(
        modifier = modifier.size(IconButtonDefaults.mediumContainerSize(IconButtonDefaults.IconButtonWidthOption.Narrow)),
        shapes = IconButtonDefaults.shapes(IconButtonDefaults.mediumSquareShape),
        onClick = onClick,
    ) {
        Icon(
            modifier = Modifier.size(IconButtonDefaults.mediumIconSize),
            imageVector = AnilibriaIcons.Forward10,
            contentDescription = null,
        )
    }
}

@Composable
private fun Modifier.symmetricInsetsPadding(): Modifier {
    val insets = WindowInsets.systemBars.union(WindowInsets.displayCutout)
    val symmetricInsets = remember(insets) {
        object : WindowInsets {
            override fun getLeft(density: Density, layoutDirection: LayoutDirection): Int {
                val left = insets.getLeft(density, layoutDirection)
                val right = insets.getRight(density, layoutDirection)
                return max(left, right)
            }

            override fun getTop(density: Density): Int {
                return insets.getTop(density)
            }

            override fun getRight(density: Density, layoutDirection: LayoutDirection): Int {
                val left = insets.getLeft(density, layoutDirection)
                val right = insets.getRight(density, layoutDirection)
                return max(left, right)
            }

            override fun getBottom(density: Density): Int {
                return insets.getBottom(density)
            }
        }
    }
    return this.windowInsetsPadding(symmetricInsets)
        .consumeWindowInsets(symmetricInsets)
}

private fun VideoPlayerState.seekForward(amount: Long) {
    val durationSec = metadata.duration ?: 0
    println("durationSec: $durationSec")
    if (durationSec > 0) {
        val delta = (amount.toFloat() / durationSec) * 1000f
        seekTo((sliderPos + delta).coerceIn(0f, 1000f))
    }
}

private fun VideoPlayerState.seekBackward(amount: Long) {
    val durationSec = metadata.duration ?: 0
    println("durationSec: $durationSec")
    if (durationSec > 0) {
        val delta = (amount.toFloat() / durationSec) * 1000f
        seekTo((sliderPos - delta).coerceIn(0f, 1000f))
    }
}

private const val CONTROLLER_HIDE_DELAY_MS = 3000L
private val CONTROLLER_OVERLAY_COLOR = Color.Black.copy(alpha = 0.6f)
