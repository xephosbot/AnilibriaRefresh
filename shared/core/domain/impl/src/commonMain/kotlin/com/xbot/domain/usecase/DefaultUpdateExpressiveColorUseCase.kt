package com.xbot.domain.usecase

import com.xbot.data.repository.AppearanceRepository
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
internal class DefaultUpdateExpressiveColorUseCase(private val repository: AppearanceRepository) :
    UpdateExpressiveColorUseCase {
    override suspend fun invoke(enabled: Boolean) {
        repository.setExpressiveColor(enabled)
    }
}
