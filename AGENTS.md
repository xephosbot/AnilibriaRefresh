# AGENTS.md - AnilibertyRefresh Project Context

## Project Overview

**AnilibertyRefresh** is a modern Kotlin Multiplatform (KMP) application for streaming anime from the Aniliberty service. It targets Android, iOS, and JVM Desktop, sharing business logic, state, networking, and UI via Compose Multiplatform.

### Tech Stack & Libraries

- **Language**: Kotlin
- **UI Framework**: Compose Multiplatform (Material 3 Expressive, Material 3 Adaptive)
- **Architecture**: Clean Architecture + MVI + Multi-Module
- **State management**: Orbit MVI (`OrbitContainerHost` on top of Jetpack `ViewModel`)
- **DI**: Koin (compiler plugin + annotations in `:shared`, DSL modules in features)
- **Error handling**: Arrow (`Either<AppError, T>`)
- **Networking**: Ktor Client + Ktorfit (annotation-based API interfaces, KSP)
- **Persistence**: DataStore Preferences
- **Paging**: AndroidX Paging
- **Image Loading**: Coil 3
- **Navigation**: Navigation 3 (type-safe, serializable keys)
- **Serialization**: Kotlinx Serialization
- **Asynchrony**: Coroutines & Flow
- **Logging**: Kermit
- **Monitoring**: Kotzilla (Koin observability)
- **Video Player**: Media3 (ExoPlayer) / Compose Media Player

## Project Structure

### Root Directory Layout

- **`android-app/`**: Android application entry point.
- **`jvm-app/`**: Desktop (JVM) application entry point.
- **`ios-app/`**: iOS application (Xcode project). A thin Swift shim that hosts the Compose UI — no SwiftUI feature code. See `ios-app/AGENTS.md`.
- **`shared/`**: Business logic and screen state (Domain, Data, Network, ViewModels). Plain KMP library, produces no Apple framework.
- **`shared-ui/`**: Compose Multiplatform UI: app shell, navigation, design system, features. **Also the iOS framework producer** (`SharedUI`, static, arm64 only).

### Module Hierarchy

#### 1. Shared Core (`:shared`)

| Module | Contents |
|---|---|
| `:shared:common` | Cross-cutting primitives: `AppError`, `AsyncResult`, `AsyncLoad`, `DispatcherProvider`. No Compose. |
| `:shared:core:domain` | Domain models (`domain.models`), repository interfaces (`domain.repository`), use cases (`domain.usecase`). |
| `:shared:core:data` | Repository implementations, data sources, mappers (`Dto.toDomain()`), DataStore, platform modules. |
| `:shared:core:test-fixtures` | Fake domain models (`domain.fixtures`) and fake repositories (`data.fixtures`) for previews and tests. |
| `:shared:core:network` | Ktorfit API interfaces (`network.api`), DTOs, request bodies, responses, network enums, Ktor client setup. Platform engines live here. |
| `:shared:core:logger:api` / `:impl` | `AppLogger` abstraction; `KotzillaAppLogger` logs via Kermit and reports errors to Kotzilla. |
| `:shared:state:<feature>` | Screen state holders: `ViewModel`, `*ScreenState`, `*ScreenAction`, `*ScreenSideEffect`. |

**Dependency direction:** `data → domain`, `data → network`. `domain` depends on nothing but `:shared:common`. `api` modules never depend on `impl` modules; only the app shell wires `impl` modules together.

#### 2. Shared UI (`:shared-ui`)

