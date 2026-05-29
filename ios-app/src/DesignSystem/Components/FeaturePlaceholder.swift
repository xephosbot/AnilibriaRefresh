//
//  FeaturePlaceholder.swift
//  AnilibriaRefresh
//
//  Единая заглушка для ещё не реализованных экранов-фич. Используется в `Features/*`
//  как каркас до появления реального UI.
//

import SwiftUI

struct FeaturePlaceholder: View {
    let title: LocalizedStringKey
    let systemImage: String

    var body: some View {
        ContentUnavailableView(title, systemImage: systemImage)
    }
}

#Preview {
    FeaturePlaceholder(title: "tab_home", systemImage: "house")
}
