package com.xbot.anilibriarefresh.models

import androidx.compose.ui.graphics.vector.ImageVector
import com.xbot.anilibriarefresh.ui.utils.StringResource

sealed class TitleTag {
    data class Text(val text: StringResource) : TitleTag()
    data class TextWithIcon(val text: StringResource, val icon: ImageVector) : TitleTag()
}
