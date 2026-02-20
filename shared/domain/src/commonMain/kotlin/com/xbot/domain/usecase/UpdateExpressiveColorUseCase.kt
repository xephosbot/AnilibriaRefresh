package com.xbot.domain.usecase

import com.xbot.domain.repository.AppearanceRepository
import org.koin.core.annotation.Factory

@Factory
class UpdateExpressiveColorUseCase(
    private val repository: AppearanceRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        repository.setExpressiveColor(enabled)
    }
}