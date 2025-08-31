package com.xbot.designsystem.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TwoRowsTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil3.SingletonImageLoader
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.xbot.designsystem.utils.releaseTitleState
import com.xbot.designsystem.utils.toComposeImageBitmap
import com.xbot.domain.models.Release
import com.xbot.localization.localizedName

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ReleaseLargeTopAppBar(
    release: Release?,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val bitmap by getBitmap(release?.poster)

    if (bitmap != null && release != null) {
        BoxWithConstraints {
            val height = calculateContainerHeight(maxWidth)
            val contentWidth = calculateContentWidth(maxWidth)

            TwoRowsTopAppBar(
                modifier = modifier
                    .asyncImageBackground(
                        bitmap = bitmap!!,
                        transition = { scrollBehavior?.state?.collapsedFraction ?: 0f }
                    ),
                title = { expanded ->
                    Text(
                        modifier = Modifier
                            .width(contentWidth)
                            .padding(end = if (expanded) 16.dp else 0.dp),
                        text = release.localizedName(),
                        maxLines = if (expanded) 2 else 1,
                        style = if (expanded) MaterialTheme.typography.displayMedium else MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                subtitle = { expanded ->
                    val releaseTitle by releaseTitleState(release)
                    if (expanded) {
                        Text(
                            text = releaseTitle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .graphicsLayer { alpha = 0.8f }
                        )
                    }
                },
                navigationIcon = navigationIcon,
                actions = actions,
                titleHorizontalAlignment = Alignment.CenterHorizontally,
                expandedHeight = height,
                windowInsets = windowInsets,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                ),
                scrollBehavior = scrollBehavior,
            )
        }
    }
}

@Composable
private fun Modifier.asyncImageBackground(
    bitmap: ImageBitmap,
    color: Color = MaterialTheme.colorScheme.surfaceContainer,
    transition: () -> Float,
): Modifier = this then graphicsLayer {
    compositingStrategy = CompositingStrategy.Offscreen
}.drawWithCache {
    val (dstSize, dstOffset) = calculateImageSize(
        srcSize = Size(bitmap.width.toFloat(), bitmap.height.toFloat()),
        dstSize = size,
    )
    val edgeHeightPx = dstSize.height * 0.5f
    val brush = Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.Black),
        startY = size.height,
        endY = size.height - edgeHeightPx,
    )

    onDrawWithContent {
        clipRect {
            drawImage(
                image = bitmap,
                dstSize = dstSize,
                dstOffset = dstOffset,
            )
            drawRect(
                topLeft = Offset(0f, size.height - edgeHeightPx),
                size = Size(size.width, edgeHeightPx),
                brush = brush,
                blendMode = BlendMode.DstIn,
            )
            drawRect(
                color = color.copy(alpha = transition()),
            )
        }
        drawContent()
    }
}

@Composable
private fun getBitmap(data: Any?): State<ImageBitmap?> {
    val context = LocalPlatformContext.current
    val imageLoader = SingletonImageLoader.get(context)
    return produceState(initialValue = null, key1 = data) {
        val request = ImageRequest.Builder(context)
            .data(data)
            .target(
                onSuccess = { result ->
                    this.value = result.toComposeImageBitmap(context)
                },
                onError = {
                    this.value = null
                },
            )
            .build()
        imageLoader.enqueue(request)
    }
}

private fun calculateImageSize(
    srcSize: Size,
    dstSize: Size,
): Pair<IntSize, IntOffset> {
    val canvasWidth = dstSize.width
    val canvasHeight = dstSize.height
    val imageWidth = srcSize.width
    val imageHeight = srcSize.height

    val scale = maxOf(
        canvasWidth / imageWidth,
        canvasHeight / imageHeight
    )

    val scaledWidth = (imageWidth * scale).toInt()
    val scaledHeight = (imageHeight * scale).toInt()

    val offsetX = (canvasWidth - scaledWidth) / 2f
    val offsetY = (canvasHeight - scaledHeight) / 2f

    return IntSize(scaledWidth, scaledHeight) to IntOffset(offsetX.toInt(), offsetY.toInt())
}