package com.xbot.designsystem.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ExperimentalMaterial3ComponentOverrideApi
import androidx.compose.material3.NavigationBarComponentOverride
import androidx.compose.material3.NavigationBarComponentOverrideContext
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ComponentOverrideApi::class)
object AnilibriaNavigationBar : NavigationBarComponentOverride {
    @Composable
    override fun NavigationBarComponentOverrideContext.NavigationBar() {
        Surface(
            color = containerColor,
            contentColor = contentColor,
            tonalElevation = tonalElevation,
            modifier = modifier
        ) {
            Row(
                modifier =
                Modifier.fillMaxWidth()
                    .windowInsetsPadding(windowInsets)
                    .height(56.dp)
                    .selectableGroup(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}