| Module | Contents |
|---|---|
| `:shared-ui` (root) | App shell: `AnilibertyApp`, nav graph, navigation chrome, `initKoin`, Coil setup. iOS framework producer. |
| `:shared-ui:common` | Compose-side utilities: `dropUnlessResumed`, `polymorphic` serialization helper, `AppState`, clipboard. |
| `:shared-ui:resource` | Compose Multiplatform resources (`Res`: strings, drawables, fonts). |
| `:shared-ui:formatters` | Domain → display text (`Release`, `Episode`, dates, durations, `AppError` messages) and localization (`LocaleManager`). |
| `:shared-ui:navigation:api` | `NavKey`, `TopLevelNavKey`, `ExternalUriNavKey`, `Navigator`, `LocalNavigator`. |
| `:shared-ui:navigation:impl` | `AnilibertyNavigator`, navigation state, top-level routes, deep links. |
| `:shared-ui:navigation:scene` | Scene/entry decorators: global snackbar, `SharedViewModelStoreNavEntryDecorator`. |
| `:shared-ui:design-system:theme` | `AnilibertyTheme`, colors, typography, shapes, `LocalMargins`. |
| `:shared-ui:design-system:icons` | `AnilibertyIcons` (Material Symbols Rounded + brand logos) as `ImageVector`s. |
| `:shared-ui:design-system:components` | Reusable components (`PreferenceItem`, `PosterImage`, `Header`, `section` modifier, …). |
| `:shared-ui:feature:<name>:api` / `:impl` | Feature modules, see below. |

### Feature Modules (API/Impl)

Each feature is split into an `api` module (what other features may see) and an `impl` module (everything else). The matching state lives in `:shared:state:<feature>`.

**Available features:** `home` (Feed, Schedule), `search` (Filters, Results), `title` (Details), `player` (Video Playback), `favorite` (User Favorites), `preference` (Settings), `login` (Login dialog).

#### `:feature:<name>:api`

- Navigation keys (`@Serializable` `data object` / `data class` implementing `NavKey`, `TopLevelNavKey` or `ExternalUriNavKey`).
- `Navigator.navigateTo{Target}()` extension functions — the only way other features navigate here.
- Keys may carry UI metadata (title, icon, description) as `StringResource` / `ImageVector`. This is intentional: every destination, including list entries and external links, is a node of one navigation graph. Hence `api` may depend on `:shared-ui:resource` and `:shared-ui:design-system:icons`.
- Typical dependencies: `:shared-ui:navigation:api`, `:shared:core:domain` (for keys that carry domain types), `:shared-ui:resource`, `:shared-ui:design-system:icons`.

#### `:feature:<name>:impl`

Dependencies: its own `api`, `:shared:state:<feature>`, `:shared-ui:design-system:*`, `:shared-ui:common`, other features' **`api`** modules only (never another `impl`). `:shared:core:test-fixtures` is allowed for previews.

**Package layout** (root package `com.xbot.<feature>`):

```
com/xbot/<feature>/
├── di/
│   └── <Feature>FeatureModule.kt      # Koin DSL module: polymorphic NavKey registration + navigation<Key> entries
├── screen/
│   └── <screen>/                      # one folder per screen, ALWAYS — also for single-screen features
│       ├── <Screen>Pane.kt            # stateful root + stateless content + previews (or <Screen>Screen.kt)
│       └── <Screen>Components.kt      # optional: pieces used only by this screen
└── component/                         # optional: composables shared by 2+ screens of this feature
```

**Where a composable lives** (promote it when the rule changes):

1. Used by one screen → `screen/<screen>/`, `private` or `internal`.
2. Used by 2+ screens of the same feature → `component/`, `internal`.
3. Used by 2+ features → `:shared-ui:design-system:components`, `public`.

**Rules:**

- Platform-specific code (`expect`/`actual`) mirrors the same package path in `androidMain`/`iosMain`/`jvmMain`, e.g. `screen/player/PictureInPictureController.android.kt`.
- No `ui/`, `util/` or `utils/` folders. Helpers live next to the screen that uses them; shared ones go to `component/`.
- Composables used only as navigation scaffolding (e.g. a list-detail `detailPlaceholder`) belong in `component/`.
- Everything in `impl` is `internal` except the Koin module value (`val <feature>FeatureModule`).
- Legacy features still have flat files and `ui/` folders; they are migrated to this layout one feature at a time. New and touched code follows this layout.

#### `:shared:state:<feature>`

