import SwiftUI
import AnilibriaFramework

@main
struct iOSApp: App {
    
    init() {
        KoinApplication.start()
    }
    
    var body: some Scene {
        WindowGroup {
            HomeScreen()
        }
    }
}
