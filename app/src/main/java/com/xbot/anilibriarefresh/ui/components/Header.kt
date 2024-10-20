package com.xbot.anilibriarefresh.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.ui.utils.only

@Composable
fun Header(
    modifier: Modifier = Modifier,
    title: String,
    contentPadding: PaddingValues = HeaderDefaults.ContentPadding,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick)
                else Modifier
            )
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //TODO: Use MaterialTheme.typography style
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        if (onClick != null) {
            Spacer(Modifier.weight(1f))
            //TODO: Use MaterialTheme.typography style
            Text(
                text = stringResource(R.string.text_favorites_btn),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

object HeaderDefaults {
    val ContentPadding = PaddingValues(16.dp)
    val ContentPaddingExcludeBottom = ContentPadding
        .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
}