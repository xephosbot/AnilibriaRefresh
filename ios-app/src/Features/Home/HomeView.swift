import SwiftUI
import Shared

struct HomeView: View {
    let viewModel: HomeViewModel

    @State private var state: HomeScreenState?

    var body: some View {
        HomeViewContent(state: state)
            .task {
                for await anyState in viewModel.container.stateFlow {
                    state = anyState as? HomeScreenState
                }
            }
    }
}

private struct HomeViewContent: View {
    let state: HomeScreenState?

    var body: some View {
        NavigationStack {
            List {
                releasesSection
            }
            .listStyle(.plain)
            .navigationTitle(String(localized: "tab_home"))
            .navigationDestination(for: Release.self) { release in
                Text(release.name) // заглушка, потом заменишь на DetailView
            }
        }
    }

    @ViewBuilder
    private var releasesSection: some View {
        let items: [Release?] = {
            switch onEnum(of: state?.releasesFeed.recommendedReleases) {
            case .success(let success):
                return (success.data as! [Release]).map { Optional($0) }
            case .loading, .error, .none:
                return Array(repeating: nil, count: 10)
            }
        }()

        ForEach(Array(items.enumerated()), id: \.offset) { _, release in
            if let release {
                NavigationLink(value: release) {
                    ReleaseRow(release: release)
                }
            } else {
                ReleaseRow(release: nil)
            }
        }
    }
}

#Preview("Loading") {
    HomeViewContent(state: nil)
}
