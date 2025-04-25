//
//  AnilibriaRefreshApp.swift
//  AnilibriaRefresh
//
//  Created by xephosbot on 11/04/25.
//

import SwiftUI
import shared

@main
struct AnilibriaRefreshApp: App {
    init () {
        StartKoinKt.doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
