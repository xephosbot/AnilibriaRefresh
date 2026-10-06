package com.xbot.title.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xbot.designsystem.icons.AnilibertyIcons
import com.xbot.designsystem.icons.PlayArrow
import com.xbot.designsystem.utils.AnilibertyPreview

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun PlayButton(
    title: String,
    subtitle: String,
    progress: Float?,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    )
    val iconColor = if (enabled) colors.containerColor else MaterialTheme.colorScheme.surface
    Button(
        onClick = onClick,
        modifier = modifier
            .heightIn(min = PlayButtonDefaults.Height)
            .progressFill(
                progress = progress,
                color = MaterialTheme.colorScheme.primary.copy(alpha = PROGRESS_ALPHA)
            ),
        enabled = enabled,
        shapes = ButtonDefaults.shapesFor(PlayButtonDefaults.Height),
        colors = colors,
        contentPadding = PlayButtonDefaults.ContentPadding
    ) {
        Box(
            modifier = Modifier
                .size(PlayButtonDefaults.IconContainerSize)
                .clip(MaterialShapes.Cookie9Sided.toShape())
                .background(LocalContentColor.current),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(PlayButtonDefaults.IconSize),
                imageVector = AnilibertyIcons.Filled.PlayArrow,
                contentDescription = null,
                tint = iconColor
            )
        }
        Spacer(Modifier.width(PlayButtonDefaults.IconSpacing))
        Column(modifier = Modifier.weight(1f)) {
            ProvideTextStyle(
                LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            ) {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private fun Modifier.progressFill(progress: Float?, color: Color): Modifier =
    if (progress == null) {
        this
    } else {
        this
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .drawWithContent {
                drawContent()
                drawRect(
                    color = color,
                    size = Size(size.width * progress.coerceIn(0f, 1f), size.height),
                    blendMode = BlendMode.SrcAtop
                )
            }
    }

internal object PlayButtonDefaults {
    val Height = 68.dp
    val ContentPadding = PaddingValues(start = 10.dp, end = 20.dp)
    val IconContainerSize = 48.dp
    val IconSize = 28.dp
    val IconSpacing = 12.dp
}

private const val PROGRESS_ALPHA = 0.22f

@AnilibertyPreview
@Composable
private fun PlayButtonPreview() {
    PlayButton(
        title = "Продолжить",
        subtitle = "Серия 7, осталось 12 мин",
        progress = 0.48f,
        enabled = true,
        onClick = {}
    )
}

@AnilibertyPreview
@Composable
private fun PlayButtonDisabledPreview() {
    PlayButton(
        title = "Смотреть",
        subtitle = "Серия 1, 24 мин",
        progress = null,
        enabled = false,
        onClick = {}
    )
}
