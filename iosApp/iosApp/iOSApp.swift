import SwiftUI
import AnilibriaFramework

@main
struct iOSApp: App {
    
    init() {
        KoinAppKt.doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView(viewModel: ContentView.ViewModel())
        }
    }
}
