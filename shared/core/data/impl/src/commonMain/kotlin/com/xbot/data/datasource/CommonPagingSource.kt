package com.xbot.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import arrow.core.Either
import com.xbot.domain.models.DomainError
import kotlinx.coroutines.CancellationException
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
 * Cancellation & defensive catch: the [loadPage] producer
 * (`ResilientHttpRequester.singleAttempt`) already rethrows `CancellationException`,
 * but the lambda runs a downstream DTO→domain mapping step (`.map { dto.toDomain() }`)
 * and `Either.map` does NOT catch throws. A runtime exception inside a mapper (NPE on
 * a nullable field, bad enum parse, etc.) would otherwise escape this `fold`. Wrap
 * the body so non-cancellation throws become `LoadResult.Error`, and rethrow
 * `CancellationException` explicitly so structured concurrency still works.
 *
 * **Page-size coupling contract:** [pageSize] MUST equal the `PagingConfig.pageSize`
 * of the enclosing [androidx.paging.Pager]. The key arithmetic relies on the
 * invariant that `loadSize` is always a multiple of [pageSize] — if the `Pager`
 * uses a different pageSize, `itemsBefore` (`pageIndex * pageSize`) and the server
 * offset `(page-1) * loadSize` drift apart and you get gaps or duplicate items.
 * The `init` block asserts `pageSize > 0`; call-site alignment is documented, not
 * statically enforceable.
 */
internal class CommonPagingSource<T : Any>(
    private val pageSize: Int = DEFAULT_PAGE_SIZE,
    private val loadPage: suspend (page: Int, limit: Int) -> Either<DomainError, PaginatedResponse<T>>,
) : PagingSource<Int, T>() {

    init {
        require(pageSize > 0) { "pageSize must be > 0, got $pageSize" }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val pageIndex = params.key ?: FIRST_PAGE_INDEX
        val loadSize = params.loadSize
        return try {
            loadPage(pageIndex + 1, loadSize).fold(
                ifLeft = { error -> LoadResult.Error(error) },
                ifRight = { page ->
                    val newCount = page.items.size
                    val total = page.total
                    val itemsBefore = pageIndex * pageSize
                    val itemsAfter = max(total - (itemsBefore + newCount), 0)

                    val prevKey = if (pageIndex == FIRST_PAGE_INDEX) null else pageIndex - 1
                    // Guard against a Pager misconfigured with `loadSize < pageSize`
                    // (would divide to 0 and loop on the same page). With the contract
                    // honoured this just evaluates to `loadSize / pageSize`.
                    val nextKey = if (itemsAfter == 0) {
                        null
                    } else {
                        pageIndex + (loadSize / pageSize).coerceAtLeast(1)
                    }

                    LoadResult.Page(
                        data = page.items,
                        prevKey = prevKey,
                        nextKey = nextKey,
                        itemsBefore = itemsBefore,
                        itemsAfter = itemsAfter,
                    )
                },
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        return anchorPosition / pageSize
    }

    override val jumpingSupported: Boolean = true

    data class PaginatedResponse<T>(
        val items: List<T>,
        val total: Int,
    )

    companion object {
        /** Default page size used when callers don't override. Matches the API's canonical page window. */
        const val DEFAULT_PAGE_SIZE = 20
        private const val FIRST_PAGE_INDEX = 0
    }
}
