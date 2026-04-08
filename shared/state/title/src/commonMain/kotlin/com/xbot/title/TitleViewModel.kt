package com.xbot.title

import androidx.lifecycle.ViewModel
import com.xbot.common.asyncLoad
import com.xbot.common.consumeError
import com.xbot.common.getOrElse
import com.xbot.domain.models.Release
import com.xbot.domain.usecase.GetFranchiseReleasesUseCase
import com.xbot.domain.usecase.GetReleaseUseCase
import kotlinx.coroutines.Job
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class TitleViewModel(
    private val aliasOrId: String,
    private val initialRelease: Release? = null,
    private val getRelease: GetReleaseUseCase,
    private val getFranchiseReleases: GetFranchiseReleasesUseCase,
) : ViewModel(), ContainerHost<TitleScreenState, TitleScreenSideEffect> {

    override val container: Container<TitleScreenState, TitleScreenSideEffect> = container(
        initialState = TitleScreenState(initialRelease = initialRelease)
    ) {
        loadDetails()
        loadRelatedReleases()
    }

    private fun loadDetails(): Job = intent {
        asyncLoad(
            request = { getRelease(aliasOrId) },
            reducer = {
                val result = it.consumeError { error ->
                    showError(error) { loadDetails() }
                }
                copy(
                    initialRelease = result.getOrElse { null }?.release ?: initialRelease,
                    details = result
                )
            }
        )
    }

    private fun loadRelatedReleases(): Job = intent {
        asyncLoad(
            request = { getFranchiseReleases(aliasOrId) },
            reducer = {
                val result = it.consumeError { error ->
                    showError(error) { loadRelatedReleases() }
                }
                copy(relatedReleases = result)
            }
        )
    }

    fun onAction(action: TitleScreenAction) {
        when (action) {
            is TitleScreenAction.Refresh -> refresh()
        }
    }

    private fun refresh() {
        loadDetails()
        loadRelatedReleases()
    }

    private fun showError(error: Throwable, retryAction: () -> Unit) = intent {
        postSideEffect(TitleScreenSideEffect.ShowErrorMessage(error, retryAction))
    }
}