```
com/xbot/<feature>/
├── di/<Feature>StateModule.kt         # @Module @Configuration @ComponentScan("com.xbot.<feature>")
├── <Feature>ViewModel.kt              # single state holder for the feature, or …
├── <Feature>ScreenState.kt
├── <Feature>ScreenAction.kt
├── <Feature>ScreenSideEffect.kt
└── <screen>/                          # … one subpackage per screen when the feature has several ViewModels
    └── <Screen>ViewModel.kt, <Screen>ScreenState.kt, …
```

- The `<screen>` subpackage name equals the `impl` folder name: `state/…/preference/appearance/` ↔ `impl/…/preference/screen/appearance/`.
- One ViewModel may serve several screens (`HomeViewModel` → Feed + Schedule, `SearchViewModel` → Filters + Results). Screens sharing a ViewModel across nav entries use `SharedViewModelStoreNavEntryDecorator`.
- No Compose dependencies in state modules.

## Architecture & Code Style

### Clean Architecture Layers

1. **Domain** (`:shared:core:domain`): Source of truth.
    - Models: data classes (e.g., `Release`, `Episode`).
    - Repository interfaces (`domain.repository`); `data` implements them.
    - Use cases are concrete `@Factory` classes (no per-use-case interface, no `Default` prefix) that implement one of the base contracts in `domain/usecase/UseCase.kt`:
        - `EitherUseCase<P, R>` — `suspend`, fallible, returns `Either<AppError, R>`;
        - `UseCase<P, R>` — `suspend`, plain result (e.g. `Update*` setters returning `Unit`);
        - `FlowUseCase<P, R>` — non-suspending, returns `Flow<R>` (observed settings, paging).
    - Input goes in a nested `data class Params` declared inside the use case; use cases without input use `Unit` and are called as `useCase()` via the `invoke()` extensions (import `com.xbot.domain.usecase.invoke`).

    ```kotlin
    @Factory
    class GetReleaseUseCase(
        private val releasesRepository: ReleasesRepository,
    ) : EitherUseCase<GetReleaseUseCase.Params, ReleaseDetails> {
        data class Params(val aliasOrId: String)

        override suspend fun invoke(params: Params): Either<AppError, ReleaseDetails> =
            releasesRepository.getRelease(params.aliasOrId)
    }
    ```
2. **Data** (`:shared:core:data`, `:shared:core:network`): Implementation details.
    - Repository implementations (`Default{Noun}Repository`, `@Singleton`).
    - Mappers: `Dto.toDomain()`.
    - Network APIs are Ktorfit interfaces (`@GET`/`@POST`/`@Path`/`@Query`/`@Body`) returning `Either<AppError, T>`; `EitherConverterFactory` maps failures to `AppError`. Implementations are generated (`ktorfit.create{Name}Api()`) and provided in `NetworkModule`. Never hand-write an API implementation.
    - List query params are typed `List<T>?` with an array-style name, e.g. `@Query("f[genres][]")`. The API reads `key[]=a&key[]=b` as an array, but a repeated plain key (`key=a&key=b`) keeps only the last value. JSON bodies are `@Serializable` classes in `network.models.requests`.
    - Bearer auth is sent only for `accounts/users/me/**` and logout (path check in `NetworkModule`).
3. **State** (`:shared:state:*`): Orbit MVI ViewModels.
4. **UI** (`:shared-ui:*`): Compose screens rendering state, Unidirectional Data Flow.

### State Holders (Orbit MVI)

```kotlin
@KoinViewModel
class AppearanceViewModel(
    private val getThemeOptionUseCase: GetThemeOptionUseCase,
    private val updateThemeOptionUseCase: UpdateThemeOptionUseCase,
) : ViewModel(), OrbitContainerHost<AppearanceScreenState, AppearanceScreenState, Nothing> {

    override val container = orbitContainer(initialState = AppearanceScreenState()) {
        startObservingAppearance()
    }

    fun onAction(action: AppearanceScreenAction) {
        when (action) {
            is AppearanceScreenAction.OnThemeOptionChange -> onThemeOptionChange(action.option)
        }
    }

    private fun onThemeOptionChange(option: ThemeOption) = intent {
        updateThemeOptionUseCase(UpdateThemeOptionUseCase.Params(option))
    }
}
```

