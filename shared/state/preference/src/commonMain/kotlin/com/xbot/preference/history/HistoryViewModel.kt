package com.xbot.preference.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.koin.core.annotation.KoinViewModel
import org.orbitmvi.orbit.OrbitContainer
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer

@KoinViewModel
class HistoryViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), OrbitContainerHost<Unit, Unit, Nothing> {
    override val container: OrbitContainer<Unit, Unit, Nothing> = orbitContainer(Unit)
}
