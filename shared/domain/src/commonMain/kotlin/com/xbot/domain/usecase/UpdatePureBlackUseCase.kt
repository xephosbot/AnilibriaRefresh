package com.xbot.domain.usecase

import com.xbot.domain.repository.AppearanceRepository

class UpdatePureBlackUseCase(
    private val repository: AppearanceRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        repository.setPureBlack(enabled)
    }
}