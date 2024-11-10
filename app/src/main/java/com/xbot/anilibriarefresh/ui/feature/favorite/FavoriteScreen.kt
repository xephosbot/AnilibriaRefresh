package com.xbot.anilibriarefresh.ui.feature.favorite

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoriteViewModel = koinViewModel(),
) {
    // TODO: Favorite screen
    Surface(modifier = modifier.fillMaxSize()) {
    }
}
