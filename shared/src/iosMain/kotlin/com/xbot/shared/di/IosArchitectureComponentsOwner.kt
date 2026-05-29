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
import androidx.savedstate.SavedState
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.savedState

/**
 * Standalone architecture-components owner for iOS/Swift.
 *
 * This is a faithful copy of Compose Multiplatform's internal
 * `androidx.compose.ui.platform.DefaultArchitectureComponentsOwner` (the object CMP creates per
 * [androidx.compose.ui.scene.ComposeContainer]). It plays the role `ComponentActivity` plays on
 * Android: it provides [Lifecycle], [ViewModelStore] and [SavedStateRegistry] so that Koin's
 * `resolveViewModel` + `SavedStateHandle` work outside of a Compose scene, in plain SwiftUI.
 *
 * Differences from CMP, all intentional:
 * - No `NavigationEventDispatcherOwner`: it only feeds Compose's predictive-back into the scene
 *   mediator and is meaningless in SwiftUI (would also require the `androidx.navigationevent` dep).
 * - [enableSavedStateHandles] is called in `init` instead of by the caller. There is exactly one
 *   call site (the Swift owner), it must run while the lifecycle is still INITIALIZED, and CMP's own
 *   standalone instance does the same (`EmptyArchitectureComponentsOwner.apply { enableSavedStateHandles() }`).
 */
class IosArchitectureComponentsOwner(
    savedState: SavedState? = null,
    enforceMainThread: Boolean = true,
) : LifecycleOwner,
    ViewModelStoreOwner,
    HasDefaultViewModelProviderFactory,
    SavedStateRegistryOwner {

    override val lifecycle: LifecycleRegistry =
        if (enforceMainThread) LifecycleRegistry(this) else LifecycleRegistry.createUnsafe(this)

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
        savedStateController.performRestore(savedState)
        enableSavedStateHandles()
    }

    /**
     * Captures the current state — including every ViewModel's [androidx.lifecycle.SavedStateHandle] —
     * into a [SavedState] that can be fed back into a new owner via the `savedState` constructor
     * parameter. Mirrors `DefaultArchitectureComponentsOwner.saveState()`.
     */
    fun saveState(): SavedState {
        val savedState = savedState()
        savedStateController.performSave(savedState)
        return savedState
    }

    /**
     * Single lifecycle entry point — mirrors `DefaultArchitectureComponentsOwner.setLifecycleState`.
     * Moving to [Lifecycle.State.DESTROYED] also clears the [ViewModelStore].
     */
    fun setLifecycleState(state: Lifecycle.State) {
        lifecycle.currentState = state
        if (state == Lifecycle.State.DESTROYED) {
            viewModelStore.clear()
        }
    }
}
