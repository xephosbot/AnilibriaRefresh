package com.xbot.sharedapp.ios

import co.touchlab.kermit.Logger
import com.xbot.domain.models.enums.ThemeOption
import com.xbot.navigation.TopLevelNavKey
import com.xbot.navigation.TopLevelRoutes
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.Foundation.NSProcessInfo
import platform.UIKit.UIColor
import platform.UIKit.UIEdgeInsetsMake
import platform.UIKit.UIImage
import platform.UIKit.UITab
import platform.UIKit.UITabBarController
import platform.UIKit.UITabBarControllerDelegateProtocol
import platform.UIKit.UIUserInterfaceStyle
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.UIKit.addChildViewController
import platform.UIKit.additionalSafeAreaInsets
import platform.UIKit.didMoveToParentViewController

/**
 * Hosts the whole Compose scene underneath a native tab bar.
 *
 * Navigation 3 remains the single source of truth: a tap is refused in [tabBarController] and
 * merely forwarded to [onTabSelected], and the selected tab is then applied from the resulting
 * navigation state in [applyState]. Nothing is mirrored, so a rejected navigation — such as opening
 * a login-gated tab while signed out — simply leaves the previous tab selected without any special
 * casing. [NativeTabBarChrome] is the other half of this: it is what turns the composition into the
 * snapshots [applyState] receives.
 *
 * The Compose controller is a child of this controller rather than of an individual tab, so the
 * composition (and with it the whole Navigation 3 back stack, scene strategies and shared element
 * transitions) survives every tab switch. Each tab is backed by an empty placeholder, and the
 * Compose view is kept above that placeholder but below the bar — see [keepComposeAboveTabContent].
 *
 * The Compose controller arrives through [attach] rather than the constructor:
 * `UITabBarController`'s designated initializer loads its view, so `viewDidLoad` runs while the
 * Kotlin fields of a subclass are still uninitialized.
 */
