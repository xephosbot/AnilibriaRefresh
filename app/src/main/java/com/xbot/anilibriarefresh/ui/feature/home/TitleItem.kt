package com.xbot.anilibriarefresh.ui.feature.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    Row(
        modifier
            .fillMaxWidth()
            .height(170.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)) {
        Box(
            modifier
                .size(height = 140.dp, width = 100.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.LightGray)) {
        }
        Column(modifier.padding(start = 16.dp).fillMaxHeight()) {
            Text(
                text = title.name,
                fontWeight = FontWeight.Bold
            )
            Text(text = title.description,
                modifier.padding(top = 5.dp)
                    .weight(1f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun LoadingTitleItem(
    modifier: Modifier = Modifier
) {
    Box(modifier.fillMaxWidth().height(120.dp))
}

@Preview
@Composable
private fun PreviewTitleItem() {
    val titleModel = TitleModel(
        id = 1,
        name = "Атака титанов",
        description = "Аниме об уничтожении мира, где главный герой может уничтожить весь мир и не хочет чтобы его друзья погибали",
        posterUrl = null,
        uploadedTime = 0L
    )
    TitleItem(title = titleModel) { }

}