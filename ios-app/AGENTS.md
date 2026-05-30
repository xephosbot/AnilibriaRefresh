# AGENTS.md - ios-app (Anilibria Refresh, iOS)

## Overview

Native **SwiftUI** app built on top of the Kotlin Multiplatform **`Shared`** framework. SwiftUI is
only the **view + presentation glue**; all state and business logic live in `Shared` (Orbit MVI
ViewModels, Koin DI, Clean Architecture). The iOS feature set mirrors `shared-ui/feature` 1:1, but
renders native SwiftUI instead of Compose.

Image loading uses **Nuke** (added as a Swift package). Sample data for `#Preview` comes from the
shared KMP `domain/test-fixtures` module, not hand-written Swift.

## Project layout (`src/`)

Organized **feature-first**, mirroring `shared-ui`:

```
src/
├── App/            // entry point + composition root (@main AnilibriaApp, RootView, Koin init)
├── Core/           // cross-feature infrastructure (≈ shared-ui/common + navigation)
│   ├── Architecture/   // KMP lifecycle bridge: IosViewModelStoreOwner, ViewModelStoreOwnerProvider
│   └── Network/        // RelativeURLDataLoader (Nuke <-> shared ImageUrlProvider)
│   └── Navigation/     // Route / TopLevelRoute marker protocols (≈ NavKey)
├── DesignSystem/   // reusable UI (≈ shared-ui/design-system)
│   └── Components/     // PosterImage, ReleaseRow, ReleaseMetaText, FeaturePlaceholder
├── Features/       // one folder per feature, names match shared-ui/feature
│   ├── Home/  Login/  Search/  Title/  Player/  Favorite/  Preference/
├── Presentation/   // SwiftUI <-> Shared-domain glue
│   └── Extensions/    // AgeRating+DisplayName, ReleaseType+DisplayName
└── Info.plist      // MUST stay here (INFOPLIST_FILE = src/Info.plist)
```

`Home` carries a `HomeRoute.swift` (the feature's navigation "api" contract). Single-view features
keep one file; promote to `Views/` + `Navigation/` subfolders only when a feature grows.

## Consuming the `Shared` framework

- **ViewModels.** Resolve through the lifecycle bridge, never construct directly:
  ```swift
  @EnvironmentObject var store: IosViewModelStoreOwner
  let viewModel: HomeViewModel = store.viewModel()
  ```
  Read state by collecting the Orbit container flow:
  ```swift
  for await anyState in viewModel.container.stateFlow { state = anyState as? HomeScreenState }
  ```
  ViewModels with `@Provided` runtime args (`TitleViewModel`, `PlayerViewModel`) take Koin params:
  `store.viewModel(parameters: { ParametersHolder(...) })`. See `Features/*/*.swift` headers.
- **DI.** `KoinKt.doInitKoin()` is called once in `AnilibriaApp.init()` before anything resolves.
- **Results.** State exposes `AsyncResult<DomainError, T>`; consume it via SKIE: `onEnum(of:)` with
  `.success / .loading / .error`.
- **Images.** The Nuke pipeline is configured in `AnilibriaApp.configureImagePipeline()` with
  `RelativeURLDataLoader`, which rewrites API-relative paths to absolute URLs using the shared
  `ImageUrlProvider` resolved from Koin via `imageUrlProvider()`. Do not hardcode the asset base
  URL in Swift — it lives in Kotlin (`Constants`).
- **Preview fixtures.** Use shared samples, e.g. `ReleaseFixtures.shared.frieren` /
  `ReleaseFixtures.shared.oshiNoKo`. Kotlin `object`s are accessed via `.shared` in Swift.

## Conventions

- **Feature-first, not type-first.** Group by feature (`Home/`), not by type (`ViewModels/`).
- **Dependencies point down:** `App → Features → Core → DesignSystem → resources`. Features talk to
  each other only through `Route` contracts, never another feature's views.
- **Thin views.** No business logic in SwiftUI. State/logic stay in `Shared`.
- **Naming (Kotlin side, consumed here):** `{Feature}ViewModel`, `{Feature}ScreenState`,
  `{Feature}ScreenAction`. SwiftUI screens are `{Feature}View`.
- **Swift idioms:** extension files `Type+Feature.swift`; `#Preview` next to the view;
  `@main struct AnilibriaApp`.

## Build & run

- **Xcode project uses synchronized folders** (`PBXFileSystemSynchronizedRootGroup` on `src`). Any
  file/folder added under `src/` is included in the target automatically — **never hand-edit
  `project.pbxproj`** to add sources. Reorganizing folders inside `src/` is safe (single Swift
  module, so no imports break between files).
- **Do not move** `Info.plist` (referenced by `INFOPLIST_FILE = src/Info.plist`). Asset resources
  (`Assets.xcassets`, `AppIcon.icon`, `Launch Screen.storyboard`, `Localizable.xcstrings`) live at
  the `ios-app/` root and are referenced by path — leave them there.
- **Scheme:** `ios-app`. The `Compile Kotlin Framework` build phase runs
  `./gradlew :shared:embedAndSignAppleFrameworkForXcode`, so the framework rebuilds on every build.
- **The `Shared` framework is arm64-only** (`iosArm64`, `iosSimulatorArm64` — no `iosX64`). Build
  the simulator with arm64; an x86_64 simulator slice will fail to link:
  ```
  xcodebuild -project ios-app/AnilibriaRefresh.xcodeproj -scheme ios-app \
    -sdk iphonesimulator -destination 'generic/platform=iOS Simulator' \
    ARCHS=arm64 ONLY_ACTIVE_ARCH=YES CODE_SIGNING_ALLOWED=NO build
  ```
- Storyboards can be validated without a full build: `ibtool --compile /tmp/out.storyboardc "Launch Screen.storyboard"`.

## Shared framework Obj-C/Swift surface (API hygiene)

The framework header is deliberately kept lean. When exposing Kotlin to Swift:

- **Only models, ViewModels + their state, `AsyncResult`, lifecycle/SavedState, minimal Koin, and
  `ImageUrlProvider` are public.** Use cases and repositories are hidden with `@HiddenFromObjC`
  (kept `public` for Kotlin/Koin, invisible to Swift). `@HiddenFromObjC` works directly in
  `commonMain` on Kotlin 2.3 — no `expect/actual` shim needed.
- **Subtype rule:** a subtype of a `@HiddenFromObjC` type must also be `@HiddenFromObjC`. Hiding an
  interface means hiding its implementations too (e.g. `*UseCase` interface + its `Default*` impl).
- **To make a Kotlin type visible to Swift**, it must be reachable from an exported declaration as a
  **concrete** type (generic type arguments get erased, so a model only used as `List<Model>`
  inside state won't surface on its own). The umbrella accessor pattern works:
  `fun imageUrlProvider(): ImageUrlProvider = KoinPlatform.getKoin().get()` pulls in exactly that
  interface without exporting the whole network module.
- **Types from non-exported modules get a module-prefixed Obj-C name** (e.g. `ImageUrlProvider`
  from the non-exported `network/api` surfaces as `ApiImageUrlProvider` in Swift). `@ObjCName` does
  **not** override this prefix — the only way to a clean name is to move the type into an exported
  module. The prefixed name is accepted here.
- **`kotlinx.serialization` types in the header come from the Kotzilla SDK** (compiled into the iOS
  framework via the Kotzilla Gradle plugin), not from app code. They cannot be removed with
  annotations (third-party); the only lever is not shipping Kotzilla on iOS. Left as-is on purpose.
