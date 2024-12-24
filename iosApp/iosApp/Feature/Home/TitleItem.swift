//
//  TitleItem.swift
//  iosApp
//
//  Created by xephosbot on 21/12/24.
//  Copyright © 2024 xbot. All rights reserved.
//

import SwiftUI
import AnilibriaFramework

struct TitleItem: View {
    let title: TitleModel
    
    init(title: TitleModel) {
        self.title = title
    }
    
    var body: some View {
        VStack(spacing: 8) {
            Text(title.name)
                .lineLimit(2)
                .font(.title)
                .frame(maxWidth: .infinity, alignment: .leading)
            Text(title.description_)
                .lineLimit(3)
                .font(.body)
                .frame(maxWidth: .infinity, alignment: .leading)
        }
    }
}
