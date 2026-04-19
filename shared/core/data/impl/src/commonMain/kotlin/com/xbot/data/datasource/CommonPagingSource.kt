package com.xbot.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import arrow.core.Either
import com.xbot.domain.models.DomainError
import kotlin.math.max

/**
 * Paging3 bridge that speaks Arrow [Either] internally and only crosses into Paging's
 * `Throwable`-based contract at the very last step.
 *
 * [loadPage] returns `Either<DomainError, PaginatedResponse<T>>` so repositories never
 * have to `throw` to signal failure; we [fold][Either.fold] at the [LoadResult]
 * boundary. Because [DomainError] extends [Exception], `LoadResult.Error(domainError)`
 * satisfies Paging's contract without an artificial wrapper — UI readers (e.g.,
 * `(loadState.refresh as? LoadState.Error)?.error`) get the typed error directly and
 * can downcast for tailored messaging.
 *
 * Cancellation: [loadPage] is a suspending Arrow-returning function — its producer
 * (`ResilientHttpRequester.singleAttempt`) already rethrows `CancellationException`,
 * so we don't need a defensive `try/catch` here.
 */
internal class CommonPagingSource<T : Any>(
    private val loadPage: suspend (page: Int, limit: Int) -> Either<DomainError, PaginatedResponse<T>>,
) : PagingSource<Int, T>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val pageIndex = params.key ?: FIRST_PAGE_INDEX
        val loadSize = params.loadSize
        return loadPage(pageIndex + 1, loadSize).fold(
            ifLeft = { error -> LoadResult.Error(error) },
            ifRight = { page ->
                val newCount = page.items.size
                val total = page.total
                val itemsBefore = pageIndex * NETWORK_PAGE_SIZE
                val itemsAfter = max(total - (itemsBefore + newCount), 0)

                val prevKey = if (pageIndex == FIRST_PAGE_INDEX) null else pageIndex - 1
                val nextKey = if (itemsAfter == 0) null else pageIndex + (loadSize / NETWORK_PAGE_SIZE)

                LoadResult.Page(
                    data = page.items,
                    prevKey = prevKey,
                    nextKey = nextKey,
                    itemsBefore = itemsBefore,
                    itemsAfter = itemsAfter,
                )
            },
        )
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val pageIndex = anchorPosition / NETWORK_PAGE_SIZE
        return pageIndex
    }

    override val jumpingSupported: Boolean = true

    data class PaginatedResponse<T>(
        val items: List<T>,
        val total: Int,
    )

    companion object {
        const val NETWORK_PAGE_SIZE = 20
        private const val FIRST_PAGE_INDEX = 0
    }
}
