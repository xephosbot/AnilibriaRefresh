package com.xbot.anilibriarefresh.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
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

                FullscreenToggle(
                    player = player
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FullscreenToggle(
        player: Player?
    ) {
        val configuration = LocalConfiguration.current
        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        HideSystemBars(isLandscape)

        val mediaState = rememberMediaState(player)
        val mediaContent = remember {
            // TODO movableContentOf here doesn't avoid Media from recreating its surface view when
            // screen rotation changed. Seems like a bug of Compose.
            // see: https://kotlinlang.slack.com/archives/CJLTWPH7S/p1654734644676989
            movableContentOf { isLandscape: Boolean, modifier: Modifier ->
                MediaContent(mediaState, isLandscape, modifier)
            }
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Fullscreen Toggle") },
                    navigationIcon = {
                        IconButton(onClick = { closeActivity() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                        }
                    }
                )
            }
        ) { padding ->
            if (!isLandscape) {
                mediaContent(
                    false,
                    Modifier
                        .padding(padding)
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                )
            }
        }
        if (isLandscape) {
            mediaContent(
                true,
                Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            )
        }
    }

    @Composable
    private fun MediaContent(
        mediaState: MediaState,
        isLandscape: Boolean,
        modifier: Modifier = Modifier
    ) {
        val activity = LocalContext.current.findActivity()!!
        val enterFullscreen = {
            activity.requestedOrientation = SCREEN_ORIENTATION_USER_LANDSCAPE
        }
        val exitFullscreen = {
            activity.requestedOrientation = SCREEN_ORIENTATION_USER_PORTRAIT
        }
        Box(modifier) {
            Media(
                mediaState,
                modifier = Modifier.fillMaxSize(),
                showBuffering = ShowBuffering.Always,
                buffering = {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            )
            Button(
                modifier = Modifier.align(Alignment.BottomEnd),
                onClick = if (isLandscape) exitFullscreen else enterFullscreen
            ) {
                Text(text = if (isLandscape) "Exit Fullscreen" else "Enter Fullscreen")
            }
        }

        BackHandler(isLandscape) {
            exitFullscreen()
        }

        BackHandler(!isLandscape) {
            closeActivity()
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

    private fun Context.findActivity(): Activity? {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) return context
            context = context.baseContext
        }
        return null
    }

    private fun closeActivity() {
        val stopIntent = Intent(this@PlayerActivity, PlaybackService::class.java)
        stopIntent.action = BasePlaybackService.STOP_ACTION
        startService(stopIntent)
        finish()
    }
}