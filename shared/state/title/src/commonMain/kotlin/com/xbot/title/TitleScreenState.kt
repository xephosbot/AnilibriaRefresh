package com.xbot.title

import com.xbot.common.AsyncResult
import com.xbot.common.error.AppError
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetails

data class TitleScreenState(
    val initialRelease: Release? = null,
    val details: AsyncResult<AppError, ReleaseDetails> = AsyncResult.Loading,
    val relatedReleases: AsyncResult<AppError, List<Release>> = AsyncResult.Loading,
)
