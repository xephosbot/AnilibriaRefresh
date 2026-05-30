//
//  RelativeURLDataLoader.swift
//  AnilibriaRefresh
//
//  Created by xephosbot on 30/05/26.
//

import Nuke
import Foundation
import Shared

final class RelativeURLDataLoader: DataLoading, @unchecked Sendable {
    private let inner = DataLoader()
    private let urlProvider: ApiImageUrlProvider

    init(urlProvider: ApiImageUrlProvider) {
        self.urlProvider = urlProvider
    }

    func loadData(
        with request: URLRequest,
        didReceiveData: @Sendable @escaping (Data, URLResponse) -> Void,
        completion: @Sendable @escaping (Error?) -> Void
    ) -> any Cancellable {
        var modified = request
        if let raw = request.url?.absoluteString,
           let full = urlProvider.getFullUrl(path: raw),
           let resolved = URL(string: full) {
            modified.url = resolved
        }
        return inner.loadData(
            with: modified,
            didReceiveData: didReceiveData,
            completion: completion
        )
    }
}
