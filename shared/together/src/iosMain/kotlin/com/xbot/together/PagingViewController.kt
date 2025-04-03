package com.xbot.together

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.PagingDataEvent
import androidx.paging.PagingDataPresenter
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

@Suppress("unused")
class PagingViewController <T: Any> {
    private val mainDispatcher = Dispatchers.Main
    private val _itemsCount = MutableStateFlow(0)

    private val pagingDataPresenter = object : PagingDataPresenter<T>(
        mainContext = mainDispatcher,
        cachedPagingData = null
    ) {
        override suspend fun presentPagingDataEvent(event: PagingDataEvent<T>) {
            updateItemsCount()
        }
    }

    @NativeCoroutines
    val itemsCount: StateFlow<Int> = _itemsCount.asStateFlow()

    fun retry() {
        pagingDataPresenter.retry()
    }

    fun refresh() {
        pagingDataPresenter.refresh()
    }

    fun fetchAt(index: Int) {
        pagingDataPresenter[index]
    }

    fun getItem(index: Int): T? {
        return pagingDataPresenter.snapshot()[index]
    }

    @NativeCoroutines
    val loadState: Flow<CombinedLoadStates> = pagingDataPresenter.loadStateFlow.map {
        it ?: CombinedLoadStates(
            refresh = InitialLoadStates.refresh,
            prepend = InitialLoadStates.prepend,
            append = InitialLoadStates.append,
            source = InitialLoadStates
        )
    }

    suspend fun submitData(pagingData: PagingData<T>) {
        pagingDataPresenter.collectFrom(pagingData)
    }

    private fun updateItemsCount() {
        _itemsCount.value = pagingDataPresenter.snapshot().size
    }

    companion object {
        private val InitialLoadStates = LoadStates(
            LoadState.Loading,
            LoadState.NotLoading(false),
            LoadState.NotLoading(false)
        )
    }
}