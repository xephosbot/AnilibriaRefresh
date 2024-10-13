package com.xbot.anilibriarefresh.ui.feature.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import com.xbot.anilibriarefresh.ui.components.PosterImage
import com.xbot.domain.model.PosterModel
import com.xbot.domain.model.TitleModel

@Composable
fun TitleItem(
    modifier: Modifier = Modifier,
    title: TitleModel?,
    shimmer: Shimmer,
    onClick: (Int) -> Unit = {}
) {
    Crossfade(
        targetState = title,
        label = "" //TODO: информативный label для перехода
    ) { state ->
        when (state) {
            null -> LoadingTitleItem(modifier, shimmer)
            else -> TitleItemContent(modifier, state, onClick)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun TitleItemContent(
    modifier: Modifier = Modifier,
    title: TitleModel,
    onClick: (Int) -> Unit
) {
    Surface(
        modifier = modifier.height(192.dp),
        onClick = { onClick(title.id) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            PosterImage(
                modifier = Modifier
                    .height(160.dp)
                    .aspectRatio(7f / 10f)
                    .clip(RoundedCornerShape(8.dp)),
                poster = title.poster
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.fillMaxHeight()) {
                Text(
                    text = title.name,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    modifier = Modifier.weight(1f),
                    text = title.description,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun LoadingTitleItem(
    modifier: Modifier = Modifier,
    shimmer: Shimmer
) {
    Surface(
        modifier = modifier
            .height(192.dp)
            .shimmer(shimmer),
        onClick = {}
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(height = 160.dp)
                    .aspectRatio(7f / 10f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.fillMaxHeight()) {
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.LightGray)
                )

                Spacer(modifier = Modifier.height(8.dp))

                repeat(2) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.LightGray)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Row(modifier = Modifier.weight(1f)) {
                    repeat(2) {
                        Box(
                            modifier = Modifier
                                .width(70.dp)
                                .height(16.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TitleItemPreview() {
    val titleModel = TitleModel(
        id = 1,
        name = "Атака титанов",
        description = "Аниме об уничтожении мира, где главный герой может уничтожить весь мир и не хочет чтобы его друзья погибали",
        poster = PosterModel(
            src = null,
            thumbnail = null
        ),
        uploadedTime = null
    )
    val shimmer = rememberShimmer(ShimmerBounds.Window)

    Column {
        TitleItem(title = titleModel, shimmer = shimmer) { }
        TitleItem(title = null, shimmer = shimmer) { }
    }
}