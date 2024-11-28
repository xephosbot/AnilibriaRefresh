package com.xbot.anilibriarefresh.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SmallButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = contentColorFor(containerColor),
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = modifier.semantics { role = Role.Button },
        onClick = onClick,
        color = containerColor,
        contentColor = contentColor,
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