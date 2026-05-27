package com.xbot.navigation.snackbar

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneDecoratorStrategy
import androidx.navigation3.scene.SceneDecoratorStrategyScope
import io.github.ajiekcx.declarativeSnackbar.core.SnackbarComponent
import io.github.ajiekcx.declarativeSnackbar.ui.SnackbarBox

@Composable
fun <T : Any, M : Any> rememberSnackbarSceneDecoratorStrategy(
    component: SnackbarComponent<M>,
    alignment: Alignment = Alignment.BottomCenter,
    enterTransition: EnterTransition = SnackbarSceneDecoratorDefaults.enterTransition(),
    exitTransition: ExitTransition = SnackbarSceneDecoratorDefaults.exitTransition(),
    snackbarContent: @Composable (M) -> Unit,
): SnackbarSceneDecoratorStrategy<T, M> =
    remember(component, alignment, enterTransition, exitTransition, snackbarContent) {
        SnackbarSceneDecoratorStrategy(
            component = component,
            alignment = alignment,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            snackbarContent = snackbarContent,
        )
    }

/**
 * Wraps every [Scene]'s content in a [SnackbarBox] so the given [SnackbarComponent] can render
 * messages above the active scene. The decorated scene's [key] is derived from the input scene's
 * class and key to keep NavDisplay's built-in transition animations working.
 */
class SnackbarSceneDecoratorStrategy<T : Any, M : Any>(
    private val component: SnackbarComponent<M>,
    private val alignment: Alignment,
    private val enterTransition: EnterTransition,
    private val exitTransition: ExitTransition,
    private val snackbarContent: @Composable (M) -> Unit,
) : SceneDecoratorStrategy<T> {
    override fun SceneDecoratorStrategyScope<T>.decorateScene(scene: Scene<T>): Scene<T> =
        SnackbarDecoratingScene(
            scene = scene,
            component = component,
            alignment = alignment,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            snackbarContent = snackbarContent,
        )
}

private class SnackbarDecoratingScene<T : Any, M : Any>(
    private val scene: Scene<T>,
    private val component: SnackbarComponent<M>,
    private val alignment: Alignment,
    private val enterTransition: EnterTransition,
    private val exitTransition: ExitTransition,
    private val snackbarContent: @Composable (M) -> Unit,
) : Scene<T> {
    override val key: Any = scene::class to scene.key
    override val entries: List<NavEntry<T>> = scene.entries
    override val previousEntries: List<NavEntry<T>> = scene.previousEntries
    override val metadata: Map<String, Any> = scene.metadata
    override val content: @Composable () -> Unit = {
        SnackbarBox(
            component = component,
            alignment = alignment,
            animationEnterTransition = enterTransition,
            animationExitTransition = exitTransition,
            snackbarContent = snackbarContent,
        ) {
            scene.content()
        }
    }
}

/**
 * Defaults matching Material3's [androidx.compose.material3.SnackbarHost] animation: fade combined
 * with a scale from 0.8 to 1 using the [MaterialTheme.motionScheme] fast effects/spatial specs.
 */
object SnackbarSceneDecoratorDefaults {
    private const val InitialScale = 0.8f

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    fun enterTransition(): EnterTransition {
        val motionScheme = MaterialTheme.motionScheme
        return fadeIn(animationSpec = motionScheme.fastEffectsSpec()) +
            scaleIn(animationSpec = motionScheme.fastSpatialSpec(), initialScale = InitialScale)
    }

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    fun exitTransition(): ExitTransition {
        val motionScheme = MaterialTheme.motionScheme
        return fadeOut(animationSpec = motionScheme.fastEffectsSpec()) +
            scaleOut(animationSpec = motionScheme.fastSpatialSpec(), targetScale = InitialScale)
    }
}
