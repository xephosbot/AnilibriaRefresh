# 02. Данные: откуда что берётся

Пример ответа — `reference/release-response.example.json`. Названия полей ниже — как в JSON; в проекте могут быть свои DTO/доменные имена.

## Поля релиза → UI

| Поле | Где на экране | Примечание |
|---|---|---|
| `poster.optimized.preview` | Hero | Fallback: `poster.preview`. Для dynamic color — извлекать палитру из `thumbnail`. |
| `name.main` | Название | Без обрезки |
| `name.english` | Подзаголовок | |
| `name.alternative` | «ещё N названий» | Строка через запятую → `split(",").map(trim).filter(notBlank)` |
| `alias` | «Скопировать ссылку», deeplink | |
| `type.description` | Мета-чип | |
| `season.value` + `year` | Мета-чип «Зима 2026» | Описание брать из **своего** маппинга по `value`, см. странности |
| `age_rating.label` / `.description` / `.is_adult` | Чип возраста, tooltip, гейт 18+ | |
| `episodes_total`, `average_duration_of_episode` | Мета-чип, «Общая длительность» | |
| `is_ongoing`, `is_in_production` | Чип «Завершён», карточка следующей серии, «Анонс» во франшизе | |
| `publish_day.value` | Дата следующей серии | Вычислять ближайшую дату на клиенте |
| `notification` | **Не выводить** | Дублирует карточку следующей серии |
| `description` | «О сюжете» | |
| `genres[]` (`name`, `image`, `total_releases`) | Чипы жанров | |
| `rating.average`, `.votes`, `.distribution` | Вкладка «Оценки» | |
| `shikimori`, `mal` (`rating`, `votes`, `url`) | Плитки внешних оценок | Скрывать плитку, если объект null |
| `added_in_users_favorites` | Счётчик на кнопке избранного | Компактный формат: 25,5K / 1,9M |
| `added_in_*_collection` (5 шт.) | «Кто смотрит» | |
| `is_blocked_by_geo`, `is_blocked_by_copyrights` | Баннер, disabled play | |
| `external_player` | Баннер блокировки, «Подробности» | Протокол-относительный URL (`//kodik…`) → дописать `https:` |
| `fresh_at` | «Последнее обновление» | Относительное время |
| `latest_episode` | Номер «следующей» серии (`ordinal + 1`, округление вниз для спешлов), превью трейлера | Отдельной карточки последней серии **нет** — она есть в списке |
| `latest_episode.youtube_id` | «Трейлер» в подробностях | Уточнить у бэка: это трейлер релиза или видео серии. Если серии — строку «Трейлер» не показывать |
| `latest_episode.rutube_id` | — | Не используется на этом экране |

## Поля серии → элемент списка

| Поле | Использование |
|---|---|
| `ordinal` | «Серия N». Дробное → вывод через запятую + бейдж «Спешл» |
| `name` | Title, если не пустое; иначе title = «Серия N» |
| `name_english` | Не выводить в списке (шум) |
| `preview.optimized.thumbnail` | Превью слева |
| `duration` (сек) | Длительность на превью `mm:ss` / `h:mm:ss` |
| `updated_at` (или дата выхода, если есть отдельное поле) | Нижняя строка «20 июля 2026, 21:06» |
| `opening`, `ending` | Для плеера (пропуск OP/ED). На этом экране не выводить |
| `hls_480/720/1080` | Для плеера. На экране не выводить |
| `sort_order` | Сортировка списка (надёжнее `ordinal`) |

Прогресс просмотра и «просмотрено» — из локальной БД / пользовательских данных, не из этого ответа.

## Производные значения (считать в маппере/доменном слое, не в Composable)

```kotlin
val totalMinutes = episodesTotal * averageDurationOfEpisode            // «≈ 4 ч 48 мин»
val collectionsTotal = watching + planned + watched + postponed + abandoned
val abandonedShare = abandoned.toDouble() / collectionsTotal           // «Бросили всего 0,3%»
val histogramMax = distribution.values.max()                           // нормировка баров
val nextEpisodeOrdinal = floor(latestEpisode.ordinal).toInt() + 1
val nextEpisodeDate = nextDateFor(publishDay, now)                     // ближайший такой день недели
val altNames = alternative.split(',').map { it.trim() }.filter { it.isNotEmpty() }
```

## Франшиза

Репозиторий уже вырезает текущий релиз. Чтобы показать «Часть N из M» и пронумеровать карточки, позицию нужно зафиксировать **до** фильтрации. Рекомендуемая доменная модель:

```kotlin
data class FranchiseSection(
    val title: String,
    val currentPosition: Int,            // 1-based позиция текущего релиза в исходной франшизе
    val totalReleases: Int,              // включая текущий
    val firstYear: Int, val lastYear: Int,
    val totalEpisodes: Int,
    val totalDuration: Duration,
    val items: List<FranchiseItem>,      // без текущего
)

data class FranchiseItem(
    val releaseId: Int,
    val position: Int,                   // исходная позиция: 1, 3, 4…
    val title: String,
    val type: String, val year: Int, val episodesTotal: Int?,
    val poster: String?,
    val isAnnounced: Boolean,            // is_in_production
    val userStatus: CollectionStatus?,   // для бейджа «Просмотрено», если доступно
)
```

Правило отображения: `franchise == null || franchise.items.isEmpty()` → нет чипа, секции и подсказки.
Подсказка «К 1 части» ведёт на `items.first { it.position == 1 }` (если текущий — не первый).

## Известные странности ответа (передать бэку / обработать на клиенте)

1. `season.value = "winter"`, а `season.description = "Осень"`. На клиенте брать название сезона из своего маппинга по `value`.
2. `age_rating.value = "R0_PLUS"` при `label = "16+"`. Ориентироваться на `label`.
3. `shikimori.id` и `mal.id` совпадают в примере — вероятно, артефакт примера; проверить на реальных данных.
4. `external_player` без схемы.
5. В `latest_episode` часто пустой `name` — это нормальный случай, UI к нему готов.
6. `publish_day.value = 1` → «Воскресенье»: уточнить нумерацию (1 = воскресенье, как в JS, а не ISO, где 1 = понедельник). Неправильная нумерация сломает дату следующей серии.
