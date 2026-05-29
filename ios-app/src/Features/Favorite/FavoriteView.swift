//
//  FavoriteView.swift
//  AnilibriaRefresh
//
//  Каркас фичи Favorite — избранное (≈ shared-ui/feature/favorite).
//
//  ВНИМАНИЕ: на KMP у favorite пока НЕТ state-слоя — в `shared-ui` есть только Compose
//  `FavoriteScreen` + навигационный роут, а в umbrella-фреймворк `Shared` ViewModel не
//  экспортируется. Когда появится `FavoriteViewModel`, подключать как `HomeViewModel`
//  (через `store.viewModel()`), если он будет без @Provided-параметров.
//

import SwiftUI

struct FavoriteView: View {
    var body: some View {
        FeaturePlaceholder(title: "tab_favorite", systemImage: "heart")
    }
}

#Preview {
    FavoriteView()
}
