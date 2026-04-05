package com.xbot.title

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetailsExtended
import com.xbot.domain.usecase.GetReleaseDetailsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
class TitleViewModel(
    private val getReleaseDetailUseCase: GetReleaseDetailsUseCase,
    private val aliasOrId: String,
    private val initialRelease: Release? = null,
) : ViewModel() {

    private val refreshTrigger = MutableStateFlow(0)

    private val _sideEffects = Channel<TitleScreenSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    private val titleDetails = refreshTrigger.flatMapLatest {
        getReleaseDetailUseCase(aliasOrId).catch { _sideEffects.trySend(TitleScreenSideEffect.ShowError(it) { refresh() }) }
    }

    val state: StateFlow<TitleScreenState> = titleDetails
        .map { releaseDetails ->
            val release = releaseDetails.details.release ?: initialRelease
            val newDetails = releaseDetails.details.copy(release = release)
            TitleScreenState(release = releaseDetails.copy(details = newDetails))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = TitleScreenState(ReleaseDetailsExtended.create(initialRelease))
        )

    fun onAction(action: TitleScreenAction) {
        when (action) {
            is TitleScreenAction.Refresh -> refresh()
        }
    }

    private fun refresh() {
        refreshTrigger.update { it + 1 }
    }
}

data class TitleScreenState(
    val release: ReleaseDetailsExtended,
)

sealed interface TitleScreenAction {
    data object Refresh : TitleScreenAction
}

sealed interface TitleScreenSideEffect {
    data class ShowError(val error: Throwable, val retryAction: () -> Unit) : TitleScreenSideEffect
}
