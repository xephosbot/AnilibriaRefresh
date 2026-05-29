//
//  PosterImage.swift
//  AnilibriaRefresh
//
//  Created by xephosbot on 09/04/26.
//

import SwiftUI
import Shimmer
import Shared
import NukeUI

struct PosterImage: View {
    
    let poster: Poster?
    var contentScale: ContentMode = .fill
    
    private var imageURL: URL? {
        poster.flatMap { URL(string: $0.src) }
    }
    
    var body: some View {
        LazyImage(url: imageURL) { state in
            if let image = state.image {
                image
                    .resizable()
                    .aspectRatio(contentMode: contentScale)
            } else if state.error != nil {
                Color.red
            } else {
                Color.gray.shimmering()
            }
        }
        .animation(.easeInOut, value: imageURL)
    }
}

#Preview {
    let testPoster = ReleaseFixtures.shared.oshiNoKo.poster
    
    PosterImage(poster: testPoster)
        .frame(width: 200, height: 300)
}
