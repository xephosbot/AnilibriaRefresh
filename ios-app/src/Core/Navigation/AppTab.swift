//
//  AppTab.swift
//  AnilibriaRefresh
//
//  Top-level destinations (tab bar), mirroring Compose `TopLevelRoutes`
//  (shared-ui/navigation/impl/TopLevelRoutes.kt): Home, Search, Favorite, Preference.
//

import SwiftUI

enum AppTab: Int, CaseIterable, Identifiable {
    case home
    case search
    case favorite
    case preference

    var id: Int { rawValue }

    var titleKey: LocalizedStringKey {
        switch self {
        case .home: "tab_home"
        case .search: "fab_search"
        case .favorite: "tab_favorite"
        case .preference: "tab_preference"
        }
    }

    var systemImage: String {
        switch self {
        case .home: "house"
        case .search: "magnifyingglass"
        case .favorite: "heart"
        case .preference: "gearshape"
        }
    }

    /// Mirrors `FavoriteRoute.requiresLogin = true`. Used by `AppRouter`'s gate hook.
    var requiresLogin: Bool {
        self == .favorite
    }
}
