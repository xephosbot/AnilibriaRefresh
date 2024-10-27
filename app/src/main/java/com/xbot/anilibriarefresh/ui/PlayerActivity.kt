package com.xbot.anilibriarefresh.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.anilibriarefresh.ui.theme.AnilibriaTheme
import com.xbot.media.ui.PlayerSurface
import com.xbot.media.ui.SURFACE_TYPE_SURFACE_VIEW
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

                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = Color.Black
                ) {
                    player?.let {
                        PlayerSurface(
                            modifier = Modifier
                                .fillMaxSize()
                                .aspectRatio(16f / 9f),
                            player = it,
                            surfaceType = SURFACE_TYPE_SURFACE_VIEW
                        )
                    }
                }
            }
        }
    }
}