//
//  Route.swift
//  AnilibriaRefresh
//
//  SwiftUI-аналоги навигационных контрактов из `shared-ui/navigation/api`
//  (`NavKey` / `TopLevelNavKey`). Каждая фича объявляет свой Route-тип рядом со своими
//  view (см. `Features/Home/HomeRoute.swift`) — это «api» фичи: то, что о ней знают
//  остальные части приложения, не завися от её реализации.
//

import SwiftUI

/// Типобезопасный маршрут навигации. SwiftUI-аналог `NavKey`.
/// Значения такого типа кладутся в `NavigationStack` и разрешаются `navigationDestination(for:)`.
protocol Route: Hashable {}

/// Пункт корневой навигации (таб/раздел). Аналог `TopLevelNavKey`:
/// несёт заголовок и SF Symbol для таб-бара.
protocol TopLevelRoute: Route {
    var titleKey: LocalizedStringKey { get }
    var systemImage: String { get }
}
