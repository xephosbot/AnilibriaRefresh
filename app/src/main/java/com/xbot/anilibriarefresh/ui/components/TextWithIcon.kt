package com.xbot.anilibriarefresh.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextWithIcon(
    modifier: Modifier = Modifier,
    text: String,
    imageVector: ImageVector,
    iconPosition: IconPosition = IconPosition.START,
    style: TextStyle = LocalTextStyle.current,
    fontSize: TextUnit = TextUnit.Unspecified,
    iconPadding: Dp = 4.dp,
) {
    val fontSizeStyled = when {
        fontSize != TextUnit.Unspecified -> fontSize
        else -> style.fontSize
    }

    val annotatedString = when(iconPosition) {
        IconPosition.START -> buildAnnotatedString {
            appendInlineContent(id = INLINE_CONTENT_ICON_ID)
            appendInlineContent(id = INLINE_CONTENT_SPACER_ID)
            append(text)
        }
        IconPosition.END -> buildAnnotatedString {
            append(text)
            appendInlineContent(id = INLINE_CONTENT_SPACER_ID)
            appendInlineContent(id = INLINE_CONTENT_ICON_ID)
        }
    }
    val inlineContent = mapOf(
        INLINE_CONTENT_ICON_ID to InlineTextContent(
            Placeholder(
                width = fontSizeStyled,
                height = fontSizeStyled,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
            )
        ) {
            Icon(
                imageVector = imageVector,
                modifier = Modifier.fillMaxSize(),
                contentDescription = null
            )
        },
        INLINE_CONTENT_SPACER_ID to InlineTextContent(
            Placeholder(
                width = iconPadding.value.sp,
                height = fontSizeStyled,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
            )
        ) {
            Spacer(modifier = Modifier.size(iconPadding))
        }
    )

    Text(
        modifier = modifier,
        text = annotatedString,
        style = style.merge(
            fontSize = fontSize
        ),
        inlineContent = inlineContent
    )
}

enum class IconPosition {
    START,
    END
}

private const val INLINE_CONTENT_ICON_ID = "iconId"
private const val INLINE_CONTENT_SPACER_ID = "spacedId"