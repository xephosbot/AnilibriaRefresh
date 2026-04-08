import SwiftUI
import Shared

struct ContentView: View {
    @EnvironmentObject var viewModelStoreOwner: IosViewModelStoreOwner

    var body: some View {
        let loginVM: LoginViewModel = viewModelStoreOwner.viewModel()
        LoginView(viewModel: loginVM)
    }
}
