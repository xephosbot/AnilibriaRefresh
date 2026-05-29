//
//  HomeRoute.swift
//  AnilibriaRefresh
//
//  «api»-контракт фичи Home — SwiftUI-аналог `HomeRoute.kt` из `shared-ui/feature/home/api`.
//

import SwiftUI
import Shared

/// Корневой раздел Home (таб). Аналог `HomeRoute : TopLevelNavKey`.
enum HomeRoute: TopLevelRoute {
    case root

    var titleKey: LocalizedStringKey { "tab_home" }
    var systemImage: String { "house" }
}

/// Назначения навигации, доступные из Home. Аналог `ScheduleRoute` и перехода на детали
/// релиза (на KMP за детали отвечает feature `title`).
enum HomeDestination: Route {
    case schedule
    case title(Release)
}
