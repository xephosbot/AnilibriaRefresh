//
//  AgeRating.swift
//  AnilibriaRefresh
//
//  Created by xephosbot on 10/04/26.
//

import SwiftUI
import Shared

extension AgeRating {
    var displayName: LocalizedStringKey {
        switch self {
        case .r0Plus:  "age_rating_0_plus"
        case .r6Plus:  "age_rating_6_plus"
        case .r12Plus: "age_rating_12_plus"
        case .r16Plus: "age_rating_16_plus"
        case .r18Plus: "age_rating_18_plus"
        }
    }
}
