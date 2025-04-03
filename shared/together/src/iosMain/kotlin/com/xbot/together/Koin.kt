package com.xbot.together

import com.xbot.domain.usecase.GetCatalogFilters
import com.xbot.domain.usecase.GetReleasesFeed
import com.xbot.domain.usecase.GetReleasesPager
import org.koin.core.Koin

val Koin.getCatalogFilters: GetCatalogFilters
    get() = get()

val Koin.getReleasesFeed: GetReleasesFeed
    get() = get()

val Koin.getReleasesPager: GetReleasesPager
    get() = get()