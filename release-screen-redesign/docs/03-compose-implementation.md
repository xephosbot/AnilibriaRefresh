# 03. Реализация в Compose

Документ — рекомендации, а не жёсткий контракт. Если в проекте уже есть подходящий паттерн или компонент, используй его. Код ниже — эскизы: имена API M3 Expressive сверяй с версией material3, которая реально подключена в проекте.

## 0. Перед стартом

1. Найди текущий экран релиза, его ViewModel/container, маппер DTO → домен и навигационный маршрут.
2. Посмотри `libs.versions.toml`: версии Compose Multiplatform и material3. Выясни, доступны ли expressive-API (`@ExperimentalMaterial3ExpressiveApi`: `MaterialShapes`, `ButtonGroup`, `ToggleButton`, `MotionScheme.expressive()` и т.д.).
   - Если доступны — используй их.
   - Если нет — **не обновляй зависимости без согласования**, реализуй fallback (указаны ниже) и оставь `TODO(expressive)` с пояснением.
3. Изучи UI-кит проекта: сегментированные list items, чипы, кнопки, карточки. Новые компоненты кладутся в UI-кит, только если они переиспользуемые (например, Cookie-аватар), остальное — в фичу.

## 1. Архитектура экрана

Состояние — Orbit MVI, как в остальном проекте.

```kotlin
data class ReleaseDetailsState(
    val release: AsyncField<ReleaseError, ReleaseDetails>,
    val franchise: AsyncField<ReleaseError, FranchiseSection?>,   // null или items.isEmpty() → франшизы нет
    val progress: AsyncField<ReleaseError, WatchProgress?>,       // текущая серия, позиция, просмотренные
    val collectionStatus: CollectionStatus?,
    val isFavorite: Boolean,
    val favoritesCount: Int,
    val notifyNextEpisode: Boolean,
    val selectedTab: ReleaseTab?,        // null до первичного выбора по умолчанию
    val episodesSort: EpisodesSort,
)

enum class ReleaseTab { Episodes, About, Ratings }
```

- Релиз, франшиза и прогресс грузятся параллельно через `AsyncField` — шапка показывается сразу, как пришёл релиз; франшиза и прогресс догружаются и не блокируют экран.
- Список серий — отдельный `Flow<PagingData<EpisodeUi>>` во ViewModel (не в state), источник — SQLDelight (offline-first). Сортировка меняет query.
- UI-локальное состояние (раскрыто ли описание / альтернативные названия) — `rememberSaveable` в Composable, не во ViewModel.

**Интенты**: `Play`, `ToggleFavorite`, `SelectCollectionStatus(status?)`, `ToggleNextEpisodeNotification`, `SelectTab(tab)`, `ToggleEpisodesSort`, `JumpToEpisode(ordinal)`, `JumpToResume`, `OpenEpisode(id)`, `OpenFranchiseItem(id)`, `OpenGenre(id)`, `OpenExternalPlayer`, `OpenTrailer`, `CopyLink`, `CopyTitle(text)`.

**Side effects**: `ShowSnackbar(text)`, `ScrollEpisodesTo(index)`, `Navigate…`, `OpenUrl(url)`, `CopyToClipboard(text)`.

**Вкладка по умолчанию** — решение в контейнере, один раз:

```kotlin
// когда release и progress перешли в Success, а selectedTab == null
val tab = if (progress?.currentEpisode != null) ReleaseTab.Episodes else ReleaseTab.About
reduce { state.copy(selectedTab = tab) }
```

`selectedTab` переживает поворот и process death (savedState контейнера или `rememberSaveable` на уровне экрана — как принято в проекте).

## 2. Каркас: сворачиваемая шапка + липкие вкладки + pager

Это самая важная и самая хрупкая часть. Требования:
- шапка прокручивается вместе с контентом активной вкладки;
- вкладки прилипают под top bar;
- у каждой вкладки свой скролл;
- при переключении вкладки, если шапка уже свёрнута, она остаётся свёрнутой;
- в «Сериях» панель инструментов липнет под вкладками.

### Рекомендуемая схема

