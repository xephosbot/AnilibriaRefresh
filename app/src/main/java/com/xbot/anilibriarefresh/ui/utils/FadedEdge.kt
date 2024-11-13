package com.xbot.anilibriarefresh.ui.utils

import android.graphics.RuntimeShader
import android.os.Build
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.intellij.lang.annotations.Language

fun Modifier.fadedEdge(
    bottomEdge: Boolean = true,
    edgeHeight: Dp = DefaultFadingEdgeHeight,
) = graphicsLayer {
    compositingStrategy = CompositingStrategy.Offscreen
}.drawWithCache {
    val edgeHeightPx = edgeHeight.toPx()
    val brush = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val nonLinearGradientShader = RuntimeShader(NON_LINEAR_GRADIENT_SHADER).apply {
            setFloatUniform("resolution", size.width, size.height)
            setFloatUniform("edgeHeight", edgeHeightPx)
            setIntUniform("bottomEdge", if (bottomEdge) 1 else 0)
        }
        ShaderBrush(nonLinearGradientShader)
    } else {
        Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color.Black),
            startY = if (bottomEdge) size.height else 0f,
            endY = if (bottomEdge) size.height - edgeHeightPx else edgeHeightPx,
        )
    }
    onDrawWithContent {
        drawContent()
        drawRect(
            topLeft = Offset(0f, if (bottomEdge) size.height - edgeHeightPx else 0f),
            size = Size(size.width, edgeHeightPx),
            brush = brush,
            blendMode = BlendMode.DstIn,
        )
    }
}

@Language("AGSL")
private val NON_LINEAR_GRADIENT_SHADER = """
    uniform float2 resolution;
    uniform float edgeHeight;
    uniform int bottomEdge;
    
    float easeInOutQuad(float x) {
        return x < 0.5 ? 2.0 * x * x : 1.0 - pow(-2.0 * x + 2.0, 2.0) / 2.0;
    }
    
    half4 main(in float2 fragCoord) {
        float y = bottomEdge == 1
            ? fragCoord.y - (resolution.y - edgeHeight)
            : edgeHeight - fragCoord.y;
        float t = clamp(y / edgeHeight, 0.0, 1.0);
        float alpha = easeInOutQuad(1.0 - t);
        return half4(0.0, 0.0, 0.0, alpha);
    }
""".trimIndent()

private val DefaultFadingEdgeHeight = 96.dp
