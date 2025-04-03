//
//  HomeScreen.swift
//  iosApp
//
//  Created by xephosbot on 20/12/24.
//  Copyright © 2024 xbot. All rights reserved.
//

import SwiftUI
import AnilibriaFramework

struct HomeScreen: View {
    @StateObject private var viewModel = ViewModel()
    
    var body: some View {
        List {
            ForEach(0..<viewModel.itemsCount, id:\.self) { index in
                TitleItem(title: viewModel.getItem(index: index))
                    .onFirstAppear {
                        viewModel.fetchAt(index: index)
                    }
            }
        }.onFirstAppear {
            viewModel.collectPagingData()
        }
    }
}
