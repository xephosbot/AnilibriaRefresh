import SwiftUI
import UIKit
import SharedUI

/// Hosts the Compose UI. `MainViewControllerKt.MainViewController()` starts Koin on first call and
/// returns the `ComposeUIViewController` that renders `AnilibriaApp` from `:shared-ui`.
///
/// The controller is stateful (it owns the Compose scene, its lifecycle and the navigation back
/// stack), so it is created once and never reconfigured from SwiftUI.
struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
