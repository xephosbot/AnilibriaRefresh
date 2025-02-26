package com.xbot.player

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.player.ui.rememberPlayer
import com.xbot.player.service.PlaybackService
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : ComponentActivity() {
    private val viewModel by viewModel<PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val player by rememberPlayer<PlaybackService>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(player, state) {
                player?.addMediaItems(state.playList)
                player?.playWhenReady = true
                player?.prepare()
            }

            PlayerScreen(
                player = player,
                onCloseClick = { closeActivity() },
                onSettingsClick = { /*TODO*/ },
                onToggleFullScreen = { /*TODO*/ }
            )
        }

        onBackPressedDispatcher.addCallback {
            closeActivity()
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