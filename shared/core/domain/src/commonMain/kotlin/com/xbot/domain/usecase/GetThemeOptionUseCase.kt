package com.xbot.domain.usecase

import com.xbot.domain.models.enums.ThemeOption
import com.xbot.domain.repository.AppearanceRepository
import kotlin.native.HiddenFromObjC
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class GetThemeOptionUseCase(private val appearanceRepository: AppearanceRepository) :
    FlowUseCase<Unit, ThemeOption> {
    override fun invoke(params: Unit): Flow<ThemeOption> = appearanceRepository.themeOption
}
