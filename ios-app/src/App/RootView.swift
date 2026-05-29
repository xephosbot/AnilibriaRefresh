import SwiftUI
import Shared

struct RootView: View {
    @EnvironmentObject var viewModelStoreOwner: IosViewModelStoreOwner

    var body: some View {
        let homeVM: HomeViewModel = viewModelStoreOwner.viewModel()
        HomeView(viewModel: homeVM)
    }
}
