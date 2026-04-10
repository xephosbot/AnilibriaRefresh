//
//  PosterImage.swift
//  AnilibriaRefresh
//
//  Created by xephosbot on 09/04/26.
//

import SwiftUI
import Shimmer
import Shared

struct PosterImage: View {
    
    let poster: Poster?
    var contentScale: ContentMode = .fill
    
    var body: some View {
        Group {
            if let poster {
                AsyncImage(
                    url: URL(string: poster.src),
                    transaction: Transaction(animation: .easeInOut)
                ) { phase in
                    switch phase {
                        case .empty:
                            Color.gray.shimmering()
                        case .success(let image):
                            image
                                .resizable()
                                .aspectRatio(contentMode: contentScale)
                        case .failure( _):
                            Color.red
                        @unknown default:
                            Color.gray.shimmering()
                    }
                }
            } else {
                Color.gray.shimmering()
            }
        }
        .animation(.easeInOut, value: poster != nil)
    }
}

#Preview {
    let testPoster = Poster(
        src: "https://anilibria.top/storage/releases/posters/9893/ItO6iCEFhNYxSmB1sSighfDiObiNyS57.webp",
        thumbnail: nil
    )
    
    PosterImage(poster: testPoster)
        .frame(width: 200, height: 300)
}
