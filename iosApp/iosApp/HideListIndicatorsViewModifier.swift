//
//  HideListIndicatorsViewModifier.swift
//  iosApp
//
//  Created by xephosbot on 19/12/24.
//  Copyright © 2024 xbot. All rights reserved.
//

import SwiftUI

struct HideListIndicatorsViewModifier: ViewModifier {
    func body(content: Content) -> some View {
        if #available(iOS 16.0, *) {
            content
                .scrollIndicators(.hidden)
        } else {
            content
        }
    }
}

extension View {
    func hideListIndicators() -> some View {
        modifier(HideListIndicatorsViewModifier())
    }
}
