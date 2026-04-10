//
//  PreviewData.swift
//  AnilibriaRefresh
//
//  Created by xephosbot on 10/04/26.
//

import Shared

enum PreviewData {
    static let release = Release(
        id: 1,
        type: .tv,
        year: 2024,
        name: "Frieren: Beyond Journey's End",
        englishName: "Sousou no Frieren",
        description: "The adventure is over but life goes on for an elf mage just beginning to learn what living is all about.",
        ageRating: .r12Plus,
        episodesCount: 28,
        episodeDuration: 24,
        favoritesCount: 1500,
        poster: Poster(
            src: "https://anilibria.top/storage/releases/posters/9893/ItO6iCEFhNYxSmB1sSighfDiObiNyS57.webp",
            thumbnail: "https://anilibria.top/storage/releases/posters/9893/ItO6iCEFhNYxSmB1sSighfDiObiNyS57.webp"
        )
    )
}
