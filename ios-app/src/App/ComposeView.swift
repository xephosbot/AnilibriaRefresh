import SwiftUI
import UIKit
import SharedUI

/// Hosts the Compose framework's view controller and reports when it has actually
/// drawn, so the splash knows when handing over will land on real content rather
/// than an empty canvas.
struct ComposeView: UIViewControllerRepresentable {
    var onFirstFrame: () -> Void

    func makeUIViewController(context: Context) -> UIViewController {
        ComposeHostController(onFirstFrame: onFirstFrame)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

private final class ComposeHostController: UIViewController {
    private let onFirstFrame: () -> Void
    private var displayLink: CADisplayLink?
    private var hasReported = false

    init(onFirstFrame: @escaping () -> Void) {
        self.onFirstFrame = onFirstFrame
        super.init(nibName: nil, bundle: nil)
    }

    @available(*, unavailable)
    required init?(coder: NSCoder) {
        fatalError("ComposeHostController is created in code, never from a nib")
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        let compose = Main_iosKt.MainViewController()
        addChild(compose)
        compose.view.frame = view.bounds
        compose.view.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        view.addSubview(compose.view)
        compose.didMove(toParent: self)
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        // `viewDidAppear` only promises the view is in the hierarchy — Compose draws on
        // its own scheduler, so its first frame may still be pending. Waiting for the
        // next display refresh is the closest proxy Swift has for "content is on screen".
        guard !hasReported, displayLink == nil else { return }
        let link = CADisplayLink(target: self, selector: #selector(displayRefreshed))
        link.add(to: .main, forMode: .common)
        displayLink = link
    }

    @objc private func displayRefreshed() {
        displayLink?.invalidate()
        displayLink = nil
        guard !hasReported else { return }
        hasReported = true
        onFirstFrame()
    }
}
