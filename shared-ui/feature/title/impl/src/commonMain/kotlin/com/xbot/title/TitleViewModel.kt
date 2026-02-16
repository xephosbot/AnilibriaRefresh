package com.xbot.title

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.designsystem.utils.MessageAction
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.designsystem.utils.StringResource
import com.xbot.designsystem.utils.localizedMessage
import com.xbot.domain.models.ReleaseDetailsExtended
import com.xbot.domain.usecase.GetReleaseDetailsUseCase
import com.xbot.resources.Res
import com.xbot.resources.button_retry
import com.xbot.title.navigation.TitleRoute
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
internal class TitleViewModel(
    private val getReleaseDetailUseCase: GetReleaseDetailsUseCase,
    private val snackbarManager: SnackbarManager,
    private val titleRoute: TitleRoute,
) : ViewModel() {

    private val refreshTrigger = MutableStateFlow(0)

    private val titleDetails = refreshTrigger.flatMapLatest {
        getReleaseDetailUseCase(titleRoute.aliasOrId).catch { showErrorMessage(it) { refresh() } }
    }

    val state: StateFlow<TitleScreenState> = titleDetails
        .map { releaseDetails ->
            val release = releaseDetails.details.release ?: titleRoute.release
            val newDetails = releaseDetails.details.copy(release = release)
            TitleScreenState(release = releaseDetails.copy(details = newDetails))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = TitleScreenState(ReleaseDetailsExtended.create(titleRoute.release))
        )

    fun onAction(action: TitleScreenAction) {
        when (action) {
            is TitleScreenAction.ShowErrorMessage -> showErrorMessage(action.error, action.onConfirmAction)
            is TitleScreenAction.Refresh -> refresh()
        }
    }

    private fun refresh() {
        refreshTrigger.update { it + 1 }
    }

    private fun showErrorMessage(error: Throwable, onConfirmAction: () -> Unit) {
        snackbarManager.showMessage(
            title = error.localizedMessage(),
            action = MessageAction(
                title = StringResource.Text(Res.string.button_retry),
                action = onConfirmAction,
            ),
        )
    }
}

@Stable
internal data class TitleScreenState(
    val release: ReleaseDetailsExtended,
)

@Stable
internal sealed interface TitleScreenAction {
    @Stable
    data class ShowErrorMessage(
        val error: Throwable,
        val onConfirmAction: () -> Unit,
    ) : TitleScreenAction

    @Stable
    data object Refresh : TitleScreenAction
}
