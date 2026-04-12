package com.xbot.title

import androidx.lifecycle.ViewModel
import com.xbot.common.LoadableField
import com.xbot.common.asyncLoad
import com.xbot.common.firstError
import com.xbot.common.getOrNull
import com.xbot.common.refreshAll
import com.xbot.common.retryErrors
import com.xbot.domain.models.Release
import com.xbot.domain.usecase.GetFranchiseReleasesUseCase
import com.xbot.domain.usecase.GetReleaseUseCase
import kotlinx.coroutines.Job
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.Syntax
import org.orbitmvi.orbit.viewmodel.container

@OptIn(OrbitExperimental::class)
class TitleViewModel(
    private val aliasOrId: String,
    private val initialRelease: Release? = null,
    private val getRelease: GetReleaseUseCase,
    private val getFranchiseReleases: GetFranchiseReleasesUseCase,
) : ViewModel(), ContainerHost<TitleScreenState, TitleScreenSideEffect> {

    private val loadableFields: List<LoadableField<TitleScreenState>> = listOf(
        LoadableField(selector = { it.details }, load = ::loadDetails),
        LoadableField(selector = { it.relatedReleases }, load = ::loadRelatedReleases),
    )

    private val onLoadError: suspend Syntax<TitleScreenState, TitleScreenSideEffect>.() -> Unit = {
        loadableFields.firstError(state)?.let { error ->
            postSideEffect(TitleScreenSideEffect.ShowLoadError(error = error, onRetry = { retry() }))
        }
    }

    override val container: Container<TitleScreenState, TitleScreenSideEffect> = container(
        initialState = TitleScreenState(initialRelease = initialRelease)
    ) {
        refreshAll(loadableFields, onBatchFailure = onLoadError)
    }

    private fun loadDetails(): Job = intent {
        asyncLoad(
            request = { getRelease(aliasOrId) },
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
            reducer = { copy(relatedReleases = it) }
        )
    }

    fun onAction(action: TitleScreenAction) {
        when (action) {
            is TitleScreenAction.Refresh -> refresh()
        }
    }

    private fun retry(): Job = intent {
        retryErrors(loadableFields, onBatchFailure = onLoadError)
    }

    private fun refresh(): Job = intent {
        refreshAll(loadableFields, onBatchFailure = onLoadError)
    }
}
