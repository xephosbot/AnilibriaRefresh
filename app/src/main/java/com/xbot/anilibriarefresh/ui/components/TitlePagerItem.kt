package com.xbot.anilibriarefresh.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.xbot.domain.model.TitleModel

@Composable
fun TitlePagerItem(
    modifier: Modifier = Modifier,
    title: TitleModel?) {

    Crossfade(
        targetState = title,
        label = "" //TODO: информативный label для перехода
    ) { state ->
        when (state) {
            null -> LoadingTitlePagerContent(modifier)
            else -> TitlePagerItemContent(modifier, state)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun TitlePagerItemContent(
    modifier: Modifier = Modifier,
    title: TitleModel
) {

}

@Composable
private fun LoadingTitlePagerContent(
    modifier: Modifier = Modifier
) {

}