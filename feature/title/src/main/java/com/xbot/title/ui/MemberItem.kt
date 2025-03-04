package com.xbot.title.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.components.PosterImage
import com.xbot.domain.models.Member
import com.xbot.title.utils.stringRes

@Composable
internal fun MemberItem(
    member: Member,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PosterImage(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .clickable { onClick() },
            poster = member.avatar
        )
        Spacer(Modifier.height(4.dp))
        Text(
            modifier = Modifier.width(IntrinsicSize.Min),
            text = member.name,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )
        member.role?.let { role ->
            Text(
                modifier = Modifier.width(IntrinsicSize.Min),
                text = stringResource(role.stringRes),
                style = MaterialTheme.typography.bodySmall,
                color = LocalContentColor.current.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}