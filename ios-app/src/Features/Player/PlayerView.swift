//
//  PlayerView.swift
//  AnilibriaRefresh
//
//  Каркас фичи Player — воспроизведение (≈ shared-ui/feature/player).
//
//  `PlayerViewModel` требует рантайм-аргументы (@Provided): `releaseId: String` и
//  `initialEpisodeOrdinal: Int`. Экран создаётся при навигации с эпизода, не как таб.
//  Проводка через `parameters:` (первый элемент — releaseId, второй — episodeOrdinal):
//
//      import Shared
//      struct PlayerView: View {
//          @EnvironmentObject var store: IosViewModelStoreOwner
//          let releaseId: String
//          let episodeOrdinal: Int
//          var body: some View {
//              let viewModel: PlayerViewModel = store.viewModel(
//                  parameters: { ParametersHolder(_values: [releaseId, episodeOrdinal]) }
//              )
//              PlayerContent(viewModel: viewModel)
//          }
//      }
//

import SwiftUI

struct PlayerView: View {
    var body: some View {
        FeaturePlaceholder(title: "Player", systemImage: "play.circle")
    }
}

#Preview {
    PlayerView()
}
