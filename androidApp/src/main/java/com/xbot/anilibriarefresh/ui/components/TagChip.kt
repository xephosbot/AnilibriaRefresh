package com.xbot.anilibriarefresh.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xbot.anilibriarefresh.models.TitleTag
import com.xbot.anilibriarefresh.ui.utils.stringResource
import com.xbot.designsystem.components.IconPosition

@Composable
fun TagChip(
    modifier: Modifier = Modifier,
    tag: TitleTag,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(containerColor),
) {
    TagChip(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor
    ) {
        when (tag) {
            is TitleTag.Text -> Text(
                text = stringResource(tag.text),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )

            is TitleTag.TextWithIcon -> TextWithIcon(
                text = stringResource(tag.text),
                imageVector = tag.icon,
                iconPosition = IconPosition.END,
            )
        }
    }
}

@Composable
fun TagChip(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(containerColor),
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = 5.dp,
        shape = CircleShape,
    ) {
        // TODO: Use MaterialTheme.typography style
        CompositionLocalProvider(
            LocalTextStyle provides LocalTextStyle.current.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 10.sp
            )
        ) {
            Row(
                modifier = modifier.padding(
                    horizontal = 8.dp,
                    vertical = 4.dp
                ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}