```
Box
├── HorizontalPager (fillMaxSize, padding(top = pinnedHeight))   ← z = 0
│     page → LazyColumn(state = listStates[page])
│              item(key="header-spacer") { Spacer(height = collapsibleHeight) }
│              [stickyHeader { EpisodesToolbar }]   // только «Серии»
│              items…
├── Header (graphicsLayer { translationY = headerOffset })     ← z = 1, opaque
│     Hero, чипы, название, подсказки, кнопки, статус, карточка серии
│     PrimaryTabRow (в самом низу Header)
├── TopAppBar (оверлей)                                          ← z = 2
└── ExtendedFab                                                   ← z = 3
```

- `pinnedHeight` = статус-бар + top bar + высота вкладок. Viewport списков начинается **под вкладками**, поэтому `stickyHeader` в «Сериях» сам прилипает прямо под них.
- `collapsibleHeight` = полная высота Header − высота вкладок − (статус-бар + top bar). Измерять через `onSizeChanged` / `SubcomposeLayout`; пока не измерено — показывать шапку без списков (или скелетон).
- Header смещается по скроллу **активной** страницы:

```kotlin
val activeList = listStates[pagerState.currentPage]
val collapse by remember {
    derivedStateOf {
        if (activeList.firstVisibleItemIndex == 0)
            activeList.firstVisibleItemScrollOffset.toFloat().coerceAtMost(collapsiblePx)
        else collapsiblePx
    }
}
// Header: Modifier.graphicsLayer { translationY = -collapse }
```

- **Жесты на шапке.** Header перекрывает списки, поэтому драг по шапке должен скроллить активный список:

```kotlin
Modifier.scrollable(
    orientation = Orientation.Vertical,
    state = rememberScrollableState { delta ->
        activeList.dispatchRawDelta(-delta)
        delta
    },
)
```
  Кнопки и чипы внутри шапки продолжают получать клики.

- **Синхронизация неактивных страниц.** Перед тем как страница станет видимой (тап по вкладке или начало свайпа, `pagerState.isScrollInProgress`), привести её скролл к текущему `collapse`:

```kotlin
suspend fun LazyListState.syncWithHeader(collapse: Float, collapsible: Float) {
    if (collapse < collapsible) {
        scrollToItem(0, collapse.roundToInt())              // шапка частично раскрыта
    } else if (firstVisibleItemIndex == 0 && firstVisibleItemScrollOffset < collapsible) {
        scrollToItem(0, collapsible.roundToInt())           // свёрнута, а вкладка была в начале
    }
    // иначе — оставить сохранённую позицию вкладки
}
```

- `listStates` — по одному `rememberLazyListState()` (saveable) на вкладку, хранятся на уровне экрана.
- Вкладки: `PrimaryTabRow(selectedTabIndex)` + `TabRowDefaults.PrimaryIndicator(Modifier.tabIndicatorOffset(index, matchContentSize = true))`. Тап → `syncWithHeader` → `pagerState.animateScrollToPage`. `pagerState.settledPage` → интент `SelectTab`.
- Бейдж количества серий на вкладке «Серии».

### Производные от `collapse`

- Top bar становится непрозрачным, когда `collapse >= titleBottomPx` (позиция низа названия внутри Header, `onGloballyPositioned`).
- FAB виден, когда `collapse >= playButtonBottomPx` и релиз не заблокирован.

Альтернатива (если схема выше не приживётся): один `NestedScrollConnection` на Box, который в `onPreScroll` сначала сворачивает шапку, а в `onPostScroll` разворачивает. Но тогда сложнее сохранять независимый скролл вкладок — предпочтительна схема с распейсером.

## 3. Компоненты шапки

