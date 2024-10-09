package com.xbot.anilibriarefresh.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xbot.api.models.title.Title
import com.xbot.domain.model.TitleModel
import com.xbot.domain.repository.TitleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: TitleRepository
) : ViewModel() {
    val titles: Flow<PagingData<TitleModel>> = repository.getLatestTitles()
        .cachedIn(viewModelScope)
}

sealed interface UiState {
    data object Loading : UiState
    data class Ready(val data: List<Title>) : UiState
}