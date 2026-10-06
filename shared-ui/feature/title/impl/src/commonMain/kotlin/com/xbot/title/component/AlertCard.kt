package com.xbot.title.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.icons.AnilibertyIcons
import com.xbot.designsystem.icons.Error
import com.xbot.designsystem.icons.OpenInNew
import com.xbot.designsystem.icons.PublicOff
import com.xbot.designsystem.utils.AnilibertyPreview

@Composable
internal fun AlertCard(
    title: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = AnilibertyIcons.PublicOff,
    text: String? = null,
    action: (@Composable () -> Unit)? = null
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                if (text != null) {
                    Text(text = text, style = MaterialTheme.typography.bodyMedium)
                }
                if (action != null) {
                    Box(modifier = Modifier.padding(top = 10.dp)) { action() }
                }
            }
        }
    }
}

@Composable
internal fun NotificationCard(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = AnilibertyIcons.Error,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                modifier = Modifier.weight(1f),
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@AnilibertyPreview
@Composable
private fun AlertCardPreview() {
    AlertCard(
        icon = AnilibertyIcons.PublicOff,
        title = "Серии недоступны в вашем регионе",
        text = "Правообладатель ограничил показ. Смотреть можно во внешнем плеере.",
        action = {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onErrorContainer,
                    contentColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Icon(
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                    imageVector = AnilibertyIcons.OpenInNew,
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(start = ButtonDefaults.IconSpacing),
                    text = "Открыть внешний плеер"
                )
            }
        }
    )
}

@AnilibertyPreview
@Composable
private fun NotificationCardPreview() {
    NotificationCard(
        text = "Это вторая часть. Сюжет продолжает первый сезон."
    )
}
