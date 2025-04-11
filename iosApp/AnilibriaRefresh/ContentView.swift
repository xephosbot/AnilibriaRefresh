//
//  ContentView.swift
//  AnilibriaRefresh
//
//  Created by xephosbot on 11/04/25.
//

import UIKit
import SwiftUI
import shared

struct ContentView: View {
    var body: some View {
        ZStack {
            ComposeView()
                .ignoresSafeArea(.all) // Compose has own keyboard handler
        }
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        Main_iosKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
