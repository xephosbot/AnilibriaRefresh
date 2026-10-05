package com.xbot.domain.usecase

import com.xbot.data.repository.AppearanceRepository
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
internal class DefaultUpdatePureBlackUseCase(private val repository: AppearanceRepository) :
    UpdatePureBlackUseCase {
    override suspend fun invoke(enabled: Boolean) {
        repository.setPureBlack(enabled)
    }
}
