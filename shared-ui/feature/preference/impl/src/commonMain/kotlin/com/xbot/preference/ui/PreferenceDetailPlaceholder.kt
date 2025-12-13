package com.xbot.preference.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.xbot.resources.Res
import com.xbot.resources.preference_select_item_placeholder
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PreferenceDetailPlaceholder(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(Res.string.preference_select_item_placeholder))
    }
}
