package com.xbot.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.kdroidfilter.composemediaplayer.DefaultVideoPlayerState
import io.github.kdroidfilter.composemediaplayer.VideoPlayerState
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.AVFoundation.AVPlayerLayer
import platform.AVKit.AVPictureInPictureController
import platform.AVKit.AVPictureInPictureControllerDelegateProtocol
import platform.Foundation.NSError
import platform.Foundation.NSProcessInfo
import platform.darwin.NSObject

@Composable
actual fun rememberPictureInPictureController(player: VideoPlayerState): PictureInPictureController {
    val playerState = player as? DefaultVideoPlayerState
    val playerLayer = playerState?.playerLayer
    
    val controller = remember { PictureInPictureControllerImpl() }
    
    DisposableEffect(playerLayer) {
        if (playerLayer != null) {
            controller.initialize(playerLayer)
        }
        onDispose {
            controller.release()
        }
    }
    
    return controller
}

internal class PictureInPictureControllerImpl : PictureInPictureController {
    private var pipController: AVPictureInPictureController? = null
    private val delegate = PiPDelegate(this)

    override var isInPictureInPictureMode by mutableStateOf(false)
        private set
    override var isTransitioningToPip by mutableStateOf(false)
        private set
    
    // We don't use modifier on iOS for PiP as it's handled by AVPlayerLayer which is attached to the view
    override val modifier: Modifier = Modifier

    fun initialize(layer: AVPlayerLayer) {
        if (!AVPictureInPictureController.isPictureInPictureSupported()) return

        pipController?.delegate = null
        
        val controller = AVPictureInPictureController(layer)
        controller.delegate = delegate
        
        // Enable auto PiP if supported (iOS 14.2+)
        if (isAutoPiPSupported()) {
            controller.canStartPictureInPictureAutomaticallyFromInline = true
        }
        
        pipController = controller
    }
    
    fun release() {
        pipController?.delegate = null
        pipController = null
    }

    override fun enterPictureInPictureMode() {
        if (pipController?.isPictureInPictureActive() == false) {
            pipController?.startPictureInPicture()
        }
    }
    
    fun onPipWillStart() {
        isTransitioningToPip = true
    }
    
    fun onPipDidStart() {
        isInPictureInPictureMode = true
        isTransitioningToPip = false
    }
    
    fun onPipWillStop() {
        isTransitioningToPip = true
    }
    
    fun onPipDidStop() {
        isInPictureInPictureMode = false
        isTransitioningToPip = false
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun isAutoPiPSupported(): Boolean {
        val version = NSProcessInfo.processInfo.operatingSystemVersion
        return version.useContents {
            majorVersion > 14L || (majorVersion == 14L && minorVersion >= 2L)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private class PiPDelegate(
    private val controller: PictureInPictureControllerImpl
) : NSObject(), AVPictureInPictureControllerDelegateProtocol {
    
    override fun pictureInPictureControllerWillStartPictureInPicture(pictureInPictureController: AVPictureInPictureController) {
        controller.onPipWillStart()
    }
    
    override fun pictureInPictureControllerDidStartPictureInPicture(pictureInPictureController: AVPictureInPictureController) {
        controller.onPipDidStart()
    }
    
    override fun pictureInPictureControllerWillStopPictureInPicture(pictureInPictureController: AVPictureInPictureController) {
        controller.onPipWillStop()
    }
    
    override fun pictureInPictureControllerDidStopPictureInPicture(pictureInPictureController: AVPictureInPictureController) {
        controller.onPipDidStop()
    }
    
    override fun pictureInPictureController(pictureInPictureController: AVPictureInPictureController, failedToStartPictureInPictureWithError: NSError) {
        controller.onPipDidStop()
    }
    
    override fun pictureInPictureController(pictureInPictureController: AVPictureInPictureController, restoreUserInterfaceForPictureInPictureStopWithCompletionHandler: (Boolean) -> Unit) {
        restoreUserInterfaceForPictureInPictureStopWithCompletionHandler(true)
    }
}
