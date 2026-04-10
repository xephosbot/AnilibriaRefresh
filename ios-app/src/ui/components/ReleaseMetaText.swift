//
//  ReleaseMetaText.swift
//  AnilibriaRefresh
//
//  Created by xephosbot on 10/04/26.
//

import SwiftUI
import Shared

struct ReleaseMetaText: View {

    let release: Release

    var body: some View {
        metaText
            .font(.caption)
            .lineLimit(1)
    }

    private var metaText: Text {
        var parts: [Text] = []

        parts.append(Text(release.ageRating.displayName))
        parts.append(Text(String(release.year)))

        if let type = release.type {
            if type == .movie {
                parts.append(Text(type.displayName))
            } else if let count = release.episodesCount {
                parts.append(Text("episode_abbreviation \(count)"))
            }
        }

        if let duration = release.episodeDuration {
            parts.append(Text("minutes_abbreviation \(duration)"))
        }

        parts.append(Text("\(release.favoritesCount) ★"))

        return parts.reduce(Text("")) { result, part in
            if result == Text("") {
                return part
            }
            return result + Text(" • ") + part
        }
    }
}

#Preview {
    ReleaseMetaText(release: PreviewData.release)
        .padding()
}
