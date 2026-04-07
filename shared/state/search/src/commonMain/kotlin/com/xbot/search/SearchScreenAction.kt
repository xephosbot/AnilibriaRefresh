package com.xbot.search

import com.xbot.domain.models.Genre
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType

sealed interface SearchScreenAction {
    data class QueryChanged(val query: String) : SearchScreenAction
    data class ToggleGenre(val genre: Genre) : SearchScreenAction
    data class TogglePublishStatus(val publishStatus: PublishStatus) : SearchScreenAction
    data class ToggleProductionStatus(val productionStatus: ProductionStatus) : SearchScreenAction
    data class ToggleReleaseType(val releaseType: ReleaseType) : SearchScreenAction
    data class ToggleSeason(val season: Season) : SearchScreenAction
    data class UpdateSortingType(val sortingType: SortingType) : SearchScreenAction
    data class UpdateYearsRange(val years: IntRange) : SearchScreenAction
    data class ToggleAgeRating(val ageRating: AgeRating) : SearchScreenAction
    data class ShowErrorMessage(
        val error: Throwable,
        val onRetry: () -> Unit,
    ) : SearchScreenAction
    data object Refresh : SearchScreenAction
}
