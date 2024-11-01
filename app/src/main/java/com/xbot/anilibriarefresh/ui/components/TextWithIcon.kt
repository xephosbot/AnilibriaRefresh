package com.xbot.anilibriarefresh.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun TextWithIcon(
    modifier: Modifier = Modifier,
    text: String,
    imageVector: ImageVector,
    fontSize: TextUnit = 18.sp
) {
    val annotatedString = buildAnnotatedString {
        appendInlineContent(id = INLINE_CONTENT_ICON_ID)
        append("  $text")
    }
    val inlineContent = mapOf(
        INLINE_CONTENT_ICON_ID to InlineTextContent(
            Placeholder(
                width = fontSize,
                height = fontSize,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
            )
        ) {
            Icon(
                imageVector = imageVector,
                modifier = Modifier.fillMaxSize(),
                contentDescription = null
            )
        }
    )

    Text(
        modifier = modifier,
        text = annotatedString,
        fontSize = fontSize,
        inlineContent = inlineContent
    )
}

private const val INLINE_CONTENT_ICON_ID = "iconId"