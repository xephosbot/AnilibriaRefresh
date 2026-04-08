import SwiftUI
import Shared

@main
struct iosApp: App {
    init() {
        StartKoinKt.startKoin()
    }

    var body: some Scene {
        WindowGroup {
            ViewModelStoreOwnerProvider {
                ContentView()
            }
        }
    }
}
