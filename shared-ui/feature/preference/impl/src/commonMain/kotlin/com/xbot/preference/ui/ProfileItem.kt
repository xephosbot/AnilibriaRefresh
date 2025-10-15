package com.xbot.preference.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.RoundedPolygon
import com.valentinilk.shimmer.shimmer
import com.xbot.designsystem.components.PosterImage
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.AnilibriaLogo
import com.xbot.domain.models.User
import com.xbot.designsystem.components.DimensionSubcomposeLayout
import com.xbot.designsystem.modifier.LocalShimmer
import com.xbot.preference.ProfileScreenState

@Composable
internal fun ProfileItem(
    modifier: Modifier = Modifier,
    state: ProfileScreenState,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val color =
        if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceBright
    val shimmer = LocalShimmer.current

    Box(
        modifier = modifier
            .background(color)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 76.dp)
            .clickable(onClick = onClick)
            .padding(start = 14.dp, end = 24.dp, top = 16.dp, bottom = 16.dp)
    ) {
        Crossfade(targetState = state) { targetState ->
            when (targetState) {
                ProfileScreenState.Loading -> {
                    Row(
                        modifier = Modifier.shimmer(shimmer),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray),
                        )
                        Spacer(Modifier.width(12.dp))

                        Column {
                            Box(
                                modifier = Modifier
                                    .height(16.dp)
                                    .widthIn(max = 300.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.LightGray),
                            )
                            Spacer(Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .height(16.dp)
                                    .widthIn(max = 260.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.LightGray),
                            )
                        }
                    }
                }

                is ProfileScreenState.LoggedIn -> {
                    Row {
                        PosterImage(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape),
                            poster = targetState.userProfile.avatar
                        )
                        Spacer(Modifier.width(12.dp))

                        Column {
                            Text(
                                text = targetState.userProfile.nickname
                                    ?: "ID: ${targetState.userProfile.id}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                            )
                            Text(
                                modifier = Modifier.graphicsLayer { alpha = 0.6f },
                                text = targetState.userProfile.email,
                                style = MaterialTheme.typography.titleSmall,
                            )
                        }
                    }
                }

                ProfileScreenState.LoggedOut -> {
                    Row {
                        PosterImage(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape),
                            poster = null
                        )
                        Spacer(Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Вход в аккаунт",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                            )
                            Text(
                                modifier = Modifier.graphicsLayer { alpha = 0.6f },
                                text = "Войдите в аккаунт",
                                style = MaterialTheme.typography.titleSmall,
                            )
                        }
                    }
                }
            }
        }
    }
}