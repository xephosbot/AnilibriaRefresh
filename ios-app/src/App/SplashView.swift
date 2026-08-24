import SwiftUI

/// The iOS launch storyboard cannot animate: the system draws it before any app code runs. So it
/// only paints the background, and this view continues from there with the same stroke-tracing
/// animation that `avd_splash.xml` plays on Android — same paths, same schedule, same colours.
///
/// It stays up until both the animation has finished *and* Compose has drawn, which is the job
/// `SplashScreen.setKeepOnScreenCondition` does on the Android side.
struct SplashView: View {
    /// Whether Compose has put a frame on screen behind this view.
    var isContentReady: Bool
    var onFinished: () -> Void

    @Environment(\.accessibilityReduceMotion) private var reduceMotion

    @State private var traced: [CGFloat] = Array(repeating: 0, count: SplashLogo.strokes.count)
    @State private var filled: [Double] = Array(repeating: 0, count: SplashLogo.strokes.count)
    @State private var opacity: Double = 1
    @State private var strokesFinished = false
    @State private var waitedLongEnough = false
    @State private var isLeaving = false

    var body: some View {
        ZStack {
            Color("SplashBackgroundColor")
            ZStack {
                ForEach(SplashLogo.strokes.indices, id: \.self) { index in
                    let shape = LogoShape(outline: SplashLogo.strokes[index])
                    shape
                        .trim(from: 0, to: traced[index])
                        .stroke(
                            SplashLogo.tint,
                            style: StrokeStyle(lineWidth: SplashLogo.lineWidth, lineCap: .butt, lineJoin: .miter)
                        )
                        .opacity(1 - filled[index])
                    shape
                        .fill(SplashLogo.tint)
                        .opacity(filled[index])
                }
            }
            .frame(width: SplashLogo.size, height: SplashLogo.size)
        }
        .ignoresSafeArea(.all)
        .opacity(opacity)
        .onAppear(perform: play)
        .onChange(of: canHandOff) { if canHandOff { handOff() } }
        .task {
            // Compose builds its Koin graph synchronously before it can draw, so readiness
            // depends on work this view does not control. Leave regardless once the ceiling
            // passes: a stalled start must never strand the splash on screen.
            try? await Task.sleep(for: SplashLogo.readinessCeiling)
            // A cancelled sleep means the splash is already leaving, not that the wait expired.
            guard !Task.isCancelled else { return }
            waitedLongEnough = true
        }
    }

    private func play() {
        // Reduce Motion rules out the tracing itself, but a cross-fade is still allowed,
        // so the logo simply starts at its final state and the view dissolves as usual.
        guard !reduceMotion else {
            traced = traced.map { _ in 1 }
            filled = filled.map { _ in 1 }
            strokesFinished = true
            return
        }

        for index in SplashLogo.strokes.indices {
            let start = Double(index) * SplashLogo.stagger
            withAnimation(.timingCurve(0.4, 0, 0.2, 1, duration: SplashLogo.trace).delay(start)) {
                traced[index] = 1
            }

            let swap = Animation.easeInOut(duration: SplashLogo.crossfade)
                .delay(start + SplashLogo.trace)
            guard index == SplashLogo.strokes.indices.last else {
                withAnimation(swap) { filled[index] = 1 }
                continue
            }
            withAnimation(swap, completionCriteria: .logicallyComplete) {
                filled[index] = 1
            } completion: {
                strokesFinished = true
            }
        }
    }

    /// Escaping closures capture a copy of this struct, so anything they read of
    /// `isContentReady` is frozen at the moment they were made. Deriving the decision here
    /// instead keeps it on the value SwiftUI re-evaluates the body with, and `.onChange`
    /// drives it — the closures only ever *write* state, which a stale copy still does correctly.
    private var canHandOff: Bool {
        strokesFinished && (isContentReady || waitedLongEnough) && !isLeaving
    }

    private func handOff() {
        isLeaving = true
        withAnimation(.easeIn(duration: SplashLogo.fadeOut)) {
            opacity = 0
        } completion: {
            onFinished()
        }
    }
}

private struct LogoShape: Shape {
    let outline: Path

    func path(in rect: CGRect) -> Path {
        let scale = min(rect.width, rect.height) / SplashLogo.viewBox
        return outline.applying(CGAffineTransform(scaleX: scale, y: scale))
    }
}

private enum SplashLogo {
    /// Coordinate space of `AppIcon.icon/Assets/icon.svg`, which the outlines are generated from.
    static let viewBox = SplashLogoPaths.viewBox
    static let size: CGFloat = 200
    static let tint = Color(red: 254 / 255, green: 54 / 255, blue: 53 / 255)

    /// avd_splash.xml strokes 10.53 units over artwork spanning 686.79 of its viewport.
    static let lineWidth: CGFloat = size * 10.53 / 686.79

    // Timings mirror avd_splash.xml: trace 280ms, 120ms apart, then a 120ms stroke-to-fill swap.
    static let trace = 0.28
    static let stagger = 0.12
    static let crossfade = 0.12
    static let fadeOut = 0.2

    /// How long to wait on Compose before leaving anyway.
    static let readinessCeiling: Duration = .seconds(5)

    /// Traced in the order Android animates them: the ring, then the A, then the L.
    static let strokes: [Path] = [
        SplashLogoPaths.arc,
        SplashLogoPaths.diagonal,
        SplashLogoPaths.hook,
    ]
}
