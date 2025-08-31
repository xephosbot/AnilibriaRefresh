package com.xbot.designsystem.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.lerp

interface ExpressiveTextStyle {
    @Composable
    fun textStyleForInteraction(pressed: Boolean): TextStyle
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
class MorphingExpressiveTextStyle(
    private val from: TextStyle,
    private val to: TextStyle,
    private val animationSpec: FiniteAnimationSpec<Float>
) : ExpressiveTextStyle {

    @Composable
    override fun textStyleForInteraction(pressed: Boolean): TextStyle {
        val textStyle by animateTextStyleAsState(if (pressed) to else from, animationSpec)
        return textStyle
    }
}

@Composable
private fun animateTextStyleAsState(
    targetValue: TextStyle,
    animationSpec: AnimationSpec<Float>,
    finishedListener: ((TextStyle) -> Unit)? = null
): State<TextStyle> {
    val animation = remember { Animatable(0f) }
    var previousTextStyle by remember { mutableStateOf(targetValue) }
    var nextTextStyle by remember { mutableStateOf(targetValue) }

    val textStyleState = remember(animation.value) {
        derivedStateOf {
            lerp(previousTextStyle, nextTextStyle, animation.value)
        }
    }

    LaunchedEffect(targetValue, animationSpec) {
        previousTextStyle = textStyleState.value
        nextTextStyle = targetValue
        animation.snapTo(0f)
        animation.animateTo(1f, animationSpec)
        finishedListener?.invoke(textStyleState.value)
    }

    return textStyleState
}
