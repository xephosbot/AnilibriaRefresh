//
//  PlayerView.swift
//  AnilibriaRefresh
//
//  Player — playback (≈ shared-ui/feature/player). Pushed via `AppRoute.player(...)`.
//
//  TODO: resolve `PlayerViewModel` (needs @Provided `releaseId` + `initialEpisodeOrdinal`):
//      let vm: PlayerViewModel = store.viewModel(
//          parameters: { ParametersHolder(_values: [String(releaseId), KotlinInt(int: Int32(episodeOrdinal))]) }
//      )
//  then render via `StateModel<PlayerScreenState>`.
//

import SwiftUI

struct PlayerScreen: View {
    let releaseId: Int
    let episodeOrdinal: Int

    var body: some View {
        FeaturePlaceholder(title: "Player", systemImage: "play.circle")
            .navigationTitle(Text(verbatim: "Player · release \(releaseId), ep \(episodeOrdinal)"))
            .navigationBarTitleDisplayMode(.inline)
    }
}
