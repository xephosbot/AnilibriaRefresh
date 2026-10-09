package com.xbot.title.component

import androidx.compose.runtime.Immutable
import com.xbot.domain.models.enums.AvailabilityStatus
import com.xbot.resources.Res
import com.xbot.resources.StringResource
import com.xbot.resources.release_details_blocked_body
import com.xbot.resources.release_details_blocked_copyright_title
import com.xbot.resources.release_details_blocked_geo_title
import com.xbot.title.TitleScreenState

@Immutable
internal sealed interface ReleaseStatusBanner {
    data object None : ReleaseStatusBanner

    sealed interface Blocked : ReleaseStatusBanner {
        val title: StringResource
        val hasExternalPlayer: Boolean

        val description: StringResource?
            get() = StringResource.Text(Res.string.release_details_blocked_body)
                .takeIf { hasExternalPlayer }
    }

    data class GeoBlocked(override val hasExternalPlayer: Boolean) : Blocked {
        override val title: StringResource =
            StringResource.Text(Res.string.release_details_blocked_geo_title)
    }

    data class CopyrightBlocked(override val hasExternalPlayer: Boolean) : Blocked {
        override val title: StringResource =
            StringResource.Text(Res.string.release_details_blocked_copyright_title)
    }

    data class Notification(val text: StringResource) : ReleaseStatusBanner
}

internal val TitleScreenState.statusBanner: ReleaseStatusBanner
    get() {
        val details = releaseDetails ?: return ReleaseStatusBanner.None
        val hasExternalPlayer = details.externalPlayerUrl != null
        return when (details.availabilityStatus) {
            AvailabilityStatus.GeoBlocked -> ReleaseStatusBanner.GeoBlocked(hasExternalPlayer)

            AvailabilityStatus.CopyrightBlocked ->
                ReleaseStatusBanner.CopyrightBlocked(hasExternalPlayer)

            AvailabilityStatus.Available ->
                details.notification
                    ?.let { ReleaseStatusBanner.Notification(StringResource.String(it)) }
                    ?: ReleaseStatusBanner.None
        }
    }
