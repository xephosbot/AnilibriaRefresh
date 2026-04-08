package com.xbot.domain.usecase

import com.xbot.data.repository.AppearanceRepository
import com.xbot.domain.models.enums.ThemeOption
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetThemeOptionUseCase(
    private val appearanceRepository: AppearanceRepository
) : GetThemeOptionUseCase {
    override fun invoke(): Flow<ThemeOption> {
        return appearanceRepository.themeOption
    }
}
