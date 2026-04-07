package com.xbot.domain.usecase

import androidx.paging.Pager
import com.xbot.domain.models.Release
import com.xbot.domain.models.filters.CatalogQuery

fun interface GetCatalogReleasesPagerUseCase {
    operator fun invoke(
        search: String?,
        filters: CatalogQuery?
    ): Pager<Int, Release>
}
