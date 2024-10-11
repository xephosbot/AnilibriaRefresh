package com.xbot.anilibriarefresh.ui.feature.title

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
    repository: TitleRepository
) : ViewModel() {
    val state: StateFlow<TitleModel?> = repository.getTitle(9000)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )
}