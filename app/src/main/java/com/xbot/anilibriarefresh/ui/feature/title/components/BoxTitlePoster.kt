package com.xbot.anilibriarefresh.ui.feature.title.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.xbot.anilibriarefresh.ui.components.PosterImage
import com.xbot.anilibriarefresh.ui.components.TextEnding
import com.xbot.anilibriarefresh.ui.icons.AnilibriaIcons
import com.xbot.anilibriarefresh.ui.icons.Heart
import com.xbot.anilibriarefresh.ui.theme.buttonPagerContentColorLightRed
import com.xbot.domain.model.TitleDetailModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BoxTitlePoster(
    title: TitleDetailModel,
    scrollState: ScrollState
) {
    Row(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()
            .clipToBounds(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PosterImage(
            modifier = Modifier
                .height(heightPosterTitle)
                .aspectRatio(7f / 10f)
                .clip(RoundedCornerShape(8.dp)),
            poster = title.poster
        )
        Column {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                textAlign = TextAlign.Center,
                text = title.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp),
            ) {
                Text(
                    text = "${title.year}, ${title.addedInUsersFavorites}"
                )
                Spacer(modifier = Modifier.padding(1.dp))
                Icon(
                    imageVector = AnilibriaIcons.Filled.Heart,
                    contentDescription = "",
                    tint = buttonPagerContentColorLightRed,
                    modifier = Modifier.size(18.dp)
                )
            }
            Row (
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxWidth(),
            ) {
                TextEnding(episodes = title.episodesTotal ?: 0, str = ", ")
                Text(text = "${title.type}, ${title.ageRating}")
            }
        }
    }
}

val heightPosterTitle = 192.dp
val paddingValues = 16.dp