package com.xbot.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.error.InstanceCreationException
import org.koin.core.error.NoDefinitionFoundException
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.mp.KoinPlatform
import org.koin.viewmodel.defaultExtras
import org.koin.viewmodel.resolveViewModel
import kotlin.reflect.KClass

@OptIn(BetaInteropApi::class, KoinInternalApi::class)
@Throws(
    IllegalStateException::class,
    NoDefinitionFoundException::class,
    InstanceCreationException::class,
    UnsupportedOperationException::class
)
fun ViewModelStore.resolveViewModel(
    modelClass: ObjCClass,
    owner: ViewModelStoreOwner,
    key: String? = null,
    extras: CreationExtras? = null,
    qualifier: Qualifier? = null,
    parameters: ParametersDefinition? = null,
): ViewModel {
    @Suppress("UNCHECKED_CAST")
    val vmClass = getOriginalKotlinClass(modelClass) as? KClass<out ViewModel>
    require(vmClass != null) { "modelClass must be a ViewModel subtype." }

    return resolveViewModel(
        vmClass = vmClass,
        viewModelStore = this,
        key = key,
        extras = extras ?: defaultExtras(owner),
        qualifier = qualifier,
        scope = KoinPlatform.getKoinOrNull()?.scopeRegistry?.rootScope ?: error("No Koin scope available"),
        parameters = parameters,
    )
}
