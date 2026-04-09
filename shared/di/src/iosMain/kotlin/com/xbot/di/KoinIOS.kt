package com.xbot.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.ParametersHolder
import org.koin.core.qualifier.Qualifier
import org.koin.mp.KoinPlatform
import kotlin.reflect.KClass

/**
 * Resolve a ViewModel from Koin and place it in this [ViewModelStore].
 *
 * Lifecycle: ViewModel is created once per store (cached by [ViewModelProvider]),
 * [ViewModel.onCleared] is called when the store is cleared.
 *
 * ## Why not [org.koin.viewmodel.factory.KoinViewModelFactory]?
 *
 * `KoinViewModelFactory` wraps every resolution in `AndroidParametersHolder`, which
 * **always** intercepts `SavedStateHandle::class` and calls `extras.createSavedStateHandle()`.
 * On iOS that call requires `SAVED_STATE_REGISTRY_OWNER_KEY` in [CreationExtras] and crashes.
 *
 * This function replicates the core logic (`scope.getWithParameters`) directly, using
 * [IosParametersHolder] that injects a plain [SavedStateHandle] without touching extras.
 * All other dependencies are still resolved from the Koin graph — behaviour is identical.
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

    val scope = KoinPlatform.getKoin().scopeRegistry.rootScope
    val savedStateHandle = SavedStateHandle()

    val factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: KClass<T>, creationExtras: CreationExtras): T =
            scope.getWithParameters(
                vmClass as KClass<ViewModel>,
                qualifier,
                IosParametersHolder(savedStateHandle, parameters),
            ) as T
    }

    val provider = ViewModelProvider.create(this, factory, extras)
    return key?.let { provider[it, vmClass] } ?: provider[vmClass]
}

/**
 * [ParametersHolder] that serves [SavedStateHandle] from a pre-built instance instead of
 * going through `AndroidParametersHolder.createSavedStateHandle()`.
 * All other types are forwarded to [userParams] or fall through to Koin's DI graph.
 */
private class IosParametersHolder(
    private val savedStateHandle: SavedStateHandle,
    private val userParams: ParametersDefinition? = null,
) : ParametersHolder() {

    private val userHolder by lazy { userParams?.invoke() }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getOrNull(clazz: KClass<*>): T? =
        if (clazz == SavedStateHandle::class) savedStateHandle as T
        else userHolder?.getOrNull(clazz)

    @Suppress("UNCHECKED_CAST")
    override fun <T> elementAt(i: Int, clazz: KClass<*>): T =
        if (clazz == SavedStateHandle::class) savedStateHandle as T
        else userHolder?.elementAt(i, clazz)
            ?: throw IndexOutOfBoundsException("No parameter at index $i for $clazz")
}

