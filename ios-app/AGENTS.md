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

### Native navigation chrome (`shared-ui/src/iosMain/kotlin/com/xbot/sharedapp/ios/`)

`AnilibertyTabBarController` is a `UITabBarController` that exists purely to get the system Liquid
Glass tab bar. It is **not** a navigation container:

- Navigation 3 stays the single source of truth. A tab tap is *refused* in
  `tabBarController(_:shouldSelectTab:)`, which only forwards the route to the navigator; the
  selection is then applied from the resulting Compose state. Never mirror state back the other
  way. (This is deliberately the opposite of the JetBrains Liquid Glass tutorial, which drains the
  Compose back stack into native containers — that would break the scene strategies, the shared
  element transitions, the snackbar decorator and the per-entry `ViewModelStore`s.)
- One single `ComposeUIViewController` hosts the entire app and is a child of the tab bar
  controller, not of any tab, so the composition survives every tab switch. Each tab is backed by
  a transparent, non-interactive `PassthroughViewController`.
- View order matters: the Compose view must sit between `UITransitionView` (the tab's content) and
  `_UITabContainerView` (the bar). Above the bar container it hides the bar; below the transition
  view its wrapper swallows every touch. `keepComposeAboveTabContent()` re-asserts this on each
  layout pass because UIKit rebuilds the hierarchy on tab changes.
- The tab bar height is fed to Compose through `additionalSafeAreaInsets`, minus the inherited
  safe area so the home indicator is not counted twice. Compose insets need no other wiring.
- Dependencies arrive via `configure(...)`, never the constructor: `UITabBarController`'s
  designated initializer loads its view, so `viewDidLoad` runs while a Kotlin subclass's fields are
  still uninitialized. For the same reason, `addChildViewController` must precede reading the
  child's `view` — it returns nil beforehand despite the non-null binding.
- Everything the chrome mirrors is pushed from inside the composition (`NativeNavigationChrome` in
  `AnilibertyApp.kt`, which sits inside `ProvideAppLocale` and `AnilibertyTheme`), so tab labels and
  the bar's appearance follow a language or theme change for free. Do not resolve them from
  `iosMain` — a one-shot `getString` outside the composition cannot see either. SF Symbols are
  mapped in `TabBarItems.kt` so the shared route contract stays free of iOS specifics.
- The theme is pushed as the raw `ThemeOption`, never a resolved "is dark" flag:
  `overrideUserInterfaceStyle` is set on the controller (so the status bar and keyboard follow too),
  and `ThemeOption.System` must stay `Unspecified` — pinning it would also pin the trait collection
  the Compose child inherits, and the app would stop tracking the system.
- Minimising the tab bar on scroll does **not** work automatically: UIKit drives it from
  `setContentScrollView:forEdge:` and Compose provides no `UIScrollView`. Drive
  `setTabBarHidden(_:animated:)` from Compose scroll state if that behaviour is wanted.

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