@OptIn(ExperimentalForeignApi::class)
internal class AnilibertyTabBarController :
    UITabBarController(nibName = null, bundle = null),
    UITabBarControllerDelegateProtocol {

    /** Set from the composition; invoked when the user picks a destination. */
    var onTabSelected: ((TopLevelNavKey) -> Unit)? = null

    private val topLevelRoutes: List<TopLevelNavKey> = TopLevelRoutes.toList()
    private val routesByIdentifier: Map<String, TopLevelNavKey> =
        topLevelRoutes.associateBy { it.tabIdentifier }
    private val tabsByIdentifier = mutableMapOf<String, UITab>()

    private var composeViewController: UIViewController? = null
    private var isInstalled = false

    /**
     * The last snapshot seen, or null if none has arrived yet. Doubles as a replay buffer: the
     * composition may push before the Compose view is attached, which makes the ordering between
     * this controller being built and the composition starting irrelevant.
     */
    private var appliedState: NativeTabBarState? = null

    fun attach(composeViewController: UIViewController) {
        this.composeViewController = composeViewController
        installIfNeeded()
    }

    override fun viewDidLoad() {
        super.viewDidLoad()
        // Runs from the superclass initializer as well, before `attach` — hence the guard.
        installIfNeeded()
    }

    override fun viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        composeViewController?.view?.setFrame(view.bounds)
        keepComposeAboveTabContent()
        updateComposeSafeAreaInsets()
    }

    /** Projects a Compose snapshot onto the bar. Never reads state back out of UIKit. */
    fun applyState(state: NativeTabBarState) {
        val previous = appliedState
        if (previous == state) return
        appliedState = state

        if (!isInstalled) return

        if (previous?.tabTitles != state.tabTitles) {
            state.tabTitles.forEach { (route, title) ->
                tabsByIdentifier[route.tabIdentifier]?.setTitle(title)
            }
        }

        if (previous?.themeOption != state.themeOption) {
            // Applied to this controller rather than to the bar alone so the status bar and the
            // keyboard follow too. ThemeOption.System must stay unspecified: overriding it would
            // also pin the trait collection inherited by the Compose child, and the app would stop
            // tracking the system.
            overrideUserInterfaceStyle = when (state.themeOption) {
                ThemeOption.System -> UIUserInterfaceStyle.UIUserInterfaceStyleUnspecified
                ThemeOption.Light -> UIUserInterfaceStyle.UIUserInterfaceStyleLight
                ThemeOption.Dark -> UIUserInterfaceStyle.UIUserInterfaceStyleDark
            }
        }

        val tab = state.topLevelRoute?.tabIdentifier?.let(tabsByIdentifier::get)
        if (tab != null && tab != selectedTab) {
            selectedTab = tab
        }

        if (previous?.tabBarVisible != state.tabBarVisible) {
            setTabBarHidden(!state.tabBarVisible, animated = true)
            // The bar's height is part of the Compose safe area, so it has to be recomputed rather
            // than waiting for a layout pass that a visibility change alone does not schedule.
            view.setNeedsLayout()
        }
    }

    override fun tabBarController(
        tabBarController: UITabBarController,
        shouldSelectTab: UITab,
    ): Boolean {
        routesByIdentifier[shouldSelectTab.identifier]?.let { route ->
            onTabSelected?.invoke(route)
        }
        // Selection is driven exclusively by the resulting Navigation 3 state.
        return false
    }

    private fun installIfNeeded() {
        if (isInstalled) return
        val composeViewController = composeViewController ?: return
        isInstalled = true

        delegate = this

        // Adding the child first is what makes UIKit load its view; reading `view` beforehand
        // hands back nil even though the binding declares it non-null.
        addChildViewController(composeViewController)
        val composeView = composeViewController.view
        composeView.setFrame(view.bounds)
        view.addSubview(composeView)
        composeViewController.didMoveToParentViewController(this)

        setTabs(buildTabs(), animated = false)

        // Replays whatever the composition pushed while the tabs did not exist yet.
        appliedState?.let { pending ->
            appliedState = null
            applyState(pending)
        }
    }

    private fun keepComposeAboveTabContent() {
        val composeView = composeViewController?.view ?: return
        val subviews = view.subviews
        val composeIndex = subviews.indexOf(composeView)
        if (composeIndex < 0) return
        val contentWrapper = topLevelSubviewFor(selectedTab?.viewController?.view) ?: return
        val wrapperIndex = subviews.indexOf(contentWrapper)
        if (wrapperIndex >= 0 && composeIndex != wrapperIndex + 1) {
            view.insertSubview(composeView, aboveSubview = contentWrapper)
        }
    }

    /** Walks up from [descendant] to the direct subview of this controller's view. */
    private fun topLevelSubviewFor(descendant: UIView?): UIView? {
        val hostView = view
        var candidate = descendant
        while (candidate != null && candidate.superview != hostView) {
            candidate = candidate.superview
        }
        return candidate
    }

    private fun buildTabs(): List<UITab> = topLevelRoutes.map { route ->
        val symbolName = route.sfSymbolName ?: run {
            Logger.w { "No SF Symbol mapped for ${route.tabIdentifier}, using a generic icon" }
            FallbackSfSymbolName
        }
        val tab = UITab(
            title = "",
            image = UIImage.systemImageNamed(symbolName),
            identifier = route.tabIdentifier,
            viewControllerProvider = { PassthroughViewController() },
        )
        tabsByIdentifier[route.tabIdentifier] = tab
        tab
    }

    /**
     * Reports the area covered by the tab bar to Compose as extra safe area, mirroring what UIKit
     * does for a real tab child. Only the part not already covered by the inherited safe area is
     * added, otherwise the home indicator inset would be counted twice.
     */
    private fun updateComposeSafeAreaInsets() {
        val composeViewController = composeViewController ?: return
        if (appliedState?.tabBarVisible == false) {
            composeViewController.additionalSafeAreaInsets = UIEdgeInsetsMake(0.0, 0.0, 0.0, 0.0)
            return
        }
        val hostHeight = view.bounds.useContents { size.height }
        val inheritedTop = view.safeAreaInsets.useContents { top }
        val inheritedBottom = view.safeAreaInsets.useContents { bottom }

        val contentTop: Double
        val contentBottom: Double
        if (isIOS26OrLater) {
            // Exact unobscured area, and it accounts for the iPad layout as well.
            val guide = contentLayoutGuide.layoutFrame
            contentTop = guide.useContents { origin.y }
            contentBottom = hostHeight - guide.useContents { origin.y + size.height }
        } else {
            val barTop = tabBar.frame.useContents { origin.y }
            val barHeight = tabBar.frame.useContents { size.height }
            val barIsAtTop = barTop <= 0.5
            contentTop = if (barIsAtTop) barTop + barHeight else 0.0
            contentBottom = if (barIsAtTop) 0.0 else hostHeight - barTop
        }

        composeViewController.additionalSafeAreaInsets = UIEdgeInsetsMake(
            maxOf(0.0, contentTop - inheritedTop),
            0.0,
            maxOf(0.0, contentBottom - inheritedBottom),
            0.0,
        )
    }

    private val isIOS26OrLater: Boolean
        get() = NSProcessInfo.processInfo.operatingSystemVersion.useContents { majorVersion >= 26L }
}

/**
 * Empty stand-in for a tab's content — the real content is the Compose controller owned by
 * [AnilibertyTabBarController]. Transparent so the tab switch animation shows nothing of its own.
 */
private class PassthroughViewController : UIViewController(nibName = null, bundle = null) {
    override fun viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = UIColor.clearColor
        view.setUserInteractionEnabled(false)
    }
}
