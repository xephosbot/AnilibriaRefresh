import SwiftUI
import Shared

/// Stateful entry: resolves & owns `SearchViewModel` (no `@Provided` args → resolved like Home).
struct SearchScreen: View {
    @EnvironmentObject private var store: IosViewModelStoreOwner

    var body: some View {
        SearchContent(viewModel: store.viewModel())
    }
}

/// Minimal content driven by typed, non-null `SearchScreenState` (full UI is a follow-up).
private struct SearchContent: View {
    let viewModel: SearchViewModel
    @State private var model: StateModel<SearchScreenState>

    init(viewModel: SearchViewModel) {
        self.viewModel = viewModel
        _model = State(initialValue: StateModel(viewModel.container.stateFlow))
    }

    var body: some View {
        List {
            Section("search_bar_placeholder") {
                Text(verbatim: model.state.query.isEmpty ? "—" : model.state.query)
                    .foregroundStyle(.secondary)
            }
            NavigationLink("button_filters", value: AppRoute.searchFilters)
        }
        .navigationTitle("fab_search")
    }
}
