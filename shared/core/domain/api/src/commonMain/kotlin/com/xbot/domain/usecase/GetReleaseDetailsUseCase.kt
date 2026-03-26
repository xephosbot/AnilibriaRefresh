package com.xbot.domain.usecase

import com.xbot.domain.models.ReleaseDetailsExtended
import kotlinx.coroutines.flow.Flow

fun interface GetReleaseDetailsUseCase {
    operator fun invoke(aliasOrId: String): Flow<ReleaseDetailsExtended>
}
