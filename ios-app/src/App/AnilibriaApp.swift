import SwiftUI

/// The iOS app is a thin shim: every screen, all state and DI live in the Compose Multiplatform
/// `SharedUI` framework. Swift only owns the app lifecycle and hands the window over to Compose.
@main
struct AnilibriaApp: App {
    var body: some Scene {
        WindowGroup {
            ComposeView()
                .ignoresSafeArea(.all)
        }
    }
}
