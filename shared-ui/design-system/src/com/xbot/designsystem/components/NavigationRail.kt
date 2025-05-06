package com.xbot.designsystem.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ComponentOverrideApi::class)
object AnilibriaNavigationRail : NavigationRailComponentOverride {
    @Composable
    override fun NavigationRailComponentOverrideContext.NavigationRail() {
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
                    header!!.invoke(this)
                    Spacer(Modifier.height(8.dp))
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    content = content,
                )
            }
        }
    }
}
