//
//  PagingViewModel.swift
//  iosApp
//
//  Created by xephosbot on 19/12/24.
//  Copyright © 2024 xbot. All rights reserved.
//

import Foundation
import AnilibriaFramework

@MainActor
final class PagingSource<Item: Identifiable & Hashable>: ObservableObject {
    @Published private(set) var items: [Item] = []
    @Published private(set) var state: LoadingState = .idle

    var totalItems: Int = 0
    private var currentPage: Int = 0
    private let limit: Int
    private let fetchPage: (Int32, Int32) async throws -> ([Item], Int32)

    init(limit: Int = 20, fetchPage: @escaping (Int32, Int32) async throws -> ([Item], Int32)) {
        self.limit = limit
        self.fetchPage = fetchPage
    }

    func fetchNextPage() async {
        guard state == .idle || state == .data else { return }
        guard items.count < totalItems || totalItems == 0 else { return }
        state = .loadingNextPage

        do {
            let response = try await fetchPage(Int32(currentPage + 1), Int32(limit))
            if currentPage == 0 {
                self.items = response.0
            } else {
                self.items.append(contentsOf: response.0)
            }
            self.totalItems = Int(response.1)
            self.currentPage += 1
            self.state = .data
        } catch {
            self.state = .error(error.localizedDescription)
        }
    }
}

enum LoadingState: Equatable {
    case idle
    case loading
    case loadingNextPage
    case data
    case nextPageData
    case empty(String)
    case error(String)
}
