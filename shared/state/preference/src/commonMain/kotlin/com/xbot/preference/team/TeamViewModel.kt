package com.xbot.preference.team

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class TeamViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<Unit, Nothing> {
    override val container: Container<Unit, Nothing> = container(Unit)
}
