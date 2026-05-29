//
//  TitleView.swift
//  AnilibriaRefresh
//
//  Каркас фичи Title — детали релиза (≈ shared-ui/feature/title).
//
//  `TitleViewModel` требует рантайм-аргументы (@Provided): `aliasOrId: String` и
//  опциональный `initialRelease: Release?`. Экран создаётся при навигации с релиза
//  (см. `HomeDestination.title(Release)`), а не как самостоятельный таб. Проводка через
//  `parameters:` (первый элемент — aliasOrId, второй — initialRelease):
//
//      import Shared
//      struct TitleView: View {
//          @EnvironmentObject var store: IosViewModelStoreOwner
//          let release: Release
//          var body: some View {
//              let viewModel: TitleViewModel = store.viewModel(
//                  parameters: { ParametersHolder(_values: [String(release.id), release]) }
//              )
//              TitleContent(viewModel: viewModel)
//          }
//      }
//

import SwiftUI

struct TitleView: View {
    var body: some View {
        FeaturePlaceholder(title: "Title", systemImage: "film")
    }
}

#Preview {
    TitleView()
}
