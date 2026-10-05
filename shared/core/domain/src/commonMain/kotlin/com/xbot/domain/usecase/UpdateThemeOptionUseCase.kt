package com.xbot.domain.usecase

import com.xbot.domain.models.enums.ThemeOption
import com.xbot.domain.repository.AppearanceRepository
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class UpdateThemeOptionUseCase(private val repository: AppearanceRepository) :
    UseCase<UpdateThemeOptionUseCase.Params, Unit> {
    data class Params(val option: ThemeOption)

    override suspend fun invoke(params: Params) {
        repository.setThemeOption(params.option)
    }
}
