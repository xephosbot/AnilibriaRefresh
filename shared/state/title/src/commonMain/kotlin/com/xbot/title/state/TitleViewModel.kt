package com.xbot.title.state

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetailsExtended
import com.xbot.domain.usecase.GetReleaseDetailsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import org.koin.core.annotation.KoinViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container

@OptIn(OrbitExperimental::class)
@KoinViewModel
class TitleViewModel(
    private val releaseAliasOrId: String,
    private val initialRelease: Release? = null,
    private val getReleaseDetailUseCase: GetReleaseDetailsUseCase,
) : ViewModel(), ContainerHost<TitleScreenState, TitleScreenSideEffect> {

    override val container: Container<TitleScreenState, TitleScreenSideEffect> = container(
        initialState = TitleScreenState(ReleaseDetailsExtended.create(initialRelease)),
    ) {
        startLoadData()
    }

    fun onAction(action: TitleScreenAction) {
        when (action) {
            is TitleScreenAction.Refresh -> refresh()
            is TitleScreenAction.ShowErrorMessage -> showErrorMessage(action.error, action.onConfirmAction)
        }
    }

    private var loadDataJob: Job? = null

    private fun startLoadData() {
        loadDataJob?.cancel()
        loadDataJob = intent {
            getReleaseDetailUseCase(aliasOrId = releaseAliasOrId)
                .catch { error ->
                    postSideEffect(TitleScreenSideEffect.ShowErrorMessage(error) {
                        onAction(
                            TitleScreenAction.Refresh
                        )
                    })
                }
                .collect { titleDetails ->
                    val release = titleDetails.details.release ?: initialRelease
                    val newDetails = titleDetails.details.copy(release = release)

                    reduce {
                        state.copy(
                            release = titleDetails.copy(details = newDetails)
                        )
                    }
                }
        }
    }

    private fun refresh(): Job = intent {
        startLoadData()
    }

    private fun showErrorMessage(error: Throwable, onRetry: () -> Unit): Job = intent {
        postSideEffect(TitleScreenSideEffect.ShowErrorMessage(error, onRetry))
    }
}

@Stable
data class TitleScreenState(
    val release: ReleaseDetailsExtended,
)

@Stable
sealed interface TitleScreenSideEffect {
    @Stable
    data class ShowErrorMessage(
        val error: Throwable,
        val onRetry: () -> Unit
    ) : TitleScreenSideEffect
}

@Stable
sealed interface TitleScreenAction {
    @Stable
    data object Refresh : TitleScreenAction

    @Stable
    data class ShowErrorMessage(
        val error: Throwable,
        val onConfirmAction: () -> Unit,
    ) : TitleScreenAction
}
