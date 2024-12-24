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
    @ObservedObject private var viewModel = ViewModel()
    
    var body: some View {
        List {
            ForEach(viewModel.newsResult, id: \.self.name) { news in
                TitleItem(title: news)
            }
            
            if viewModel.showLoding {
                // loading view
                VStack {
                    ProgressView(label: { Text("Loading...") })
                }
            }
            
            
            if (!viewModel.newsResult.isEmpty) {
                VStack(alignment: .center) {
                    if(viewModel.hasNextPage) {
                        ProgressView().onAppear {
                            viewModel.loadNextPage()
                        }
                    } else {
                        Text("-- Not more --").foregroundColor(.gray)
                    }
                }.frame(maxWidth: .infinity).listRowInsets(EdgeInsets())
                    .listRowBackground(Color.clear)
            }
            
        }
            .onFirstAppear {
                viewModel.startLoadNews()
            }.task {
                await viewModel.subscribeDataChanged()
            }.task {
                await viewModel.subscribeLoadState()
            }
    }
}
