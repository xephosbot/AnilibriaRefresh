package com.xbot.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.icons.AnilibriaIcons

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Header(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Box(Modifier.padding(top = 8.dp)) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProvideTextStyle(MaterialTheme.typography.titleMedium) {
                title()
            }

            if (onClick != null) {
                Spacer(Modifier.weight(1f))
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Hairline) {
                    FilledTonalIconButton(
                        modifier = Modifier.size(IconButtonDefaults.smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Narrow)),
                        onClick = onClick,
                        shapes = IconButtonDefaults.shapes()
                    ) {
                        Icon(
                            imageVector = AnilibriaIcons.Outlined.ChevronRight,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}
