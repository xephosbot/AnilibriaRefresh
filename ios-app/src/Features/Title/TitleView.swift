//
//  TitleView.swift
//  AnilibriaRefresh
//
//  Title — release details (≈ shared-ui/feature/title). Pushed via `AppRoute.title(releaseId:)`.
//
//  TODO: resolve `TitleViewModel` (needs @Provided `aliasOrId` + optional `initialRelease`):
//      let vm: TitleViewModel = store.viewModel(
//          parameters: { ParametersHolder(_values: [String(releaseId), nil]) }
//      )
//  then render via `StateModel<TitleScreenState>` like `HomeContent`.
//

import SwiftUI

struct TitleScreen: View {
    let releaseId: Int

    var body: some View {
        FeaturePlaceholder(title: "Title", systemImage: "film")
            .navigationTitle(Text(verbatim: "Title #\(releaseId)"))
            .navigationBarTitleDisplayMode(.inline)
    }
}
