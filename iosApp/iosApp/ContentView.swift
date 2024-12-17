import SwiftUI
import AnilibriaFramework

struct ContentView: View {
    @ObservedObject private(set) var viewModel: ViewModel
    
    var body: some View {
        ListView(phrases: viewModel.titles)
            .task { await self.viewModel.startObserving() }
    }
}

extension ContentView {
    @MainActor
    class ViewModel: ObservableObject {
        @Published var titles: Array<String> = []
        let repository = KoinKt.getTitleRepository()

        func startObserving() async {
            let titles = try? await repository.getRecommendedTitles()
            for title in titles ?? [] {
                self.titles.append(title.name)
            }
        }
    }
}

struct ListView: View {
    let phrases: Array<String>

    var body: some View {
        List(phrases, id: \.self) {
            Text($0)
        }
    }
}

