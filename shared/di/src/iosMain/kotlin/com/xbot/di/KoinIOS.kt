package com.xbot.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.mp.KoinPlatform
import org.koin.viewmodel.factory.KoinViewModelFactory
import kotlin.reflect.KClass

/**
 * Resolve a ViewModel from Koin and place it in this ViewModelStore.
 *
 * Lifecycle: ViewModel is created once per store (cached by ViewModelProvider),
 * onCleared() is called when the store is cleared.
 *
 * @param extras     CreationExtras from Swift (default: Empty — SavedStateHandle will be empty,
 *                   Orbit starts from initialState; state persists within a session only)
 * @param parameters Koin ParametersDefinition for extra constructor arguments passed from Swift
 */
@OptIn(BetaInteropApi::class, KoinInternalApi::class)
@Throws(IllegalArgumentException::class)
fun ViewModelStore.resolveViewModel(
    modelClass: ObjCClass,
    key: String? = null,
    extras: CreationExtras = CreationExtras.Empty,
    qualifier: Qualifier? = null,
    parameters: ParametersDefinition? = null,
): ViewModel {
    @Suppress("UNCHECKED_CAST")
    val vmClass = getOriginalKotlinClass(modelClass) as? KClass<out ViewModel>
    require(vmClass != null) { "modelClass must be a ViewModel subtype." }

    val koin = KoinPlatform.getKoin()
    val factory = KoinViewModelFactory(vmClass, koin.scopeRegistry.rootScope, qualifier, parameters)
    val provider = ViewModelProvider.create(this, factory, extras)
    return key?.let { provider[it, vmClass] } ?: provider[vmClass]
}
