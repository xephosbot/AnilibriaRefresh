import SwiftUI
import AnilibriaFramework

struct ContentView: View {
    @ObservedObject private(set) var viewModel: ViewModel
    
    var body: some View {
        ListView(titles: viewModel.titles)
            .task { await self.viewModel.startObserving() }
    }
}

extension ContentView {
    @MainActor
    class ViewModel: ObservableObject {
        @Published var titles: Array<String> = []
        let repository = KoinKt.getTitleRepository()

        func startObserving() async {
            do {
                let titles = try await repository.getRecommendedTitles()
                self.titles = titles.map { $0.name }
            } catch {
                print("Error loading titles: \(error.localizedDescription)")
            }
        }
    }
}

struct ListView: View {
    let titles: Array<String>

    var body: some View {
        List(titles, id: \.self) {
            Text($0)
        }
    }
}
