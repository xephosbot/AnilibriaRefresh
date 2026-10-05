package com.xbot.domain.usecase

import com.xbot.data.repository.AppearanceRepository
import kotlin.native.HiddenFromObjC
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
internal class DefaultGetPureBlackUseCase(private val appearanceRepository: AppearanceRepository) :
    GetPureBlackUseCase {
    override fun invoke(): Flow<Boolean> = appearanceRepository.isPureBlack
}