- Public surface: `container` + a single `fun onAction(action: {Feature}ScreenAction)`.
- One-off events (snackbars, navigation requests) go through the Orbit side-effect type `{Feature}ScreenSideEffect`, not through state.
- Use `Nothing` as the side-effect type when the screen has none.

### Naming Conventions

#### Classes & Files

- **ViewModels**: `{Feature}ViewModel` (e.g., `HomeViewModel`, `AppearanceViewModel`).
- **States**: `{Feature}ScreenState`.
- **Actions**: `{Feature}ScreenAction` (sealed interface, entries `On{Something}`).
- **Side effects**: `{Feature}ScreenSideEffect`.
- **Use Cases**: `{Verb}{Noun}UseCase` — the class itself, no interface or `Default` prefix.
- **Repositories**: `{Noun}Repository`.
- **Navigation keys**: `{Feature}Route`, `{Screen}Route`.
- **Koin modules**: `{Feature}FeatureModule` (DSL `val`, in `impl`), `{Feature}StateModule` / `DomainModule` / `DataModule` (annotated classes).

#### Navigation Callbacks

- **Naming**: Use `on[Target]Click` pattern (e.g., `onScheduleClick`, `onReleaseClick`, `onBackClick`, `onPreferenceClick`). Name the target, not the layout role (`onPreferenceClick`, not `onDetailClick`).
- **Lifecycle Protection**: All navigation callbacks must be wrapped with `dropUnlessResumed` using the custom extension.
- **Extensions**: Use helper extension functions for navigation (e.g., `navigator.navigateToSchedule()`) instead of raw `navigate()`.

**Example:**
```kotlin
// In <Feature>FeatureModule.kt
navigation<HomeRoute> {
    val navigator = LocalNavigator.current
    val lifecycleOwner = LocalLifecycleOwner.current
    FeedPane(
        onScheduleClick = lifecycleOwner.dropUnlessResumed {
            navigator.navigateToSchedule()
        },
        // For callbacks with parameters:
        onReleaseClick = { releaseId ->
            lifecycleOwner.dropUnlessResumed {
                navigator.navigateToTitle(releaseId)
            }.invoke()
        }
    )
}
```

#### Composables

1. **Stateful Screen (Root)**: Named `{Screen}Screen` or `{Screen}Pane`.
    * Accepts `ViewModel` (via `koinViewModel()`) as a defaulted parameter.
    * Collects state with Orbit's `viewModel.collectAsState()` and side effects with `viewModel.collectSideEffect { }`.
    * Receives navigation callbacks; never touches `Navigator` directly.
    * Passes data to the stateless content composable.
2. **Stateless Content**: Named `{Screen}ScreenContent` (or a private composable with explicit parameters).
    * Accepts `state`, `onAction` lambda, and specific event callbacks.
    * No ViewModel usage.
    * Preview friendly (`@AnilibertyPreview`, fixtures from `:shared:core:test-fixtures`).
3. **Adaptive layout decisions are made by the caller.** Screens do not read layout locals such as `LocalIsSinglePane` to decide what to render; the nav entry computes the value (e.g., `selectedRoute = if (isSinglePane) null else …`) and passes it in.
4. **Parameter order** follows the Compose API guidelines: required parameters, then `modifier: Modifier = Modifier`, then optional parameters, then trailing content lambda.
5. **Lazy lists**: provide `key` and `contentType` for items. Spacing between groups belongs to the group header/item, not to `Spacer` items between groups (a single trailing `Spacer` is fine).

**Example:**
```kotlin
// Stateful
@Composable
internal fun AppearancePane(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AppearanceViewModel = koinViewModel(),
) {
    val state by viewModel.collectAsState()
    AppearanceScreenContent(
        state = state,
        onAction = viewModel::onAction,
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

// Stateless
@Composable
private fun AppearanceScreenContent(
    state: AppearanceScreenState,
    onAction: (AppearanceScreenAction) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) { ... }
```

