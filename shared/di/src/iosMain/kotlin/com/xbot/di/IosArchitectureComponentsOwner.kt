package com.xbot.di

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
 *
 * Implements [ViewModelStoreOwner], [SavedStateRegistryOwner], [LifecycleOwner],
 * and [HasDefaultViewModelProviderFactory] so that Koin's `defaultExtras` picks up
 * the [SAVED_STATE_REGISTRY_OWNER_KEY] required by `SavedStateHandle`-dependent ViewModels.
 *
 * ## Lifecycle mapping (JetBrains docs)
 *
 * | iOS event                              | Call       | State transition       |
 * |----------------------------------------|------------|------------------------|
 * | `viewWillAppear` / `willEnterForeground` | [onStart]  | CREATED → STARTED      |
 * | `didBecomeActive`                      | [onResume] | STARTED → RESUMED      |
 * | `willResignActive`                     | [onStop]   | RESUMED → STARTED      |
 * | `viewDidDisappear` / `didEnterBackground` | [onStop] | STARTED → CREATED      |
 * | `viewControllerDidLeaveWindowHierarchy` | [clear]   | CREATED → DESTROYED    |
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

    /** CREATED → STARTED. Call on `viewWillAppear` / `willEnterForeground`. */
    fun onStart() {
        lifecycle.currentState = Lifecycle.State.STARTED
    }

    /** STARTED → RESUMED. Call on `didBecomeActive`. */
    fun onResume() {
        lifecycle.currentState = Lifecycle.State.RESUMED
    }

    /**
     * Moves lifecycle down to [targetState].
     * Call on `willResignActive` (→ STARTED) or `viewDidDisappear` / `didEnterBackground` (→ CREATED).
     *
     * [LifecycleRegistry] dispatches intermediate events automatically,
     * e.g. RESUMED → CREATED dispatches ON_PAUSE then ON_STOP.
     */
    fun onStop(targetState: Lifecycle.State = Lifecycle.State.CREATED) {
        lifecycle.currentState = targetState
    }

    /** CREATED → DESTROYED. Clears the [ViewModelStore]. */
    fun clear() {
        lifecycle.currentState = Lifecycle.State.DESTROYED
        viewModelStore.clear()
    }
}


