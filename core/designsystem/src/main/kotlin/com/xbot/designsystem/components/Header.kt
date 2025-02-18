package com.xbot.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.icons.AnilibriaIcons

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
            ProvideTextStyle(MaterialTheme.typography.titleLarge) {
                title()
            }

            if (onClick != null) {
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onClick) {
                    Icon(
                        imageVector = AnilibriaIcons.Outlined.ChevronRight,
                        contentDescription = null
                    )
                }
            }
        }
    }
}