### Dependency Injection (Koin)

Two styles, by layer:

- **`:shared:*` modules (domain, data, network, logger, state)** use the **Koin compiler plugin with annotations**:
    - One `@Module @Configuration @ComponentScan("com.xbot.<package>") class <Name>Module` per module, in a `di` package. `@Configuration` makes it auto-included by `@KoinApplication`.
    - Classes are annotated directly: `@Factory` (use cases), `@Singleton` (repositories, clients, loggers), `@KoinViewModel` (ViewModels).
    - Apply `alias(libs.plugins.koin.compiler)` and enable `koinCompiler { compileSafety = true }`.
- **`:shared-ui:feature:*:impl` modules** use the **Koin DSL**:
    - `val <feature>FeatureModule = module { ... }` in `di/<Feature>FeatureModule.kt`.
    - Registers back-stack keys via `polymorphic<NavKey> { subclass(...) }` and screens via `navigation<Key>(metadata = ...) { ... }`.
    - Must be added to `modules(...)` in `shared-ui/.../sharedapp/di/StartKoin.kt`.

### Navigation (Navigation 3)

- Type-safe keys defined in `:api` modules; serializable data classes/objects.
- `Navigator` (`LocalNavigator.current`) abstracts all navigation; screens receive callbacks, nav entries call the navigator.
- **External links** are `ExternalUriNavKey`s and go through the navigator like any other destination; `AnilibertyNavigator` opens the URI instead of pushing it.
- **Only keys that can end up on the back stack** are registered in `polymorphic<NavKey>`. `ExternalUriNavKey`s never reach the back stack and must not be registered.
- Keys with `requiresLogin = true` are redirected to `LoginRoute(returnTo = key)` by the navigation interceptor in the app shell.
- Adaptive layouts use scene strategies in entry `metadata` (`ListDetailSceneStrategy`, `SupportingPaneSceneStrategy`, `DialogSceneStrategy`).

### General Code Style Guidelines

- **Visibility**: Always prefer `internal` visibility for classes, functions, and properties if they are not required to be `public`. Feature implementation details, repository and use case implementations, and internal helpers MUST be `internal`.
- **Imports**: Keep imports clean and remove any unused imports.
- **File Formatting**: Every file MUST end with a single new line.

## Adding Dependencies

All dependencies are declared in `gradle/libs.versions.toml` and consumed via the version catalog (`libs.*`) and type-safe project accessors (`projects.*`). Never hardcode versions directly in `build.gradle.kts`.

### build.gradle.kts Structure

Modules use the `android.multiplatform.library` plugin with a standard layout:

```kotlin
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    // add other plugins as needed, e.g. kotlin.serialization, koin.compiler, compose.compiler
}

kotlin {
    android {
        namespace = "com.xbot.<area>.<module>"
        compileSdk {
            version = release(libs.versions.android.compilesdk.get().toInt())
        }
        minSdk {
            version = release(libs.versions.android.minsdk.get().toInt())
        }
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    jvmToolchain(21)

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        // common dependencies go here
    }

    sourceSets {
        androidMain.dependencies {
            // Android-specific dependencies
        }
        iosMain.dependencies {
            // iOS-specific dependencies
        }
        jvmMain.dependencies {
            // Desktop-specific dependencies
        }
    }
}

// Only for modules that apply libs.plugins.koin.compiler
koinCompiler {
    compileSafety = true
}
```

Compose modules additionally apply `compose.compiler` + `compose.multiplatform` and add `androidRuntimeClasspath(libs.compose.ui.tooling)` in the top-level `dependencies { }` block for previews.

### Rules

- **Common dependencies** go in the top-level `@OptIn(ExperimentalKotlinGradlePluginApi::class) dependencies { }` block inside `kotlin { }`. This makes them available on all targets.
    - **Exception — modules that apply the Kotzilla plugin** (`:shared-ui` and `:shared-ui:feature:*:impl`): declare common dependencies in `sourceSets.commonMain.dependencies { }` instead. Kotzilla 3.0.0 reads the module's dependencies when it is applied, which freezes the top-level `dependencies { }` block and fails the build with "The value for property 'implementation' ... is final".
