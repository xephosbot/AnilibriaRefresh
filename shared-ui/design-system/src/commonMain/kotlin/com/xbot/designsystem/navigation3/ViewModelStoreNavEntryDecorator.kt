package com.xbot.designsystem.navigation3

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SAVED_STATE_REGISTRY_OWNER_KEY
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.navEntryDecorator
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.compose.LocalSavedStateRegistryOwner

/**
 * Returns a [ViewModelStoreNavEntryDecorator] that is remembered across recompositions.
 *
 * @param [viewModelStoreOwner] The [ViewModelStoreOwner] that provides the [ViewModelStore] to
 *   NavEntries
 * @param [removeViewModelStoreOnPop] A lambda that returns a Boolean for whether the store for a
 *   [NavEntry] should be removed when the [NavEntry] is popped from the backStack. If true, the
 *   entry's ViewModelStore will be removed.
 */
@Composable
fun <T : Any> rememberViewModelStoreNavEntryDecorator(
    viewModelStoreOwner: ViewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) {
            "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
        },
    removeViewModelStoreOnPop: () -> Boolean =
        ViewModelStoreNavEntryDecoratorDefault.removeViewModelStoreOnPop(),
): NavEntryDecorator<T> {
    val currentRemoveViewModelStoreOnPop = rememberUpdatedState(removeViewModelStoreOnPop)
    return remember(viewModelStoreOwner, currentRemoveViewModelStoreOnPop) {
        ViewModelStoreNavEntryDecorator(
            viewModelStoreOwner.viewModelStore,
            removeViewModelStoreOnPop,
        )
    }
}

/**
 * Provides the content of a [NavEntry] with a [ViewModelStoreOwner] and provides that
 * [ViewModelStoreOwner] as a [LocalViewModelStoreOwner] so that it is available within the content.
 *
 * This requires the usage of [androidx.navigation3.runtime.SaveableStateHolderNavEntryDecorator] to
 * ensure that the [NavEntry] scoped [ViewModel]s can properly provide access to
 * [androidx.lifecycle.SavedStateHandle]s
 *
 * @param [viewModelStore] The [ViewModelStore] that provides to NavEntries
 * @param [removeViewModelStoreOnPop] A lambda that returns a Boolean for whether the store for a
 *   [NavEntry] should be cleared when the [NavEntry] is popped from the backStack. If true, the
 *   entry's ViewModelStore will be removed.
 * @see NavEntryDecorator.onPop for more details on when this callback is invoked
 */
fun< T : Any> ViewModelStoreNavEntryDecorator(
    viewModelStore: ViewModelStore,
    removeViewModelStoreOnPop: () -> Boolean,
) = navEntryDecorator<T>(
        onPop = ({ key ->
            if (removeViewModelStoreOnPop()) {
                viewModelStore.getEntryViewModel().clearViewModelStoreOwnerForKey(key)
            }
        }),
        decorator = { entry ->
            val viewModelStore =
                viewModelStore.getEntryViewModel().viewModelStoreForKey(entry.contentKey)

            val savedStateRegistryOwner = LocalSavedStateRegistryOwner.current
            val childViewModelStoreOwner = remember {
                object :
                    ViewModelStoreOwner,
                    SavedStateRegistryOwner by savedStateRegistryOwner,
                    HasDefaultViewModelProviderFactory {
                    override val viewModelStore: ViewModelStore
                        get() = viewModelStore

                    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
                        get() = SavedStateViewModelFactory()

                    override val defaultViewModelCreationExtras: CreationExtras
                        get() =
                            MutableCreationExtras().also {
                                it[SAVED_STATE_REGISTRY_OWNER_KEY] = this
                                it[VIEW_MODEL_STORE_OWNER_KEY] = this
                            }

                    init {
                        require(this.lifecycle.currentState == Lifecycle.State.INITIALIZED) {
                            "The Lifecycle state is already beyond INITIALIZED. The " +
                                    "ViewModelStoreNavEntryDecorator requires adding the " +
                                    "SavedStateNavEntryDecorator to ensure support for " +
                                    "SavedStateHandles."
                        }
                        //enableSavedStateHandles()
                    }
                }
            }
            CompositionLocalProvider(LocalViewModelStoreOwner provides childViewModelStoreOwner) {
                entry.Content()
            }
        },
    )

private class EntryViewModel : ViewModel() {
    private val owners = mutableMapOf<Any, ViewModelStore>()

    fun viewModelStoreForKey(key: Any): ViewModelStore = owners.getOrPut(key) { ViewModelStore() }

    fun clearViewModelStoreOwnerForKey(key: Any) {
        owners.remove(key)?.clear()
    }

    override fun onCleared() {
        owners.forEach { (_, store) -> store.clear() }
    }
}

/** Holds the default functions for the [ViewModelStoreNavEntryDecorator]. */
public object ViewModelStoreNavEntryDecoratorDefault {
    /**
     * Controls whether the [ViewModelStoreNavEntryDecorator] should clear the ViewModelStore scoped
     * to a [NavEntry] when [NavEntryDecorator.onPop] is invoked for that [NavEntry]'s
     * [NavEntry.contentKey]
     *
     * The ViewModelStore is cleared if this returns true. The store is retained if false.
     */
    @Composable public fun removeViewModelStoreOnPop(): () -> Boolean {
        return { true }
    }
}

private fun ViewModelStore.getEntryViewModel(): EntryViewModel {
    val provider =
        ViewModelProvider.create(
            store = this,
            factory = viewModelFactory { initializer { EntryViewModel() } },
        )
    return provider[EntryViewModel::class]
}