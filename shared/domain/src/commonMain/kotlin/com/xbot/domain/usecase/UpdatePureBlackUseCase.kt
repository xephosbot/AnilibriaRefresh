package com.xbot.domain.usecase

import com.xbot.domain.repository.AppearanceRepository
import org.koin.core.annotation.Factory

@Factory
class UpdatePureBlackUseCase(
    private val repository: AppearanceRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        repository.setPureBlack(enabled)
    }
}