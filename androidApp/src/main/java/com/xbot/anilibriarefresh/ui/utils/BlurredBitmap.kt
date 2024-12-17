package com.xbot.anilibriarefresh.ui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.HardwareRenderer
import android.graphics.PixelFormat
import android.graphics.RenderEffect
import android.graphics.RenderNode
import android.graphics.Shader
import android.hardware.HardwareBuffer
import android.media.ImageReader
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.annotation.FloatRange
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlin.math.roundToInt

@Composable
fun rememberBlurredBitmap(
    bitmap: Bitmap,
    @FloatRange(from = 0.0, to = 25.0) radius: Float
): Bitmap {
    val context = LocalContext.current
    return remember(context, bitmap, radius) {
        val tempBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            BlurUtils.blur(tempBitmap, radius)
        } else {
            BlurUtils.blur(context, tempBitmap, radius)
        }
    }
}

object BlurUtils {
    private const val BITMAP_SCALE = 0.4f

    @Suppress("DEPRECATION")
    fun blur(context: Context, bitmap: Bitmap, radius: Float): Bitmap {
        val width = (bitmap.width * BITMAP_SCALE).roundToInt()
        val height = (bitmap.height * BITMAP_SCALE).roundToInt()

        val inputBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
        val outputBitmap = Bitmap.createBitmap(inputBitmap)

        val rs = RenderScript.create(context)
        val theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
        val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
        theIntrinsic.setRadius(radius)
        theIntrinsic.setInput(tmpIn)
        theIntrinsic.forEach(tmpOut)
        tmpOut.copyTo(outputBitmap)

        return outputBitmap
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun blur(bitmap: Bitmap, radius: Float): Bitmap {
        val width = (bitmap.width * BITMAP_SCALE).roundToInt()
        val height = (bitmap.height * BITMAP_SCALE).roundToInt()

        val inputBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
        val imageReader = ImageReader.newInstance(
            inputBitmap.width, inputBitmap.height,
            PixelFormat.RGBA_8888, 1,
            HardwareBuffer.USAGE_GPU_SAMPLED_IMAGE or HardwareBuffer.USAGE_GPU_COLOR_OUTPUT,
        )
        val renderNode = RenderNode("BlurEffect")
        val hardwareRenderer = HardwareRenderer()

        hardwareRenderer.setSurface(imageReader.surface)
        hardwareRenderer.setContentRoot(renderNode)
        renderNode.setPosition(0, 0, imageReader.width, imageReader.height)
        val blurRenderEffect = RenderEffect.createBlurEffect(
            radius, radius,
            Shader.TileMode.MIRROR,
        )
        renderNode.setRenderEffect(blurRenderEffect)

        val renderCanvas = renderNode.beginRecording()
        renderCanvas.drawBitmap(inputBitmap, 0f, 0f, null)
        renderNode.endRecording()
        hardwareRenderer.createRenderRequest()
            .setWaitForPresent(true)
            .syncAndDraw()

        val image = imageReader.acquireNextImage() ?: throw RuntimeException("No Image")
        val hardwareBuffer = image.hardwareBuffer ?: throw RuntimeException("No HardwareBuffer")
        val outputBitmap =  Bitmap.wrapHardwareBuffer(hardwareBuffer, null)
            ?: throw RuntimeException("Create Bitmap Failed")

        hardwareBuffer.close()
        image.close()
        imageReader.close()
        renderNode.discardDisplayList()
        hardwareRenderer.destroy()

        return outputBitmap
    }
}
