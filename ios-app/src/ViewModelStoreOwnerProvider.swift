//
// Created by xephosbot on 08/04/26.
//

import SwiftUI
import Shared

struct ViewModelStoreOwnerProvider<Content: View>: View {
    @StateObject private var viewModelStoreOwner = IosViewModelStoreOwner()
    @Environment(\.scenePhase) private var scenePhase
    @State private var isAppeared = false

    private let content: Content

    /// Initializes the provider with its content, creating a new `IOSViewModelStoreOwner`.
    init(@ViewBuilder content: () -> Content) {
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
                viewModelStoreOwner.onScenePhaseChanged(newPhase)
            }
    }
}
