package com.xbot.domain.usecase

import com.xbot.data.repository.AppearanceRepository
import kotlin.native.HiddenFromObjC
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
internal class DefaultGetDynamicThemeUseCase(
    private val appearanceRepository: AppearanceRepository
) : GetDynamicThemeUseCase {
    override fun invoke(): Flow<Boolean> = appearanceRepository.isDynamicTheme
}
