package com.xbot.anilibriarefresh.ui.feature.title.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.xbot.anilibriarefresh.ui.components.TextEnding
import com.xbot.anilibriarefresh.ui.icons.AnilibriaIcons
import com.xbot.anilibriarefresh.ui.icons.Heart
import com.xbot.anilibriarefresh.ui.theme.buttonPagerContentColorLightRed
import com.xbot.domain.model.TitleDetailModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Genres(
    modifier: Modifier = Modifier,
    title: TitleDetailModel,
    flowRowSize: MutableIntState
) {
    Row(modifier = modifier.padding(start = 16.dp, end = 16.dp)) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    flowRowSize.intValue = coordinates.size.height
                },
            horizontalArrangement = Arrangement.Start
        ) {
            title.genres.forEachIndexed { index, item ->
                Text(
                    text = if (index != title.genres.size - 1) "$item, " else item
                )
            }
        }
    }
    Spacer(Modifier.padding(bottom = 16.dp))
}