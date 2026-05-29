//
//  PreferenceView.swift
//  AnilibriaRefresh
//
//  Каркас фичи Preference — настройки (≈ shared-ui/feature/preference).
//
//  Preference — это кластер под-экранов (Appearance / History / Donate / Team / Language).
//  Корневой экран — список разделов настроек. ViewModel-и под-экранов (`AppearanceViewModel`
//  и пр.) НЕ требуют @Provided-параметров и резолвятся напрямую — по образцу `HomeViewModel`:
//
//      import Shared
//      struct AppearanceView: View {
//          @EnvironmentObject var store: IosViewModelStoreOwner
//          var body: some View {
//              let viewModel: AppearanceViewModel = store.viewModel()
//              AppearanceContent(viewModel: viewModel)
//          }
//      }
//

import SwiftUI

struct PreferenceView: View {
    var body: some View {
        FeaturePlaceholder(title: "tab_preference", systemImage: "gearshape")
    }
}

#Preview {
    PreferenceView()
}
