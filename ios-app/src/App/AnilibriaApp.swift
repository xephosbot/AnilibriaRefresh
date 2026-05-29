import SwiftUI
import Nuke
import Shared

@main
struct AnilibriaApp: App {
    init() {
        KoinKt.doInitKoin()
        configureImagePipeline()
     }

    var body: some Scene {
        WindowGroup {
            ViewModelStoreOwnerProvider {
                RootView()
            }
        }
    }
    
    private func configureImagePipeline() {
        var config = ImagePipeline.Configuration.withDataCache(
            name: "com.anilibria.imagecache"
        )
        config.dataLoader = RelativeURLDataLoader(
            urlProvider: ImageUrlProviderImpl()
        )
        ImagePipeline.shared = ImagePipeline(configuration: config)
    }
}
