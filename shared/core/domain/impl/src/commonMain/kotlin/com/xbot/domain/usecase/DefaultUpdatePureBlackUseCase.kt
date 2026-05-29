package com.xbot.domain.usecase

import com.xbot.data.repository.AppearanceRepository
import org.koin.core.annotation.Factory
import kotlin.native.HiddenFromObjC

@Factory
@HiddenFromObjC
internal class DefaultUpdatePureBlackUseCase(
    private val repository: AppearanceRepository
) : UpdatePureBlackUseCase {
    override suspend fun invoke(enabled: Boolean) {
        repository.setPureBlack(enabled)
    }
}
