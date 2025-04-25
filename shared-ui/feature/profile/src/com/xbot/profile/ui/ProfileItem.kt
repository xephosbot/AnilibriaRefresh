package com.xbot.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
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
import com.xbot.designsystem.components.PosterImage
import com.xbot.designsystem.components.SplitButtonLayout
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.AnilibriaLogo
import com.xbot.domain.models.Profile
import com.xbot.shared.ui.designsystem.components.DimensionSubcomposeLayout

@Composable
internal fun ProfileItem(
    profile: Profile,
    onAccountClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    ProfileItemLayout(
        avatar = {
            PosterImage(
                modifier = Modifier.size(132.dp),
                poster = profile.avatar
            )
        },
        content = {
            Text(
                text = profile.nickname ?: "ID: ${profile.id}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
            )
            Text(
                modifier = Modifier.graphicsLayer { alpha = 0.6f },
                text = profile.email,
                style = MaterialTheme.typography.titleSmall,
            )
            Spacer(Modifier.height(8.dp))
            AccountButton(
                modifier = Modifier.fillMaxWidth(),
                onLeadingClick = {  },
                onTrailingClick = {  },
            )
        }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun AccountButton(
    modifier: Modifier = Modifier,
    onLeadingClick: () -> Unit,
    onTrailingClick: () -> Unit,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.1f),
        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
    ),
    trailingEnabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(8.dp),
) {
    SplitButtonLayout(
        modifier = modifier.fillMaxWidth(),
        leadingButton = {
            SplitButtonDefaults.LeadingButton(
                modifier = Modifier.fillMaxSize(),
                contentPadding = contentPadding,
                colors = colors,
                onClick = onLeadingClick
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceBright,
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
                        Icon(
                            imageVector = AnilibriaIcons.Filled.AnilibriaLogo,
                            contentDescription = null
                        )
                    }
                }
                Spacer(Modifier.weight(1.0f))
                Text(
                    text = "НАСТРОЙКИ АККАУНТА",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.weight(1.0f))
            }
        },
        trailingButton = {
            SplitButtonDefaults.TrailingButton(
                modifier = Modifier.fillMaxSize(),
                enabled = trailingEnabled,
                contentPadding = contentPadding,
                colors = colors,
                onClick = onTrailingClick
            ) {
                Box(
                    modifier = Modifier.size(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                        contentDescription = null
                    )
                }
            }
        }
    )
}
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ProfileItemLayout(
    avatar: @Composable () -> Unit,
    avatarShape: RoundedPolygon = MaterialShapes.Cookie9Sided,
    avatarBorderWidth: Dp = 24.dp,
    avatarOffsetRatio: Float = 0.43f,
    containerColor: Color = MaterialTheme.colorScheme.surfaceBright,
    content: @Composable ColumnScope.() -> Unit
) {
    DimensionSubcomposeLayout(
        content = { avatar() }
    ) { avatarSize ->
        val density = LocalDensity.current
        val avatarOffset = avatarSize.height * avatarOffsetRatio
        val borderedAvatarSize = with(density) {
            Size(
                width = avatarSize.width + avatarBorderWidth.toPx() * 2,
                height = avatarSize.height + avatarBorderWidth.toPx() * 2
            )
        }

        Box(
            modifier = Modifier.profileItemBackground(
                avatarShapePath = avatarShape.toPath(),
                borderedAvatarSize = borderedAvatarSize,
                avatarOffset = avatarOffset,
                containerColor = containerColor
            ),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .padding(top = with(density) { avatarSize.height.toDp() } + 16.dp)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = content
            )

            Box(
                modifier = Modifier
                    .padding(avatarBorderWidth)
                    .clip(avatarShape.toShape())
            ) {
                avatar()
            }
        }
    }
}

private fun Modifier.profileItemBackground(
    avatarShapePath: Path,
    borderedAvatarSize: Size,
    avatarOffset: Float,
    containerColor: Color
): Modifier = this.then(
    drawWithCache {
        val shapePath = avatarShapePath.apply {
            val matrix = Matrix().apply {
                scale(borderedAvatarSize.height, borderedAvatarSize.width)
                translate(
                    x = (size.width - borderedAvatarSize.width) * 0.5f / borderedAvatarSize.width,
                    y = 0f
                )
            }
            transform(matrix)
        }

        onDrawWithContent {
            // Draw background rectangle
            drawRoundRect(
                color = containerColor,
                topLeft = Offset(0f, avatarOffset),
                size = Size(size.width, size.height - avatarOffset),
                cornerRadius = CornerRadius(24.dp.toPx())
            )

            // Draw avatar shape background
            drawPath(
                path = shapePath,
                color = containerColor,
            )

            drawContent()
        }
    }
)