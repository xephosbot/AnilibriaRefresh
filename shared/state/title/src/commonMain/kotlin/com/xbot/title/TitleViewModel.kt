package com.xbot.title

import androidx.lifecycle.ViewModel
import com.xbot.common.asyncLoad
import com.xbot.common.getOrNull
import com.xbot.domain.models.Release
import com.xbot.domain.usecase.GetFranchiseReleasesUseCase
import com.xbot.domain.usecase.GetReleaseUseCase
import kotlinx.coroutines.Job
import org.koin.core.annotation.KoinViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@KoinViewModel
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
            onError = { error -> showError(error) { loadDetails() } },
            reducer = {
                copy(
                    initialRelease = it.getOrNull()?.release ?: initialRelease,
                    details = it
                )
            }
        )
    }

    private fun loadRelatedReleases(): Job = intent {
        asyncLoad(
            request = { getFranchiseReleases(aliasOrId) },
            onError = { error -> showError(error) { loadRelatedReleases() } },
            reducer = {
                copy(relatedReleases = it)
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
