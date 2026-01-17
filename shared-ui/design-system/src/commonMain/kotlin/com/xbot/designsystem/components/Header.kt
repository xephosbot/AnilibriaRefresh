package com.xbot.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.ChevronRight
import com.xbot.designsystem.theme.AnilibriaTheme
import com.xbot.designsystem.utils.AnilibriaPreview

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Header(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Header(
        title = title,
        modifier = modifier,
        content = if (onClick != null) {
            {
                FilledTonalIconButton(
                    modifier = Modifier
                        .size(IconButtonDefaults.smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Narrow)),
                    onClick = onClick,
                    shapes = IconButtonDefaults.shapes()
                ) {
                    Icon(
                        modifier = Modifier.size(IconButtonDefaults.smallIconSize),
                        imageVector = AnilibriaIcons.ChevronRight,
                        contentDescription = null
                    )
                }
            }
        } else null
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Header(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (RowScope.() -> Unit)?,
) {
    Box(
        Modifier.padding(
            top = 24.dp,
            bottom = 16.dp,
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProvideTextStyle(MaterialTheme.typography.titleLarge) {
                title()
            }

            if (content != null) {
                Spacer(Modifier.weight(1f))
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                    content.invoke(this)
                }
            }
        }
    }
}


@Preview(name = "Header", widthDp = 278)
@Composable
private fun HeaderPreview() {
    AnilibriaPreview {
        Header(
            title = { Text("Title") },
            onClick = {}
        )
    }
}

@Preview(name = "Header Dark", widthDp = 278)
@Composable
private fun HeaderPreviewDark() {
    AnilibriaPreview(useDarkTheme = true) {
        Header(
            title = { Text("Title") },
            onClick = {}
        )
    }
}