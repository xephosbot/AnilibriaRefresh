//
//  Title.swift
//  iosApp
//
//  Created by xephosbot on 19/12/24.
//  Copyright © 2024 xbot. All rights reserved.
//

import AnilibriaFramework

struct Title : Identifiable, Hashable {
    let id: Int32
    let name: String
}

extension Title {
    init(kmpModel: TitleModel) {
        self.id = kmpModel.id
        self.name = kmpModel.name
    }
}
