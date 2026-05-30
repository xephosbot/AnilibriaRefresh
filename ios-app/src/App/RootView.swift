import SwiftUI

/// Top-level navigation, mirroring the Compose `NavigationSuiteScaffold`:
/// a tab bar over four sections, each with its own `NavigationStack` (per-tab back stack).
/// `AppRouter` holds the selected tab + per-tab paths and is shared via the environment.
struct RootView: View {
    @State private var router = AppRouter()

    var body: some View {
        TabView(selection: $router.selectedTab) {
            makeTab(.home, path: $router.homePath) { HomeScreen() }
            makeTab(.search, path: $router.searchPath) { SearchScreen() }
            makeTab(.favorite, path: $router.favoritePath) { FavoriteView() }
            makeTab(.preference, path: $router.preferencePath) { PreferenceView() }
        }
        .environment(router)
    }

    private func makeTab<Root: View>(
        _ tab: AppTab,
        path: Binding<[AppRoute]>,
        @ViewBuilder root: () -> Root
    ) -> some View {
        NavigationStack(path: path) {
            root()
                .navigationDestination(for: AppRoute.self) { destination(for: $0) }
        }
        .tabItem { Label(tab.titleKey, systemImage: tab.systemImage) }
        .tag(tab)
    }

    @ViewBuilder
    private func destination(for route: AppRoute) -> some View {
        switch route {
        case .title(let releaseId):
            TitleScreen(releaseId: releaseId)
        case .player(let releaseId, let episodeOrdinal):
            PlayerScreen(releaseId: releaseId, episodeOrdinal: episodeOrdinal)
        case .schedule:
            ScheduleView()
        case .searchFilters:
            SearchFiltersView()
        }
    }
}
