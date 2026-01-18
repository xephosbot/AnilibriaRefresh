package com.xbot.domain.usecase

import com.xbot.domain.repository.AppearanceRepository

class UpdateExpressiveColorUseCase(
    private val repository: AppearanceRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        repository.setExpressiveColor(enabled)
    }
}