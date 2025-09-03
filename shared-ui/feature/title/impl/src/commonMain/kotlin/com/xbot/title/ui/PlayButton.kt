package com.xbot.shared.ui.feature.title.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xbot.resources.Res
import com.xbot.resources.button_watch_continue
import com.xbot.designsystem.components.SplitButtonLayout
import com.xbot.designsystem.icons.AnilibriaIcons
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun PlayButton(
    modifier: Modifier = Modifier,
    onLeadingClick: () -> Unit,
    onTrailingClick: () -> Unit,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.1f),
        disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.38f),
    ),
    trailingEnabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(8.dp),
) {
    SplitButtonLayout(
        modifier = modifier.fillMaxWidth(),
        leadingButton = {
            SplitButtonDefaults.LeadingButton(
                modifier = Modifier.fillMaxSize(),
                contentPadding = contentPadding,
                colors = colors,
                onClick = onLeadingClick
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onTertiary) {
                        Icon(
                            imageVector = AnilibriaIcons.Outlined.PlayArrow,
                            contentDescription = null
                        )
                    }
                }
                Spacer(Modifier.weight(1.0f))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(Res.string.button_watch_continue),
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Серия 1",
                        style = MaterialTheme.typography.labelMedium,
                        color = LocalContentColor.current.copy(alpha = 0.6f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.weight(1.0f))
            }
        },
        trailingButton = {
            SplitButtonDefaults.TrailingButton(
                modifier = Modifier.fillMaxSize(),
                enabled = trailingEnabled,
                contentPadding = contentPadding,
                colors = colors,
                onClick = onTrailingClick
            ) {
                Box(
                    modifier = Modifier.size(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = AnilibriaIcons.Outlined.PlayList,
                        contentDescription = null
                    )
                }
            }
        }
    )
}