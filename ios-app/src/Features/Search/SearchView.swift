//
//  SearchView.swift
//  AnilibriaRefresh
//
//  Каркас фичи Search (≈ shared-ui/feature/search).
//
//  `SearchViewModel` из `Shared` НЕ требует @Provided-параметров (как `HomeViewModel`),
//  поэтому подключается напрямую через store — по образцу `RootView` / `HomeView`:
//
//      import Shared
//      struct SearchView: View {
//          @EnvironmentObject var store: IosViewModelStoreOwner
//          var body: some View {
//              let viewModel: SearchViewModel = store.viewModel()
//              SearchContent(viewModel: viewModel)   // collect viewModel.container.stateFlow
//          }
//      }
//

import SwiftUI

struct SearchView: View {
    var body: some View {
        FeaturePlaceholder(title: "fab_search", systemImage: "magnifyingglass")
    }
}

#Preview {
    SearchView()
}
