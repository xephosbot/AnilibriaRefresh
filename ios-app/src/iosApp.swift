import SwiftUI
import Shared

@main
struct iosApp: App {
    init() {
        KoinKt.initKoin()
    }

    var body: some Scene {
        WindowGroup {
            ViewModelStoreOwnerProvider {
                ContentView()
            }
        }
    }
}
