# AGENTS.md - ios-app (Anilibria Refresh, iOS)

## Overview

**Thin Compose Multiplatform shim.** The whole UI — screens, design system, navigation, state, DI —
lives in the Kotlin `:shared-ui` module and ships as the **`SharedUI`** framework. Swift owns
nothing but the app lifecycle: it creates the window and hands it to Compose.

There is deliberately **no SwiftUI feature code**. If a screen needs to change, change it in
`shared-ui/feature/*`, not here. The previous native-SwiftUI iOS app (its own views, view-model
bridge, Nuke image loading, `AppRouter`) was removed in the CMP migration.

## Project layout (`src/`)

```
src/
├── App/
│   ├── AnilibriaApp.swift   // @main App — one WindowGroup hosting ComposeView
│   └── ComposeView.swift    // UIViewControllerRepresentable over MainViewControllerKt
└── Info.plist               // MUST stay here (INFOPLIST_FILE = src/Info.plist)
```

That is the entire Swift surface, and it should stay that way.

## Consuming the `SharedUI` framework

- Entry point is `MainViewControllerKt.MainViewController()` (Kotlin:
  `shared-ui/src/iosMain/kotlin/MainViewController.kt`). It **starts Koin on first call** and
  returns the `ComposeUIViewController` rendering `AnilibriaApp()`. Swift must not call
  `initKoin` itself — the guard in Kotlin is the single source of truth.
- The controller owns the Compose scene, its lifecycle and the navigation back stack, so it is
  created once in `makeUIViewController` and never reconfigured.
- `ComposeView` uses `.ignoresSafeArea(.all)`: Compose draws edge-to-edge and applies its own
  window insets. Do not re-add SwiftUI safe-area padding around it.

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
  xcodebuild -project ios-app/AnilibriaRefresh.xcodeproj -scheme ios-app \
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
