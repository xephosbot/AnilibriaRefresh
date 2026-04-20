import SwiftUI
import Shared

@main
struct iosApp: App {
    init() {
        doInitKotzillaConfig()
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