| Блок | Реализация | Fallback без expressive-API |
|---|---|---|
| Hero | Coil `AsyncImage`, `ContentScale.Crop`, высота ~440dp; сверху `Brush.verticalGradient` в `surface` | — |
| Top bar | Свой Row поверх, кнопки `FilledTonalIconButton` с полупрозрачным контейнером | — |
| Мета-чипы | `FlowRow` чипов из UI-кита; «Часть N из M» — `AssistChip` tonal primary | — |
| Название | Headline UI-кита, без `maxLines`; для длинных — `BasicText(autoSize = …)`, если доступно | уменьшение `fontSize` по длине строки |
| Альт. названия | `TextButton` + `AnimatedVisibility { FlowRow(чипы) }` | — |
| Play | Кастомная `Surface` с `shape` = анимированный `RoundedCornerShape(radius)`, radius 34dp → 18dp при нажатии (`interactionSource.collectIsPressedAsState()` + `animateDpAsState(spring)`); фон-прогресс — `drawBehind` | — |
| Иконка play | `MaterialShapes.Cookie9Sided.toShape()` | `RoundedPolygon.star(numVerticesPerRadius = 9, innerRadius ≈ 0.86, rounding)` из `androidx.graphics:graphics-shapes` → свой `Shape` |
| Избранное | `IconToggleButton`/`ToggleButton`, shape morph 20dp → круг, `FILL` иконки | анимированный радиус |
| Статус | `ButtonGroup` + `ToggleButton` с connected-формами (`ButtonGroupDefaults.connectedLeading/Middle/TrailingButtonShapes`), в `horizontalScroll` | Row из `Surface` с асимметричными `RoundedCornerShape` и морфом выбранного в pill |
| Карточка серии | `Surface(tertiaryContainer, RoundedCornerShape(28.dp))`, «календарик» в Cookie7Sided, колокольчик — toggle | — |
| Баннер блокировки | `Surface(errorContainer)` | — |
| Подсказка о предыдущей части | `Surface(surfaceContainerHigh)` + `FilledButton` | — |

Анимации: `MaterialTheme.motionScheme` (expressive spatial spring) для морфов и появления FAB; уважать системное «уменьшение движения».

## 4. Вкладка «Серии»

```kotlin
LazyColumn(state = listStates[Episodes], contentPadding = PaddingValues(bottom = 120.dp)) {
    item(key = "header-spacer") { Spacer(Modifier.height(collapsibleDp)) }
    stickyHeader(key = "toolbar") { EpisodesToolbar(…) }
    items(count = episodes.itemCount, key = episodes.itemKey { it.id }) { i ->
        EpisodeListItem(episodes[i], position = segmentPosition(i, episodes.itemCount))
    }
}
```

- `EpisodeListItem` — сегментированный list item из UI-кита. `segmentPosition` → First / Middle / Last / Single для радиусов.
- Пустое `name` → title = «Серия N»; иначе label «Серия N» + title = name. Форматирование `ordinal` и бейдж «Спешл» — в маппере (`EpisodeUi`), не в Composable.
- Текущая серия — `secondaryContainer`, вместо даты «Остановились на mm:ss».
- **Прыжки** («Где остановился», «№ серии», чипы диапазонов): контейнер вычисляет индекс серии в текущей сортировке (запрос в SQLDelight: количество серий «до» нужной) и шлёт `ScrollEpisodesTo(index)`. Экран использует существующий механизм jump scrolling (`PagingScrollHandler`) с поправкой на служебные элементы (spacer + toolbar = +2). После прыжка — короткая подсветка элемента.
- Чипы диапазонов показываются при `count > 100`. Активный чип — `derivedStateOf { (firstVisibleItemIndex - 2) / 100 }`, ряд чипов автоскроллится к активному.
- После прыжка шапка должна быть свёрнута (прыжок всегда уводит список дальше `collapsible`).

## 5. Вкладка «О релизе»

Один `LazyColumn` со spacer'ом, секции — отдельные `item`:
1. Описание: `Text(maxLines = if (expanded) Int.MAX_VALUE else 4)` + затухание `drawWithContent` с градиентом + `animateContentSize()`.
2. Франшиза (если есть): `HorizontalMultiBrowseCarousel(state = rememberCarouselState { items.size }, preferredItemWidth = 172.dp, itemSpacing = 8.dp)`, у карточки `Modifier.maskClip(MaterialTheme.shapes.extraLarge)`. Номер карточки — `item.position` (исходный, с пропуском текущего). Сводка над каруселью — из `FranchiseSection`.
   Если карусели нет в доступной версии material3 — `LazyRow` + `snapFlingBehavior`, без маскирования.
