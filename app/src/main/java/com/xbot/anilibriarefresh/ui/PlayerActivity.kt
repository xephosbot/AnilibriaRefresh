package com.xbot.anilibriarefresh.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import com.xbot.anilibriarefresh.service.PlaybackService
import com.xbot.anilibriarefresh.ui.theme.AnilibriaTheme
import com.xbot.anilibriarefresh.ui.utils.rememberSystemUiController
import com.xbot.media.service.BasePlaybackService
import com.xbot.media.ui.Media
import com.xbot.media.ui.PlayerController
import com.xbot.media.ui.ShowBuffering
import com.xbot.media.ui.rememberMediaState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerActivity : ComponentActivity() {

    private val viewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        enableEdgeToEdge()

        setContent {
            AnilibriaTheme {
                val player by viewModel.controller.collectAsStateWithLifecycle()
                FullscreenPlayer(player = player)
            }
        }

        onBackPressedDispatcher.addCallback {
            closeActivity()
        }
    }

    @Composable
    private fun FullscreenPlayer(player: Player?) {
        val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
        val systemUiController = rememberSystemUiController()

        SideEffect {
            systemUiController.isSystemBarsVisible = !isLandscape
            systemUiController.systemBarsBehavior = if (isLandscape) {
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            } else {
                WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
            }
        }

        val mediaState = rememberMediaState(player)

        CompositionLocalProvider(LocalContentColor provides Color.White) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    then(
                        if (!isLandscape) Modifier.systemBarsPadding()
                        else Modifier
                    )
            ) {
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
                                IconButton(onClick = { closeActivity() }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null
                                    )
                                }
                            },
                            bottomActions = {
                                IconButton(onClick = {}) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = null
                                    )
                                }
                                IconButton(onClick = {
                                    if (isLandscape) {
                                        systemUiController.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                                    } else {
                                        systemUiController.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                                    }
                                }) {
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
        }
    }

    private fun closeActivity() {
        Intent(this@PlayerActivity, PlaybackService::class.java).apply {
            action = BasePlaybackService.STOP_ACTION
            startService(this)
        }
        finish()
    }
}