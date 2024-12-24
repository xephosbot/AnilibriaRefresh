//
//  HomeViewModel.swift
//  iosApp
//
//  Created by xephosbot on 20/12/24.
//  Copyright © 2024 xbot. All rights reserved.
//

import Foundation
import AnilibriaFramework
import KMPNativeCoroutinesAsync

extension HomeScreen {
    @MainActor final class ViewModel: ObservableObject {

        @LazyKoin private var titlePager: TitlePager
        private var delegate = PagingCollectionViewController()

        @Published private(set) var newsResult:[TitleModel] = []
        @Published private(set) var hasNextPage: Bool = false
        @Published private(set) var showLoding: Bool = false

        func startLoadNews() {
            Task {
                try? await asyncSequence(for: titlePager.invoke()).collect { pagingData in
                    delegate.submitData(pagingData: pagingData)
                }
            }
        }

        func subscribeDataChanged() async {
            do {
                for try await _ in asyncSequence(for: delegate.onPagesUpdatedFlow) {
                    self.newsResult = delegate.getItems()
                }
            } catch {}
        }

        func loadNextPage() {
            delegate.loadNextPage()
        }

        func subscribeLoadState() async {
            do {
                for try await loadState in asyncSequence(for: delegate.loadStateFlow) {
                    switch onEnum(of: loadState.append) {
                        case .error(let errorState):
                            print(errorState.error.message?.description ?? "append error...")
                            break
                        case .loading(_):
                            break
                        case .notLoading(let notLoading):
                            self.hasNextPage = !notLoading.endOfPaginationReached
                            break
                    }

                    switch onEnum(of: loadState.refresh) {
                        case .error(_):
                            break
                        case .loading(_):
                            self.showLoding = true
                            break
                        case .notLoading(_):
                            self.showLoding = false
                            break
                    }
                }
            } catch {

            }
        }
    }
}
