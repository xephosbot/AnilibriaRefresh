# AGENTS.md - ios-app (Aniliberty Refresh, iOS)

## Overview

**Thin Compose Multiplatform shim.** The whole UI — screens, design system, navigation, state, DI —
lives in the Kotlin `:shared-ui` module and ships as the **`SharedUI`** framework. Swift owns
nothing but the app lifecycle: it creates the window and hands it to Compose.

There is deliberately **no SwiftUI feature code**. If a screen needs to change, change it in
`shared-ui/feature/*`, not here. The previous native-SwiftUI iOS app (its own views, view-model
bridge, Nuke image loading, `AppRouter`) was removed in the CMP migration.

The one rendering exception is the **launch animation** (`SplashView.swift`). The launch storyboard
is drawn by the system before any app code runs and cannot animate, so the animation has to start
in-process, above Compose, before the framework has finished starting. Do not move it to
`shared-ui`. It clears only once its animation has finished *and* Compose reports a drawn frame,
which is what `SplashScreen.setKeepOnScreenCondition` does on Android.

## Project layout (`src/`)

```
src/
├── App/
│   ├── AnilibertyApp.swift     // @main App — ComposeView with SplashView layered over it
│   ├── ComposeView.swift       // hosts MainViewControllerKt, reports its first drawn frame
│   ├── SplashView.swift        // launch animation, mirrors android-app's avd_splash.xml
│   └── SplashLogoPaths.swift   // GENERATED logo outlines — see tools/generate-splash-paths.py
└── Info.plist                  // MUST stay here (INFOPLIST_FILE = src/Info.plist)
```

`SplashLogoPaths.swift` is generated from `AppIcon.icon/Assets/icon.svg`; never edit it by hand.
After changing the artwork, run `tools/generate-splash-paths.py` (needs `brew install swiftdraw`)
and re-check the outlines against `android-app`'s `avd_splash.xml`, which is maintained separately —
the system draws Android's splash from a resource before the app starts, so it cannot share code.

That is the entire Swift surface, and it should stay that way.

## Consuming the `SharedUI` framework

- Entry point is `Main_iosKt.MainViewController()` (Kotlin:
  `shared-ui/src/iosMain/kotlin/main.ios.kt`, a top-level file with no package, hence the
  `Main_iosKt` Objective-C name). It **starts Koin on first call** and returns an
  `AnilibertyTabBarController`. Swift must not call `initKoin` itself — the guard in Kotlin is the
  single source of truth.
- The controller owns the Compose scene, its lifecycle and the navigation back stack, so it is
  created once in `makeUIViewController` and never reconfigured.
- `ComposeView` uses `.ignoresSafeArea(.all)`: Compose draws edge-to-edge and applies its own
  window insets. Do not re-add SwiftUI safe-area padding around it.

### Native navigation bar (`shared-ui/src/iosMain/kotlin/com/xbot/sharedapp/ios/`)

`AnilibertyTabBarController` is a `UITabBarController` that exists purely to get the system Liquid
Glass tab bar. It is **not** a navigation container.

Everything in this package is `internal` — Swift still sees only `MainViewController()`.
`commonMain` declares a `NavigationChrome` and reads it from `LocalNavigationChrome`; the default
draws a `NavigationSuiteScaffold`, and `main.ios.kt` is the only place that swaps in
`NativeTabBarChrome`. Do not add a platform parameter or nullable host to `AnilibertyApp`.

- Navigation 3 is the source of truth. A tap is *refused* in `shouldSelectTab` and only forwarded
  to the navigator; selection is then applied from the resulting Compose state. Never mirror back.
- One `ComposeUIViewController` hosts the whole app as a child of the tab bar controller, not of a
  tab, so the composition survives tab switches. Tabs hold transparent `PassthroughViewController`s.
- The Compose view must sit above the tab's content wrapper and below the bar.
  `keepComposeAboveTabContent()` re-asserts this each layout pass — UIKit rebuilds the hierarchy.
- Dependencies arrive via `attach(...)`, never the constructor: the designated initializer loads the
  view, so `viewDidLoad` runs before Kotlin fields exist. Likewise `addChildViewController` must
  precede reading the child's `view`. Pushes before attach are buffered and replayed.
- State arrives as one `NativeTabBarState` pushed from inside the composition, so labels and theme
  follow locale/theme changes for free. Never resolve them outside it. Theme is pushed as raw
  `ThemeOption`; `System` must map to `Unspecified` or the Compose child stops tracking the system.
- `NavKey.hidesNavigationBar` is platform-neutral, not an iOS hook: iOS hides the native bar,
  Compose collapses its navigation suite.
- Tab bar height reaches Compose via `additionalSafeAreaInsets`, minus the inherited safe area.
- Minimising the bar on scroll does **not** work automatically — UIKit drives it from
  `setContentScrollView:forEdge:` and Compose provides no `UIScrollView`.

## When Swift code *is* justified

Only for things Compose cannot reach from Kotlin, and even then keep the Swift file a bridge:
platform entitlements/permission prompts, `UIApplicationDelegate` hooks, deep links
(`.onOpenURL` → forward to the Kotlin `ExternalUriHandler`, which is what Android/desktop do).
Anything that renders belongs in `shared-ui`.

## Build & run

- **Xcode project uses synchronized folders** (`PBXFileSystemSynchronizedRootGroup` on `src`). Any
  file added under `src/` joins the target automatically — **never hand-edit `project.pbxproj`** to
  add sources.
- **Do not move** `Info.plist` (`INFOPLIST_FILE = src/Info.plist`). Asset resources
  (`Assets.xcassets`, `AppIcon.icon`, `Launch Screen.storyboard`, `Localizable.xcstrings`) live at
  the `ios-app/` root and are referenced by path — leave them there.
- **Scheme:** `ios-app`. The `Compile Kotlin Framework` build phase runs
  `./gradlew :shared-ui:embedAndSignAppleFrameworkForXcode`, so the framework rebuilds on every
  build. `:shared` no longer produces a framework — it is linked in transitively through
  `:shared-ui`.
- **The framework is arm64-only** (`iosArm64`, `iosSimulatorArm64` — no `iosX64`). Build the
  simulator with arm64; an x86_64 slice will fail to link:
  ```
  xcodebuild -project ios-app/AnilibertyRefresh.xcodeproj -scheme ios-app \
    -sdk iphonesimulator -destination 'generic/platform=iOS Simulator' \
    ARCHS=arm64 ONLY_ACTIVE_ARCH=YES CODE_SIGNING_ALLOWED=NO build
  ```
- Kotlin-only iteration is much faster than a full Xcode build:
  `./gradlew :shared-ui:linkDebugFrameworkIosSimulatorArm64`.
- No Swift Package Manager dependencies. Nuke / NukeUI / SwiftUI-Shimmer were dropped with the
  SwiftUI code — images go through Coil in Compose.
- Storyboards can be validated without a full build: `ibtool --compile /tmp/out.storyboardc "Launch Screen.storyboard"`.

## Obj-C/Swift surface

The framework is built **without SKIE and without `export(...)`**, so the generated header exposes
essentially one symbol: `MainViewControllerKt.MainViewController()`. Kotlin models, use cases and
ViewModels are intentionally *not* part of the Swift API any more — nothing on the Swift side needs
them. Do not add `export(...)` entries to `shared-ui/build.gradle.kts` to "make a type visible";
that is a sign logic is being pulled back into Swift, which this module no longer does.

The `@HiddenFromObjC` annotations still present in `:shared` are inert leftovers from the SwiftUI
era and harmless.
