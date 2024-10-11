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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.xbot.domain.model.PosterModel
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
            null -> LoadingTitleItem(modifier)
            else -> TitleItemContent(modifier, state, onClick)
        }
    }
}

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
                imageUrl = title.poster.src,
                thumbnailUrl = title.poster.thumbnail
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PosterImage(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    thumbnailUrl: String? = null
) {
    val requestManager = LocalContext.current.let { remember(it) { Glide.with(it) } }

    GlideImage(
        model = imageUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier,
        transition = CrossFade,
        loading = placeholder(ColorPainter(MaterialTheme.colorScheme.onSurface))
    ) {
        it.thumbnail(requestManager.asDrawable().load(thumbnailUrl).override(10, 70))
    }
}


@Composable
private fun LoadingTitleItem(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(192.dp),
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
                    .background(MaterialTheme.colorScheme.onSurface)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.fillMaxHeight()) {
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.onSurface)
                )

                Spacer(modifier = Modifier.height(8.dp))

                repeat(2) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.onSurface)
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
                                .background(MaterialTheme.colorScheme.onSurface)
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
    Column {
        TitleItem(title = titleModel) { }
        TitleItem(title = null) { }
    }
}