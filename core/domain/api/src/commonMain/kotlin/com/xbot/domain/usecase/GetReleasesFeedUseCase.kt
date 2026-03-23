package com.xbot.domain.usecase

import com.xbot.domain.models.ReleasesFeed
import kotlinx.coroutines.flow.Flow

fun interface GetReleasesFeedUseCase {
    fun invoke(): Flow<ReleasesFeed>
}
