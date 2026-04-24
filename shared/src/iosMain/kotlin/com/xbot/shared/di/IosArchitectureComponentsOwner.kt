package com.xbot.shared.di

import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.SAVED_STATE_REGISTRY_OWNER_KEY
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.enableSavedStateHandles
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner

/**
 * A full-featured architecture components owner for iOS/Swift usage.
 */
class IosArchitectureComponentsOwner :
    LifecycleOwner,
    ViewModelStoreOwner,
    SavedStateRegistryOwner,
    HasDefaultViewModelProviderFactory {

    override val lifecycle: LifecycleRegistry = LifecycleRegistry.createUnsafe(this)
    override val viewModelStore: ViewModelStore = ViewModelStore()

    private val savedStateController = SavedStateRegistryController.create(this)

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateController.savedStateRegistry

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory =
        SavedStateViewModelFactory()

    override val defaultViewModelCreationExtras: CreationExtras =
        MutableCreationExtras().also {
            it[SAVED_STATE_REGISTRY_OWNER_KEY] = this
            it[VIEW_MODEL_STORE_OWNER_KEY] = this
        }

    init {
        savedStateController.performAttach()
        savedStateController.performRestore(null)
        enableSavedStateHandles()
        lifecycle.currentState = Lifecycle.State.CREATED
    }

    fun onStart() {
        lifecycle.currentState = Lifecycle.State.STARTED
    }

    fun onResume() {
        lifecycle.currentState = Lifecycle.State.RESUMED
    }

    fun onStop(targetState: Lifecycle.State = Lifecycle.State.CREATED) {
        lifecycle.currentState = targetState
    }

    fun clear() {
        lifecycle.currentState = Lifecycle.State.DESTROYED
        viewModelStore.clear()
    }
}
