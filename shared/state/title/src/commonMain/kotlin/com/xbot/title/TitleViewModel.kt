package com.xbot.title

import androidx.lifecycle.ViewModel
import com.xbot.common.asyncLoad
import com.xbot.common.getOrNull
import com.xbot.domain.models.Release
import com.xbot.domain.usecase.GetFranchiseReleasesUseCase
import com.xbot.domain.usecase.GetReleaseUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import org.koin.core.annotation.Provided
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container

@OptIn(OrbitExperimental::class)
@KoinViewModel
class TitleViewModel(
    @Provided private val aliasOrId: String,
    @Provided private val initialRelease: Release? = null,
    private val getRelease: GetReleaseUseCase,
    private val getFranchiseReleases: GetFranchiseReleasesUseCase,
) : ViewModel(), ContainerHost<TitleScreenState, TitleScreenSideEffect> {

    override val container: Container<TitleScreenState, TitleScreenSideEffect> = container(
        initialState = TitleScreenState(initialRelease = initialRelease)
    ) {
        coroutineScope {
            launch { loadDetails() }
            launch { loadRelatedReleases() }
        }
    }

    private suspend fun loadDetails() = subIntent {
        asyncLoad(
            request = { getRelease(aliasOrId) },
            onError = { error -> showError(error) { refresh() } },
            reducer = {
                copy(
                    initialRelease = it.getOrNull()?.release ?: initialRelease,
                    details = it
                )
            }
        )
    }

    private suspend fun loadRelatedReleases() = subIntent {
        asyncLoad(
            request = { getFranchiseReleases(aliasOrId) },
            onError = { error -> showError(error) { refresh() } },
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

    private fun refresh(): Job = intent {
        coroutineScope {
            launch { loadDetails() }
            launch { loadRelatedReleases() }
        }
    }

    private fun showError(error: Throwable, retryAction: () -> Unit): Job = intent {
        postSideEffect(TitleScreenSideEffect.ShowErrorMessage(error, retryAction))
    }
}
