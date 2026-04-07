package com.xbot.domain.usecase

import com.xbot.data.repository.AppearanceRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetDynamicThemeUseCase(
    private val appearanceRepository: AppearanceRepository
) : GetDynamicThemeUseCase {
    override fun invoke(): Flow<Boolean> {
        return appearanceRepository.isDynamicTheme
    }
}
