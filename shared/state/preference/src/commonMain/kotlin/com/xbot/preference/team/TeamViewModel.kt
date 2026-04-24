package com.xbot.preference.team

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.koin.core.annotation.KoinViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@KoinViewModel
class TeamViewModel(
    private val savedStateHandle: SavedStateHandle? = null,
) : ViewModel(), ContainerHost<Unit, Nothing> {
    override val container: Container<Unit, Nothing> = container(Unit)
}
