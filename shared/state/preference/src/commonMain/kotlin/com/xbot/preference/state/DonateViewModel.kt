package com.xbot.preference.state

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

import org.koin.core.annotation.KoinViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container

@OptIn(OrbitExperimental::class)
@KoinViewModel
class DonateViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<Unit, Nothing> {
    override val container: Container<Unit, Nothing> = container(Unit)
}
