package com.xbot.anilibriarefresh.ui.feature.title

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TitleScreen(
    modifier: Modifier = Modifier,
    viewModel: TitleViewModel = hiltViewModel()
) {
    TitleScreenContent()
}

@Composable
private fun TitleScreenContent(
    modifier: Modifier = Modifier
) {
    //TODO: реализовать title detail screen
}

@Preview
@Composable
private fun TitleScreenPreview() {
    TitleScreenContent()
}