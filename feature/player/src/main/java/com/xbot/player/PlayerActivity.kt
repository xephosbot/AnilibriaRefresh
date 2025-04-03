package com.xbot.player

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.designsystem.theme.AnilibriaTheme
import com.xbot.player.service.PlaybackService
import com.xbot.player.ui.rememberPlayer
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : ComponentActivity() {
    private val viewModel by viewModel<PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val player = rememberPlayer<PlaybackService>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(player, state) {
                player?.clearMediaItems()
                player?.addMediaItems(state.playList)
                player?.seekTo(state.currentPlayingItemId, 0L)
                player?.playWhenReady = true
                player?.prepare()
            }

            AnilibriaTheme {
                PlayerScreen(
                    player = player,
                    onCloseClick = { closeActivity() },
                    onSettingsClick = { /*TODO*/ },
                    onToggleFullScreen = { isFullscreen ->
                        setFullscreen(!isFullscreen)
                    }
                )
            }
        }

        onBackPressedDispatcher.addCallback {
            closeActivity()
        }
    }

    private fun setFullscreen(isFullscreen: Boolean) {
        val orientation = when (isFullscreen) {
            true -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            else -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        requestedOrientation = orientation
        if (isFullscreen) hideSystemUi() else showSystemUi()
    }

    private fun hideSystemUi() {
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showSystemUi() {
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.show(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
        }
    }

    private fun closeActivity() {
        Intent(this@PlayerActivity, PlaybackService::class.java).apply {
            action = PlaybackService.STOP_ACTION
            startService(this)
        }
        finish()
    }
}