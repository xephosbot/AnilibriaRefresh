//
//  AppRoute.swift
//  AnilibriaRefresh
//
//  Pushable navigation destinations (the "api" contracts of features), mirroring the Kotlin
//  `NavKey` route classes. Pushed onto a tab's `NavigationStack` path by `AppRouter`.
//

import Foundation

enum AppRoute: Hashable {
    case schedule
    case title(releaseId: Int)
    case player(releaseId: Int, episodeOrdinal: Int)
    case searchFilters

    /// Mirrors `NavKey.requiresLogin`. Routed through `AppRouter`'s gate hook.
    var requiresLogin: Bool {
        switch self {
        default: false
        }
    }
}
