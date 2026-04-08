package com.xbot.title

import com.xbot.common.AsyncResult
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetails

data class TitleScreenState(
    val initialRelease: Release? = null,
    val details: AsyncResult<DomainError, ReleaseDetails> = AsyncResult.Loading,
    val relatedReleases: AsyncResult<DomainError, List<Release>> = AsyncResult.Loading,
) {
    val isWatchButtonVisible: Boolean
        get() = details is AsyncResult.Success && details.data.episodes.firstOrNull() != null
}
