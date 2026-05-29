//
// Created by xephosbot on 08/04/26.
//

import SwiftUI
import Shared

struct ViewModelStoreOwnerProvider<Content: View>: View {
    @StateObject private var viewModelStoreOwner: IosViewModelStoreOwner
    @Environment(\.scenePhase) private var scenePhase
    @State private var isAppeared = false
    /// Last captured state. Faithful to `ComposeContainer`, which keeps the
    /// `architectureComponentsOwner.saveState()` result in a field. A parent navigation host can
    /// own/persist it and feed it back via `init(savedState:)` when a screen is re-created.
    @State private var savedState: SavedState?

    private let content: Content

    /// - Parameter savedState: optional state from a previous owner to restore (e.g. provided by a
    ///   parent so a re-created screen keeps its ViewModels' `SavedStateHandle`s). `nil` on cold start.
    init(savedState: SavedState? = nil, @ViewBuilder content: () -> Content) {
        _viewModelStoreOwner = StateObject(wrappedValue: IosViewModelStoreOwner(savedState: savedState))
        self.content = content()
    }

    var body: some View {
        content
            .environmentObject(viewModelStoreOwner)
            .onAppear {
                isAppeared = true
                viewModelStoreOwner.onAppear(scenePhase: scenePhase)
            }
            .onDisappear {
                isAppeared = false
                viewModelStoreOwner.onDisappear()
            }
            .onChange(of: scenePhase) { _, newPhase in
                guard isAppeared else { return }
                // performSave on the way to background — mirrors ComposeContainer capturing
                // saveState() on dispose.
                if newPhase == .background {
                    savedState = viewModelStoreOwner.saveState()
                }
                viewModelStoreOwner.onScenePhaseChanged(newPhase)
            }
    }
}
