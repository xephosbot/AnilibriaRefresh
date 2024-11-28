package com.xbot.anilibriarefresh.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Header(
    title: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // TODO: Use MaterialTheme.typography style
        Text(
            text = title,
            fontSize = 16.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight.Medium,
        )

        if (onClick != null) {
            Spacer(Modifier.weight(1f))
            SmallButton(
                onClick = onClick
            ) {
                Text(text = "See all")
            }
        }
    }
}