package com.xbot.anilibriarefresh.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import com.xbot.anilibriarefresh.service.PlaybackService
import com.xbot.anilibriarefresh.ui.theme.AnilibriaTheme
import com.xbot.media.service.BasePlaybackService
import com.xbot.media.ui.Media
import com.xbot.media.ui.MediaState
import com.xbot.media.ui.PlayerController
import com.xbot.media.ui.ShowBuffering
import com.xbot.media.ui.rememberMediaState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerActivity : ComponentActivity() {

    private val viewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AnilibriaTheme {
                val player by viewModel.controller.collectAsStateWithLifecycle()

                FullscreenPlayer(
                    player = player
                )
            }
        }
    }

    @Composable
    private fun FullscreenPlayer(
        player: Player?
    ) {
        val configuration = LocalConfiguration.current
        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        HideSystemBars(isLandscape)

        val mediaState = rememberMediaState(player)

        CompositionLocalProvider(LocalContentColor provides Color.White) {
            MediaContent(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    then(
                        if (!isLandscape) Modifier.systemBarsPadding()
                        else Modifier
                    ),
                mediaState = mediaState,
                isLandscape = isLandscape,
            )
        }
    }

    @Composable
    private fun MediaContent(
        modifier: Modifier = Modifier,
        mediaState: MediaState,
        isLandscape: Boolean
    ) {
        val activity = LocalContext.current.findActivity()!!
        val enterFullscreen = {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
        }
        val exitFullscreen = {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
        }
        Box(modifier = modifier) {
            Media(
                modifier = Modifier.fillMaxSize(),
                state = mediaState,
                showBuffering = ShowBuffering.Always,
                buffering = {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        CircularProgressIndicator(color = LocalContentColor.current)
                    }
                },
                controller = { state, isBuffering ->
                    PlayerController(
                        mediaState = state,
                        isBuffering = isBuffering,
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    closeActivity()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null
                                )
                            }
                        },
                        bottomActions = {
                            IconButton(
                                onClick = {}
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = null
                                )
                            }
                            IconButton(
                                onClick = if (isLandscape) exitFullscreen else enterFullscreen
                            ) {
                                Icon(
                                    imageVector = when(isLandscape) {
                                        true -> Icons.Default.FullscreenExit
                                        else -> Icons.Default.Fullscreen
                                    },
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }
            )
        }

        BackHandler(isLandscape) {
            exitFullscreen()
        }

        BackHandler(!isLandscape) {
            closeActivity()
        }

        SideEffect {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
        }
    }

    @Composable
    fun HideSystemBars(isLandscape: Boolean) {
        val context = LocalContext.current

        DisposableEffect(isLandscape) {
            val window = context.findActivity()?.window ?: return@DisposableEffect onDispose {}
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)

            when (isLandscape) {
                true -> insetsController.apply {
                    hide(WindowInsetsCompat.Type.statusBars())
                    hide(WindowInsetsCompat.Type.navigationBars())
                    systemBarsBehavior =
                        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
                else -> {
                    insetsController.apply {
                        show(WindowInsetsCompat.Type.statusBars())
                        show(WindowInsetsCompat.Type.navigationBars())
                        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
                    }
                }
            }

            onDispose {
                insetsController.apply {
                    show(WindowInsetsCompat.Type.statusBars())
                    show(WindowInsetsCompat.Type.navigationBars())
                    systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
                }
            }
        }
    }

    private fun closeActivity() {
        Intent(this@PlayerActivity, PlaybackService::class.java).apply {
            action = BasePlaybackService.STOP_ACTION
            startService(this)
        }
        finish()
    }

    private fun Context.findActivity(): Activity? {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) return context
            context = context.baseContext
        }
        return null
    }
}