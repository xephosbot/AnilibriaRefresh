import Combine
import SwiftUI
import Shared

class IosViewModelStoreOwner: ObservableObject {

    let componentsOwner = IosArchitectureComponentsOwner()

    /// Retrieve or create a ViewModel scoped to this owner.
    ///
    /// The ViewModel is cached inside the store — subsequent calls with the same key
    /// return the existing instance. `onCleared()` is called when this owner is deinitialized.
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

    // MARK: - Lifecycle

    /// CREATED → STARTED → RESUMED. Call when the view appears.
    func onAppear(scenePhase: ScenePhase) {
        componentsOwner.onStart()
        if scenePhase == .active {
            componentsOwner.onResume()
        }
    }

    /// RESUMED/STARTED → CREATED. Call when the view disappears.
    func onDisappear() {
        componentsOwner.onStop(targetState: .created)
    }

    /// Reacts to SwiftUI `scenePhase` changes (maps to JetBrains lifecycle docs).
    /// - `.active`     → RESUMED  (`didBecomeActive`)
    /// - `.inactive`   → STARTED  (`willResignActive`)
    /// - `.background`  → CREATED  (`didEnterBackground`)
    func onScenePhaseChanged(_ phase: ScenePhase) {
        switch phase {
        case .active:
            componentsOwner.onResume()
        case .inactive:
            componentsOwner.onStop(targetState: .started)
        case .background:
            componentsOwner.onStop(targetState: .created)
        @unknown default:
            break
        }
    }

    /// CREATED → DESTROYED. Clears the ViewModelStore.
    func clear() {
        componentsOwner.clear()
    }

    deinit {
        componentsOwner.clear()
    }
}
