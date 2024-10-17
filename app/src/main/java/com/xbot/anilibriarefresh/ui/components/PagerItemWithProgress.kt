package com.xbot.anilibriarefresh.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PagerItemWithProgress(state: PagerState) {

    Row(
        Modifier
            .fillMaxWidth()
            .aspectRatio(7f / 10f)
            .padding(bottom = 50.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        repeat(state.pageCount) { iteration ->
            ProgressBarComponent(
                modifier = Modifier.padding(start = 2.dp, end = 2.dp),
                color = if (state.currentPage == iteration) Color.Red
                else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}