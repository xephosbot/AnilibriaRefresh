//
//  ReleaseRow.swift
//  AnilibriaRefresh
//
//  Created by xephosbot on 09/04/26.
//

import SwiftUI
import Shimmer
import Shared

struct ReleaseRow: View {

    let release: Release?

    var body: some View {
        Group {
            if let release {
                loadedRow(release)
                    .transition(.opacity)
            } else {
                placeholderRow
                    .transition(.opacity)
            }
        }
        .animation(.easeInOut(duration: 0.3), value: release != nil)
    }

    private func loadedRow(_ release: Release) -> some View {
        HStack(spacing: 12) {
            PosterImage(poster: release.poster)
                .frame(width: 80, height: 120)
                .clipShape(RoundedRectangle(cornerRadius: 8))

            VStack(alignment: .leading, spacing: 4) {
                Text(release.name)
                    .font(.headline)
                    .lineLimit(2)

                ReleaseMetaText(release: release)
                    .foregroundStyle(.secondary)

                if let description = release.description_ {
                    Text(description.replacingOccurrences(of: "\n", with: " "))
                        .font(.subheadline)
                        .foregroundStyle(.tertiary)
                        .lineLimit(3)
                }

                Spacer(minLength: 0)
            }
            .padding(.vertical, 8)
        }
    }

    private var placeholderRow: some View {
        HStack(spacing: 12) {
            RoundedRectangle(cornerRadius: 8)
                .fill(.quaternary)
                .frame(width: 80, height: 120)

            VStack(alignment: .leading, spacing: 6) {
                RoundedRectangle(cornerRadius: 4)
                    .fill(.quaternary)
                    .frame(height: 18)
                    .frame(maxWidth: .infinity)

                RoundedRectangle(cornerRadius: 4)
                    .fill(.quaternary)
                    .frame(width: 160, height: 14)

                RoundedRectangle(cornerRadius: 4)
                    .fill(.quaternary)
                    .frame(height: 14)
                    .frame(maxWidth: .infinity)

                Spacer(minLength: 0)
            }
            .padding(.vertical, 8)
        }
        .shimmering()
    }
}


#Preview("Loaded") {
    List {
        ReleaseRow(release: PreviewData.release)
        ReleaseRow(release: PreviewData.release)
    }
    .listStyle(.plain)
}

#Preview("Loading") {
    List {
        ReleaseRow(release: nil)
        ReleaseRow(release: nil)
        ReleaseRow(release: nil)
    }
    .listStyle(.plain)
}
