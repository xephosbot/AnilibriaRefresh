import Combine
import SwiftUI
import Shared

/// Owns an `IosArchitectureComponentsOwner` (the SwiftUI analog of Compose Multiplatform's
/// `DefaultArchitectureComponentsOwner`) and bridges SwiftUI lifecycle into it.
/// Hold it as a `@StateObject` — one per screen you want to scope ViewModels to.
class IosViewModelStoreOwner: ObservableObject {

    let componentsOwner: IosArchitectureComponentsOwner

    /// - Parameter savedState: state captured from a previous owner via `saveState()`, used to
    ///   restore ViewModel `SavedStateHandle`s. Pass `nil` on a cold start.
    init(savedState: SavedState? = nil) {
        componentsOwner = IosArchitectureComponentsOwner(savedState: savedState, enforceMainThread: true)
        // Mirror CMP: move out of INITIALIZED right after construction so ViewModels can be
        // resolved (and their SavedStateHandles created) before the view appears.
        componentsOwner.setLifecycleState(state: .created)
    }

    /// Retrieve or create a ViewModel scoped to this owner.
    ///
    /// The ViewModel is cached inside the store — subsequent calls with the same key
    /// return the existing instance. `onCleared()` is called when this owner is destroyed.
    ///
    /// - Parameters:
    ///   - key:        Override the store key (default: Swift class name). Use when you need
    ///                 multiple instances of the same ViewModel type on one screen.
    ///   - extras:     Optional `CreationExtras`. Defaults to the owner's extras
    ///                 which include `SAVED_STATE_REGISTRY_OWNER_KEY`.
    ///   - parameters: Koin `ParametersDefinition` lambda for extra constructor args from Swift.
    func viewModel<T: ViewModel>(
        key: String? = nil,
        extras: CreationExtras? = nil,
        parameters: (() -> ParametersHolder)? = nil
    ) -> T {
        do {
            return try componentsOwner.viewModelStore.resolveViewModel(
                modelClass: T.self,
                owner: componentsOwner,
                key: key,
                extras: extras,
                qualifier: nil,
                parameters: parameters
            ) as! T
        } catch {
            fatalError("Failed to create ViewModel of type \(T.self)")
        }
    }

    /// Captures current state (incl. every ViewModel's `SavedStateHandle`) so it can be restored
    /// into a future owner via `init(savedState:)`. Mirrors `ComposeContainer` storing
    /// `architectureComponentsOwner.saveState()` when the scene is disposed.
    func saveState() -> SavedState {
        componentsOwner.saveState()
    }

    // MARK: - SwiftUI → Lifecycle adapter
    // (the SwiftUI counterpart of CMP's `lifecycleDelegate.onLifecycleStateUpdated`)

    /// CREATED → STARTED → RESUMED. Call when the view appears.
    func onAppear(scenePhase: ScenePhase) {
        componentsOwner.setLifecycleState(state: scenePhase == .active ? .resumed : .started)
    }

    /// Back to CREATED. Call when the view disappears.
    func onDisappear() {
        componentsOwner.setLifecycleState(state: .created)
    }

    /// Reacts to SwiftUI `scenePhase` changes.
    /// - `.active`     → RESUMED
    /// - `.inactive`   → STARTED
    /// - `.background` → CREATED
    func onScenePhaseChanged(_ phase: ScenePhase) {
        switch phase {
        case .active:     componentsOwner.setLifecycleState(state: .resumed)
        case .inactive:   componentsOwner.setLifecycleState(state: .started)
        case .background: componentsOwner.setLifecycleState(state: .created)
        @unknown default: break
        }
    }

    /// CREATED → DESTROYED. Clears the ViewModelStore.
    func clear() {
        componentsOwner.setLifecycleState(state: .destroyed)
    }

    deinit {
        componentsOwner.setLifecycleState(state: .destroyed)
    }
}
