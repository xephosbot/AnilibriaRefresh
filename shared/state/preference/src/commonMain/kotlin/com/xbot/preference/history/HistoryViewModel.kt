package com.xbot.preference.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class HistoryViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<Unit, Nothing> {
    override val container: Container<Unit, Nothing> = container(Unit)
}
