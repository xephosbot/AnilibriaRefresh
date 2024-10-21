package com.xbot.anilibriarefresh.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun IconComponent(modifier: Modifier = Modifier, icon: ImageVector, onClick: () -> Unit) {

    Box(modifier = modifier
        .size(50.dp)
        .clip(RoundedCornerShape(100))
        .background(MaterialTheme.colorScheme.surface.copy(0.7f))
        .clickable { onClick() }) {
        Icon(
            modifier = Modifier.size(32.dp).align(Alignment.Center),
            imageVector = icon,
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = ""
        )
    }
}