3. Жанры — `FlowRow` чипов с `AsyncImage` 36dp в круге.
4. Команда — `LazyRow`, аватар-заглушка: инициал в Cookie-формах разных вариантов (6/8/10/12 граней), цвета чередуются из контейнерных ролей темы.
5. Подробности — сегментированный список из UI-кита.

## 6. Вкладка «Оценки»

- Гистограмма — Column из 10 строк, бар — `Box` с `fillMaxWidth(fraction)`, `fraction` анимируется от 0 при первом показе вкладки (`LaunchedEffect(pagerState.settledPage == Ratings)` + флаг «уже анимировали»).
- Крупная оценка — в `MaterialShapes.Cookie9Sided` (или fallback).
- Shikimori/MAL — две плитки в `Row`, `weight(1f)`, тап → `OpenUrl`.
- «Кто смотрит» — stacked bar: `Row` из `Box(Modifier.weight(count.toFloat()))` с зазором 2dp, цвета — фиксированный набор ролей темы; легенда — сетка 2 колонки.

## 7. FAB и прочее

- `ExtendedFloatingActionButton` в `AnimatedVisibility(enter = scaleIn + slideInVertically с пружиной)`.
- Нижняя навигация приложения скрывается на этом маршруте (через существующий механизм навигации).
- Snackbar — через side effect и `SnackbarHostState` экрана.

## 8. Предлагаемая раскладка файлов

Адаптировать к модульной структуре проекта:

```
feature/release/
  presentation/
    ReleaseDetailsContainer.kt      // Orbit
    ReleaseDetailsState.kt
    model/EpisodeUi.kt, FranchiseSection.kt, …
    mapper/ReleaseDetailsUiMapper.kt
  ui/
    ReleaseDetailsScreen.kt         // сбор state/side effects
    ReleaseDetailsLayout.kt         // каркас из раздела 2
    header/ReleaseHero.kt, ReleaseMetaChips.kt, ReleaseTitle.kt, PrimaryActions.kt,
           CollectionStatusGroup.kt, NextEpisodeCard.kt, BlockedBanner.kt, PrequelHint.kt
    episodes/EpisodesPage.kt, EpisodesToolbar.kt, EpisodeListItem.kt
    about/AboutPage.kt, FranchiseCarousel.kt, GenreChips.kt, TeamRow.kt, ReleaseDetailsList.kt
    ratings/RatingsPage.kt, RatingHistogram.kt, CommunityStats.kt
    preview/ReleaseDetailsPreviewData.kt
```

## 9. Порядок работы

1. Доменные модели и маппер + юнит-тесты (раздел «Тесты»).
2. Каркас `ReleaseDetailsLayout` с заглушками страниц — проверить скролл, липкость, переключение вкладок, свайп, жесты по шапке. **Не идти дальше, пока каркас не работает стабильно.**
3. Компоненты шапки.
4. Вкладка «Серии» с Paging и прыжками.
5. «О релизе», затем «Оценки».
6. FAB, top bar, анимации, состояния загрузки/ошибки/оффлайна, гейт 18+.
7. Превью для всех состояний из таблицы в `01-ux-spec.md`.

## 10. Тесты

Юнит (commonTest):
- маппинг сезона по `value` (игнорируя `description`);
- `alternative` → список имён (пустые элементы, пробелы);
- форматирование `ordinal` (целое, `12.5` → «12,5» + спешл);
- title серии при пустом / непустом `name`;
- ближайшая дата по `publish_day` (сегодня этот день / завтра / через неделю, границы суток);
- `abandonedShare`, нормировка гистограммы (все нули → без деления на ноль);
- правило франшизы: `null`/пустой список → `showFranchise = false`; позиция и total сохраняются после фильтрации;
- выбор вкладки по умолчанию;
- условие подсказки о предыдущей части.

UI (если в проекте есть compose-тесты): переключение вкладок сохраняет скролл; FAB появляется после прокрутки; play disabled при блокировке.
