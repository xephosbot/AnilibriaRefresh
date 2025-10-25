package com.xbot.search.navigation

import com.xbot.common.navigation.NavKey
import com.xbot.common.navigation.Navigator
import com.xbot.domain.models.filters.CatalogFilters
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data object SearchRoute : NavKey

@Serializable
data class SearchFiltersRoute(
    @Transient
    val initialFilters: CatalogFilters? = null
) : NavKey

fun Navigator<*>.navigateToSearch() {
    navigate(SearchRoute)
}