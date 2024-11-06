package com.xbot.anilibriarefresh.ui.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.xbot.anilibriarefresh.ui.icons.AnilibriaIcons
import com.xbot.anilibriarefresh.ui.icons.Heart
import com.xbot.domain.model.TitleModel

@Composable
fun rememberTitleModelTags(title: TitleModel): List<TagData> {
    val context = LocalContext.current
    return remember(title, context) { title.tags(context) }
}

//TODO: Move string to resources
fun TitleModel.tags(context: Context): List<TagData> {
    return buildList {
        add(TagData.Text(year.toString()))
        if (type != "Фильм" && episodesCount != null) {
            add(TagData.Text("$episodesCount эп."))
        } else if (type == "Фильм") {
            add(TagData.Text(type))
        }
        add(TagData.TextWithIcon(favoritesCount.toString(), AnilibriaIcons.Filled.Heart))
    }
}

sealed interface TagData {
    data class Text(val text: String): TagData
    data class TextWithIcon(
        val text: String,
        val icon: ImageVector
    ): TagData
}