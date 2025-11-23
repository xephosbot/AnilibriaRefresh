# AGENTS.md - AnilibriaRefresh Project Context

## Project Overview

**AnilibriaRefresh** is a modern Kotlin Multiplatform (KMP) application for streaming anime from the Anilibria service. It targets Android, iOS, and JVM Desktop, sharing business logic, networking, and UI via Compose Multiplatform.

### Tech Stack & Libraries

- **Language**: Kotlin 2.0+
- **UI Framework**: Compose Multiplatform (Material 3)
- **Architecture**: Clean Architecture + MVI + Multi-Module
- **DI**: Koin
- **Networking**: Ktor Client
- **Image Loading**: Coil 3
- **Navigation**: Jetpack Navigation 3 (Type-safe)
- **Serialization**: Kotlinx Serialization
- **Asynchrony**: Coroutines & Flow
- **Video Player**: Media3 (ExoPlayer) / Compose Media Player

## Project Structure

### Root Directory Layout

- **`android-app/`**: Android application entry point.
- **`jvm-app/`**: Desktop (JVM) application entry point.
- **`ios-app/`**: iOS application (Xcode project) wrapping the shared UI.
- **`shared/`**: Core business logic (Domain, Data, Network).
- **`shared-ui/`**: Compose Multiplatform UI, features, and design system.

### Module Hierarchy

#### 1. Shared Core (`:shared`)
- **`:shared:domain`**: Pure Kotlin. Entities, Use Cases, Repository Interfaces. No platform dependencies.
- **`:shared:data`**: Repository implementations, Data Sources, DB, Settings.
- **`:shared:network`**: Ktor client setup, DTOs, API definitions.

#### 2. Shared UI (`:shared-ui`)
- **`:shared-ui:design-system`**: Reusable UI components, theming, icons.
- **`:shared-ui:resource`**: Shared resources (Strings, Drawables, Fonts).
- **`:shared-ui:common`**: Navigation utilities, common extensions.
- **`:shared-ui:feature`**: Feature modules using **API/Impl** pattern.

### Feature Module Pattern (API/Impl)
To enforce separation of concerns and build performance, features are split:

*   **`:feature:name:api`**: Contains Navigation Routes (`Route` classes/objects) and public interfaces.
    *   *Dependencies*: `:shared-ui:common`
*   **`:feature:name:impl`**: Contains Screens, ViewModels, DI Modules, and internal logic.
    *   *Dependencies*: `:feature:name:api`, `:shared:domain`, `:shared-ui:design-system`

**Available Features:**
- `home` (Feed, Schedule)
- `search` (Search, Catalog)
- `title` (Details, Episodes)
- `player` (Video Playback)
- `favorite` (User Favorites)
- `preference` (Settings)

## Architecture & Code Style

### Clean Architecture Layers
1.  **Domain**: Source of truth.
    *   Models: Data classes (e.g., `Release`, `Episode`).
    *   Use Cases: `GetReleasesFeedUseCase`. invoke operator overloading.
2.  **Data**: Implementation details.
    *   Repositories: Implement domain interfaces.
    *   Mappers: `Dto.toDomain()`.
3.  **UI**: Presentation.
    *   MVVM pattern.
    *   Unidirectional Data Flow (UDF).

### Naming Conventions

#### Classes & Files
- **ViewModels**: `{Feature}ViewModel` (e.g., `FeedViewModel`).
- **States**: `{Feature}ScreenState` (e.g., `FeedScreenState`).
- **Actions/Events**: `{Feature}ScreenAction` (e.g., `FeedScreenAction`).
- **Use Cases**: `{Verb}{Noun}UseCase` (e.g., `GetReleasesFeedUseCase`).
- **Repositories**: `{Noun}Repository` (e.g., `ReleaseRepository`).

#### Composables
1.  **Stateful Screen (Root)**: Named `{Feature}Screen` or `{Feature}Pane`.
    *   Accepts `ViewModel` (via `koinViewModel()`).
    *   Collects state (`collectAsStateWithLifecycle`).
    *   Handles Navigation events.
    *   Passes data to Content composable.
2.  **Stateless Content**: Named `{Feature}ScreenContent` or matches the root name but with explicit parameters.
    *   Accepts `state`, `onAction` lambda, and specific event callbacks.
    *   No ViewModel usage.
    *   Preview friendly.

**Example:**
```kotlin
// Stateful
@Composable
fun FeedPane(
    viewModel: FeedViewModel = koinViewModel(),
    onNavigateToDetails: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    FeedScreenContent(
        state = state,
        onAction = viewModel::onAction,
        onReleaseClick = onNavigateToDetails
    )
}

// Stateless
@Composable
private fun FeedScreenContent(
    state: FeedScreenState,
    onAction: (FeedScreenAction) -> Unit,
    onReleaseClick: (Int) -> Unit
) { ... }
```

### Dependency Injection (Koin)
- Modules defined in `di` package within implementation modules.
- Use `module { }`.
- Use `viewModelOf(::MyViewModel)`.
- Navigation graphs defined using `navigation<Route> { ... }`.

### Navigation (Navigation 3)
- Type-safe routes defined in `:api` modules.
- Serializable data classes/objects for routes.
- `Navigator` interface for abstracting navigation actions.

## Configuration

### Key Versions (`libs.versions.toml`)
- **Kotlin**: 2.2.21
- **Compose Multiplatform**: 1.10.0-beta02
- **Android Gradle Plugin**: 9.0.0-beta02
- **Koin**: 4.2.0-alpha2
- **Ktor**: 3.3.2
- **Coil**: 3.3.0

### API Configuration
- **Base URL**: `https://aniliberty.top/api/v1/`
- **Asset URL**: `https://aniliberty.top`

## Build & Run

- **Android**: `./gradlew :android-app:assembleDebug`
- **Desktop**: `./gradlew :jvm-app:run`
- **iOS**: Standard Xcode build workflow (relies on `:shared` framework).

## Development Notes

- **Multiplatform Resources**: Use `Res` object from `compose-resources` for strings/images.
- **Image Loading**: Use `PosterImage` component (wraps Coil).
- **Design System**: Strict usage of `AnilibriaTheme` and components in `:shared-ui:design-system`.
- **Error Handling**: `DomainError` sealed class in Domain layer. ViewModels map errors to UI messages.
