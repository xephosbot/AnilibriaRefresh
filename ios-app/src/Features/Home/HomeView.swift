import SwiftUI
import Shared

/// Stateful entry: resolves & owns the feature's ViewModel from the scoped store.
struct HomeScreen: View {
    @EnvironmentObject private var store: IosViewModelStoreOwner

    var body: some View {
        HomeContent(viewModel: store.viewModel())
    }
}

/// Observes the ViewModel's state (typed, non-null via `StateModel`) and renders by it.
private struct HomeContent: View {
    @State private var model: StateModel<HomeScreenState>

    init(viewModel: HomeViewModel) {
        _model = State(initialValue: StateModel(viewModel.container.stateFlow))
    }

    var body: some View {
        List {
            releasesSection
        }
        .listStyle(.plain)
        .navigationTitle("tab_home")
    }

    @ViewBuilder
    private var releasesSection: some View {
        let items: [Release?] = {
            switch onEnum(of: model.state.releasesFeed.recommendedReleases) {
            case .success(let success):
                return (success.data as! [Release]).map { Optional($0) }
            case .loading, .error:
                return Array(repeating: nil, count: 10)
            }
        }()

        ForEach(Array(items.enumerated()), id: \.offset) { _, release in
            if let release {
                NavigationLink(value: AppRoute.title(releaseId: Int(release.id))) {
                    ReleaseRow(release: release)
                }
            } else {
                ReleaseRow(release: nil)
            }
        }
    }
}