- **Platform-specific dependencies** go in the corresponding `sourceSets` block:
    - `androidMain.dependencies { }` — Android only (e.g., OkHttp, Brotli decoder, AndroidContextProvider)
    - `iosMain.dependencies { }` — iOS only (e.g., `ktor-client-darwin`)
    - `jvmMain.dependencies { }` — Desktop only (e.g., `ktor-client-cio`)
- **Ktor engine** is always platform-specific (`:shared:core:network`): `okhttp` for Android, `darwin` for iOS, `cio` for JVM. Never add an engine to common dependencies.
- Use `api(...)` only when the dependency's types appear in the module's public API (e.g., `data` exposes `domain` repository interfaces); otherwise `implementation(...)`.
- When adding a new library, always add its version to `[versions]` and its coordinates to `[libraries]` in `libs.versions.toml` first, following the existing grouping structure.

## Configuration

### Key Versions (`gradle/libs.versions.toml` is authoritative)

- **Kotlin**: 2.4.20
- **Compose Multiplatform**: 1.13.0-alpha01
- **Android Gradle Plugin**: 9.4.1
- **Navigation 3**: runtime 1.2.0, UI 1.1.2
- **Koin**: 4.2.2 (compiler plugin 1.2.1)
- **Orbit MVI**: 12.0.1
- **Ktor**: 3.6.0
- **Ktorfit**: 2.7.5
- **Coil**: 3.6.3
- **Kotzilla**: 3.0.0
- **Android SDK**: compile/target 37, min 24
- **iOS deployment target**: 18.0

### API Configuration

- **Base URL**: `https://aniliberty.top/api/v1/`
- **Asset URL**: `https://aniliberty.top`

## Build & Run

- **Android**: `./gradlew :android-app:assembleDebug`
- **Desktop**: `./gradlew :jvm-app:run`
- **iOS**: Standard Xcode build workflow. The `Compile Kotlin Framework` phase runs `./gradlew :shared-ui:embedAndSignAppleFrameworkForXcode`; for a Kotlin-only check use `./gradlew :shared-ui:linkDebugFrameworkIosSimulatorArm64`. See `ios-app/AGENTS.md`.
- **Quick compile check of one module**: `./gradlew :<module path>:compileKotlinJvm`.

### Formatting & static analysis

- **Format**: `./gradlew spotlessApply` — Spotless + ktlint (`android_studio` style, rules in `.editorconfig`). Run it before committing; `spotlessCheck` verifies.
- **Static analysis**: `./gradlew :detekt` — one root-level run over every module's `src/`, config in `config/detekt/detekt.yml`.
- **Baseline**: existing detekt findings live in the single `config/detekt/baseline.xml`. Fix new findings instead of re-baselining; regenerate with `./gradlew :detektBaseline` only when deliberately accepting debt.
- Suppress a ktlint rule locally with `@Suppress("ktlint:standard:<rule>")` and a comment saying why (e.g. Swift-facing `MainViewController`, KMP actual files named after their common file).

## Development Notes

- **Multiplatform Resources**: Use `Res` object from `:shared-ui:resource` for strings/images.
- **Image Loading**: Use `PosterImage` component (wraps Coil).
- **Icons**: Use `AnilibertyIcons` from `:shared-ui:design-system:icons` (Material Symbols Rounded; brand logos from Simple Icons). Do not add `material-icons-extended`.
- **Design System**: Strict usage of `AnilibertyTheme` and components in `:shared-ui:design-system:*`. Horizontal screen margins come from `LocalMargins`.
- **Error Handling**: `AppError` sealed class in `:shared:common` (`ServerError`, `ConnectionError`, `UnknownError`), returned as `Either<AppError, T>` from use cases. ViewModels map errors to UI state/side effects; `:shared-ui:formatters` turns them into user-facing messages.
