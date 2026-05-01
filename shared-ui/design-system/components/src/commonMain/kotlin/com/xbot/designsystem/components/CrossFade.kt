package com.xbot.designsystem.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.updateTransition
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun <T : Any> TypedCrossFade(
    targetState: T,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<Float>? = null,
    label: String = "Crossfade",
    content: @Composable (T) -> Unit,
) {
    val transition = updateTransition(targetState, label)
    transition.Crossfade(
        modifier = modifier,
        animationSpec = animationSpec ?: MaterialTheme.motionScheme.defaultEffectsSpec(),
        contentKey = { it::class },
        content = content
    )
}