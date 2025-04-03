//
//  CharactersListRowView.swift
//  iosApp
//
//  Created by xephosbot on 21/12/24.
//  Copyright © 2024 xbot. All rights reserved.
//

import SwiftUI
import AnilibriaFramework

struct CharactersListRowView: View {
    let character: TitleModel
    
    var body: some View {
        Text(character.name)
    }
}
