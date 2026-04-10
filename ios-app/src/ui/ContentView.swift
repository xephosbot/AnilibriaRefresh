import SwiftUI
import Shared

struct ContentView: View {
    @EnvironmentObject var viewModelStoreOwner: IosViewModelStoreOwner

    var body: some View {
        let homeVM: HomeViewModel = viewModelStoreOwner.viewModel()
        HomeView(viewModel: homeVM)
    }
}
