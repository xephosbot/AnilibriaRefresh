import SwiftUI

/// The iOS app is a thin shim: every screen, all state and DI live in the Compose Multiplatform
/// `SharedUI` framework. Swift only owns the app lifecycle, the window it hands to Compose, and the
/// launch animation, which has to be native because the launch storyboard cannot animate itself.
@main
struct AnilibertyApp: App {
    @State private var showsSplash = true
    @State private var isContentReady = false

    var body: some Scene {
        WindowGroup {
            ZStack {
                ComposeView { isContentReady = true }
                    .ignoresSafeArea(.all)

                if showsSplash {
                    SplashView(isContentReady: isContentReady) { showsSplash = false }
                }
            }
        }
    }
}
