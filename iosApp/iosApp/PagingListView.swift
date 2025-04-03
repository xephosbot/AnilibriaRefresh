//
//  PagingListView.swift
//  iosApp
//
//  Created by xephosbot on 19/12/24.
//  Copyright © 2024 xbot. All rights reserved.
//

import SwiftUI

struct PagingListView<Item: Identifiable & Hashable, Content : View>: View {
    @ObservedObject var pagingsource: PagingSource<Item>
    let content: (Item) -> Content
    
    var body: some View {
        List {
            ForEach(pagingsource.items) { item in
                content(item)
            }
            if pagingsource.state == .loadingNextPage {
                ProgressView()
            } else if pagingsource.items.count < pagingsource.totalItems {
                ProgressView()
                    .onAppear {
                        Task {
                            await pagingsource.fetchNextPage()
                        }
                    }
            }
        }
        .hideListIndicators()
        .onAppear {
            if pagingsource.items.isEmpty {
                Task {
                    await pagingsource.fetchNextPage()
                }
            }
        }
    }
}
