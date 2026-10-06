package com.xbot.title.screen.details

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.intl.Locale
import com.xbot.designsystem.icons.AnilibertyIcons
import com.xbot.designsystem.icons.Bookmark
import com.xbot.designsystem.icons.Cancel
import com.xbot.designsystem.icons.PauseCircle
import com.xbot.designsystem.icons.TaskAlt
import com.xbot.designsystem.icons.Visibility
import com.xbot.domain.models.enums.CollectionType
import com.xbot.localization.LocalAppLanguage
import com.xbot.resources.Res
import com.xbot.resources.release_details_collection_abandoned
import com.xbot.resources.release_details_collection_planned
import com.xbot.resources.release_details_collection_postponed
import com.xbot.resources.release_details_collection_watched
import com.xbot.resources.release_details_collection_watching
import kotlin.time.Duration
import org.jetbrains.compose.resources.StringResource

internal val CollectionStatusOrder = listOf(
    CollectionType.WATCHING,
    CollectionType.PLANNED,
    CollectionType.WATCHED,
    CollectionType.POSTPONED,
    CollectionType.ABANDONED
)

internal val CollectionType.labelRes: StringResource
    get() = when (this) {
        CollectionType.WATCHING -> Res.string.release_details_collection_watching
        CollectionType.PLANNED -> Res.string.release_details_collection_planned
        CollectionType.WATCHED -> Res.string.release_details_collection_watched
        CollectionType.POSTPONED -> Res.string.release_details_collection_postponed
        CollectionType.ABANDONED -> Res.string.release_details_collection_abandoned
    }

internal val CollectionType.icon: ImageVector
    get() = when (this) {
        CollectionType.WATCHING -> AnilibertyIcons.Visibility
        CollectionType.PLANNED -> AnilibertyIcons.Bookmark
        CollectionType.WATCHED -> AnilibertyIcons.TaskAlt
        CollectionType.POSTPONED -> AnilibertyIcons.PauseCircle
        CollectionType.ABANDONED -> AnilibertyIcons.Cancel
    }

@Composable
internal fun appLocale(): Locale = Locale(LocalAppLanguage.current)

internal fun Int.formatGrouped(): String = toString()
    .reversed()
    .chunked(DIGIT_GROUP_SIZE)
    .joinToString("\u00A0")
    .reversed()

internal fun Duration.formatPlayback(): String = toComponents { hours, minutes, seconds, _ ->
    val mm = minutes.toString().padStart(2, '0')
    val ss = seconds.toString().padStart(2, '0')
    if (hours > 0) "$hours:$mm:$ss" else "$minutes:$ss"
}

private const val DIGIT_GROUP_SIZE = 3
