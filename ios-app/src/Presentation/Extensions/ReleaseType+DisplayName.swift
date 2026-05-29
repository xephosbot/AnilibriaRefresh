//
//  ReleaseType.swift
//  AnilibriaRefresh
//
//  Created by xephosbot on 10/04/26.
//

import SwiftUI
import Shared

extension ReleaseType {
    var displayName: LocalizedStringKey {
        switch self {
        case .tv:      "release_type_tv"
        case .ona:     "release_type_ona"
        case .web:     "release_type_web"
        case .ova:     "release_type_ova"
        case .oad:     "release_type_oad"
        case .movie:   "release_type_movie"
        case .dorama:  "release_type_dorama"
        case .special: "release_type_special"
        }
    }
}
