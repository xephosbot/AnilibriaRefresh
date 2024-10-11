package com.xbot.anilibriarefresh.ui.feature.title

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.domain.model.TitleModel
import com.xbot.domain.repository.TitleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TitleViewModel @Inject constructor(
    repository: TitleRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val state: StateFlow<TitleModel?> = repository.getTitle(savedStateHandle["titleId"]!!)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )
}