package com.xbot.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NavigationRail(
    modifier: Modifier = Modifier,
    containerColor: Color = NavigationRailDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    header: @Composable (ColumnScope.() -> Unit)? = null,
    windowInsets: WindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout).only(
        WindowInsetsSides.Vertical + WindowInsetsSides.Start,
    ),
    contentArrangement: Arrangement.Vertical = Arrangement.Bottom,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        color = containerColor,
        contentColor = contentColor,
        modifier = modifier,
    ) {
        Column(
            Modifier.fillMaxHeight()
                .windowInsetsPadding(windowInsets)
                .widthIn(min = 80.dp)
                .padding(vertical = 4.dp)
                .selectableGroup(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (header != null) {
                header()
                Spacer(Modifier.height(8.dp))
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = contentArrangement,
                content = content,
            )
        }
    }
}