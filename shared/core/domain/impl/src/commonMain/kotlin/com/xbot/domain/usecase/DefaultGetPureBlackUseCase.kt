package com.xbot.domain.usecase

import com.xbot.data.repository.AppearanceRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetPureBlackUseCase(
    private val appearanceRepository: AppearanceRepository
) : GetPureBlackUseCase {
    override fun invoke(): Flow<Boolean> {
        return appearanceRepository.isPureBlack
    }
}
