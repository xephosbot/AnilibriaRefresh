package com.xbot.anilibriarefresh.ui

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.anilibriarefresh.media.ui.PlayerSurface
import com.xbot.anilibriarefresh.media.ui.SURFACE_TYPE_SURFACE_VIEW

@Composable
fun AnilibriaPlayerScreen(
    modifier: Modifier = Modifier,
    viewModel: TestPlaybackViewModel = hiltViewModel()
) {
    val player by viewModel.player.collectAsStateWithLifecycle()

    if (player != null) {
        Surface {
            PlayerSurface(
                modifier = modifier
                    .fillMaxSize()
                    .aspectRatio(16f / 9f),
                player = player!!,
                surfaceType = SURFACE_TYPE_SURFACE_VIEW
            )
        }
    }
}