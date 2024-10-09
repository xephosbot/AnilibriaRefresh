package com.xbot.anilibriarefresh.ui.feature.home

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xbot.domain.model.TitleModel

@Composable
fun TitleItem(
    modifier: Modifier = Modifier,
    title: TitleModel?,
    onClick: (Int) -> Unit
) {
    Crossfade(
        targetState = title,
        label = "" //TODO: информативный label для перехода
    ) { state ->
        when (state) {
            null -> {
                LoadingTitleItem(modifier)
            }
            else -> {
                TitleItemContent(
                    modifier = modifier,
                    title = state,
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
private fun TitleItemContent(
    modifier: Modifier = Modifier,
    title: TitleModel,
    onClick: (Int) -> Unit
) {
    //TODO: Layout для загруженного Title
}

@Composable
private fun LoadingTitleItem(
    modifier: Modifier = Modifier
) {
    //TODO: Layout для процесса загрузки
}