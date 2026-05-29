//
//  RelativeURLDataLoader.swift
//  AnilibriaRefresh
//
//  Created by xephosbot on 30/05/26.
//

import Nuke
import Foundation

protocol ImageUrlProvider : Sendable {
    func resolve(_ path: String?) -> URL?
}

// MARK: - Impl

final class ImageUrlProviderImpl: ImageUrlProvider {
    func resolve(_ path: String?) -> URL? {
        guard let path, !path.isEmpty else { return nil }
        if path.hasPrefix("http") { return URL(string: path) }
        let normalized = path.hasPrefix("/") ? path : "/\(path)"
        return URL(string: "https://aniliberty.top" + normalized)
    }
}

final class RelativeURLDataLoader: DataLoading {
    private let inner = DataLoader()
    private let urlProvider: ImageUrlProvider

    init(urlProvider: ImageUrlProvider) {
        self.urlProvider = urlProvider
    }

    func loadData(
        with request: URLRequest,
        didReceiveData: @Sendable @escaping (Data, URLResponse) -> Void,
        completion: @Sendable @escaping (Error?) -> Void
    ) -> any Cancellable {
        var modified = request
        if let raw = request.url?.absoluteString,
           let resolved = urlProvider.resolve(raw) {
            modified.url = resolved
        }
        return inner.loadData(
            with: modified,
            didReceiveData: didReceiveData,
            completion: completion
        )
    }
}
