package com.xbot.domain.usecase

import com.xbot.domain.repository.AppearanceRepository
import kotlin.native.HiddenFromObjC
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class GetExpressiveColorUseCase(private val appearanceRepository: AppearanceRepository) :
    FlowUseCase<Unit, Boolean> {
    override fun invoke(params: Unit): Flow<Boolean> = appearanceRepository.isExpressiveColor
}
