package com.xbot.designsystem.components

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity

@Suppress("UnusedReceiverParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarDefaults.pinnedScrollBehaviorFixed(
    state: TopAppBarState = rememberTopAppBarState(),
    canScroll: () -> Boolean = { true }
): TopAppBarScrollBehavior = PinnedScrollBehavior(state, canScroll)

@OptIn(ExperimentalMaterial3Api::class)
private class PinnedScrollBehavior(
    override val state: TopAppBarState,
    val canScroll: () -> Boolean = { true }
) : TopAppBarScrollBehavior {
    override val isPinned: Boolean = true
    override val snapAnimationSpec: AnimationSpec<Float>? = null
    override val flingAnimationSpec: DecayAnimationSpec<Float>? = null
    override var nestedScrollConnection =
        object : NestedScrollConnection {
            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                if (!canScroll()) return Offset.Zero
                state.contentOffset += consumed.y
                return Offset.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                if (available.y > 0f) {
                    // Reset the total content offset to zero when scrolling all the way down.
                    // This will eliminate some float precision inaccuracies.
                    state.contentOffset = 0f
                }
                return super.onPostFling(consumed, available)
            }
        }
}
