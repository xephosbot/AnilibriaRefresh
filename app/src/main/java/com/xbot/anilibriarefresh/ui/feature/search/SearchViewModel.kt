package com.xbot.anilibriarefresh.ui.feature.search

import androidx.lifecycle.ViewModel
import com.xbot.domain.repository.TitleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    repository: TitleRepository
) : ViewModel() {

}