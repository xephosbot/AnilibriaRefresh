import Combine
import SwiftUI
import Shared

class IosViewModelStoreOwner: ObservableObject, ViewModelStoreOwner {

    let viewModelStore = ViewModelStore()

    /// Retrieve or create a ViewModel scoped to this owner.
    ///
    /// The ViewModel is cached inside the store — subsequent calls with the same key
    /// return the existing instance. `onCleared()` is called when this owner is deinitialized.
    ///
    /// - Parameters:
    ///   - key:        Override the store key (default: Swift class name). Use when you need
    ///                 multiple instances of the same ViewModel type on one screen.
    ///   - extras:     Optional `CreationExtras` (e.g. custom SavedState). Defaults to empty —
    ///                 `SavedStateHandle` will be empty and Orbit starts from `initialState`.
    ///   - parameters: Koin `ParametersDefinition` lambda for extra constructor args from Swift.
    func viewModel<T: ViewModel>(
        key: String? = nil,
        extras: CreationExtras? = nil,
        parameters: (() -> ParametersHolder)? = nil
    ) -> T {
        do {
            return try viewModelStore.resolveViewModel(
                modelClass: T.self,
                key: key,
                extras: extras ?? CreationExtras.Empty.shared,
                qualifier: nil,
                parameters: parameters
            ) as! T
        } catch {
            fatalError("Failed to create ViewModel of type \(T.self)")
        }
    }
    
    /// This can be called from outside when using the `ViewModelStoreOwnerProvider`
    func clear() {
        viewModelStore.clear()
    }

    /// This is called when this class is used as a `@StateObject`
    deinit {
        viewModelStore.clear()
    }
}
