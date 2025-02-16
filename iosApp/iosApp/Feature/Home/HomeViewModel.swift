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
        
        @LazyKoin private var pager: GetReleasesPager
        private var delegate: PagingViewController<Release>
        
        @Published private(set) var itemsCount: Int = 0
        
        init() {
            delegate = PagingViewController()
        }
        
        func fetchAt(index: Int) {
            print(index)
            delegate.fetchAt(index: Int32(index))
        }
        
        func getItem(index: Int) -> Release? {
            return delegate.getItem(index: Int32(index))
        }
        
        func collectPagingData() {
            Task {
                try? await asyncSequence(for: pager.invoke(search: nil)).collect { pagingData in
                    try? await delegate.submitData(pagingData: pagingData)
                }
            }
            Task {
                try? await asyncSequence(for: delegate.loadState).collect { loadState in
                    
                }
            }
            Task {
                try? await asyncSequence(for: delegate.itemsCount).collect { itemsCount in
                    self.itemsCount = itemsCount.intValue
                }
            }
        }
    }
}
