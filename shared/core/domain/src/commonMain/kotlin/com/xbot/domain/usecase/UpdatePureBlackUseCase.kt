package com.xbot.domain.usecase

import com.xbot.domain.repository.AppearanceRepository
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class UpdatePureBlackUseCase(private val repository: AppearanceRepository) :
    UseCase<UpdatePureBlackUseCase.Params, Unit> {
    data class Params(val enabled: Boolean)

    override suspend fun invoke(params: Params) {
        repository.setPureBlack(params.enabled)
    }
}
