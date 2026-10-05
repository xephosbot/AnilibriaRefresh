package com.xbot.domain.usecase

import com.xbot.data.repository.AppearanceRepository
import kotlin.native.HiddenFromObjC
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
internal class DefaultGetExpressiveColorUseCase(
    private val appearanceRepository: AppearanceRepository
) : GetExpressiveColorUseCase {
    override fun invoke(): Flow<Boolean> = appearanceRepository.isExpressiveColor
}
