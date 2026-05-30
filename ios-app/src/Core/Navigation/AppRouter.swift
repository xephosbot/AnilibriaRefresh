//
//  AppRouter.swift
//  AnilibriaRefresh
//
//  SwiftUI analog of the Compose `Navigator` + `NavigationState`: a per-tab back stack model.
//  Each tab owns its own `[AppRoute]` path; `navigate` pushes onto the currently selected tab.
//  Injected into the environment by `RootView` and read by screens with
//  `@Environment(AppRouter.self)`.
//

import SwiftUI

@MainActor
@Observable
final class AppRouter {
    var selectedTab: AppTab = .home

    var homePath: [AppRoute] = []
    var searchPath: [AppRoute] = []
    var favoritePath: [AppRoute] = []
    var preferencePath: [AppRoute] = []

    /// Push a route onto the current tab's stack. Mirrors `Navigator.navigate(key)`.
    ///
    /// The `requiresLogin` branch is a scaffolded hook — auth-state observation is deferred,
    /// so for now it just pushes. Later: redirect to a Login route when unauthenticated.
    func navigate(_ route: AppRoute) {
        // if route.requiresLogin && !isAuthenticated { push(.login(returnTo: route)); return }
        push(route, onto: selectedTab)
    }

    /// Clear a tab's stack (e.g. re-tapping the active tab).
    func popToRoot(_ tab: AppTab) {
        write(to: tab) { $0.removeAll() }
    }

    private func push(_ route: AppRoute, onto tab: AppTab) {
        write(to: tab) { $0.append(route) }
    }

    private func write(to tab: AppTab, _ mutate: (inout [AppRoute]) -> Void) {
        switch tab {
        case .home: mutate(&homePath)
        case .search: mutate(&searchPath)
        case .favorite: mutate(&favoritePath)
        case .preference: mutate(&preferencePath)
        }
    }
}
