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
 * Hosts the whole Compose scene underneath a native tab bar. Not a navigation container: the single
 * Compose controller is a child of this controller rather than of any tab, so the composition
 * survives every tab switch, and Navigation 3 stays the source of truth.
 */
@OptIn(ExperimentalForeignApi::class)
internal class AnilibertyTabBarController :
    UITabBarController(nibName = null, bundle = null),
    UITabBarControllerDelegateProtocol {

    var onTabSelected: ((TopLevelNavKey) -> Unit)? = null

    private val topLevelRoutes: List<TopLevelNavKey> = TopLevelRoutes.toList()
    private val routesByIdentifier: Map<String, TopLevelNavKey> =
        topLevelRoutes.associateBy { it.tabIdentifier }
    private val tabsByIdentifier = mutableMapOf<String, UITab>()

    private var composeViewController: UIViewController? = null
    private var isInstalled = false

    /** Last snapshot seen; doubles as a replay buffer for pushes that arrive before [attach]. */
    private var appliedState: NativeTabBarState? = null

    // Not the constructor: UITabBarController's designated initializer loads its view, so
    // viewDidLoad runs while a Kotlin subclass's fields are still uninitialized.
    fun attach(composeViewController: UIViewController) {
        this.composeViewController = composeViewController
        installIfNeeded()
    }

    override fun viewDidLoad() {
        super.viewDidLoad()
        installIfNeeded()
    }

    override fun viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        composeViewController?.view?.setFrame(view.bounds)
        keepComposeAboveTabContent()
        updateComposeSafeAreaInsets()
    }

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
            // Unspecified for System is required: overriding it would pin the trait collection the
            // Compose child inherits and the app would stop tracking the system.
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
            // A visibility change alone schedules no layout pass, and the bar is part of the safe area.
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
        // Refused on purpose: selection is applied from the resulting Navigation 3 state instead.
        return false
    }

    private fun installIfNeeded() {
        if (isInstalled) return
        val composeViewController = composeViewController ?: return
        isInstalled = true

        delegate = this

        // Adding the child is what makes UIKit load its view; reading `view` first hands back nil
        // despite the non-null binding.
        addChildViewController(composeViewController)
        val composeView = composeViewController.view
        composeView.setFrame(view.bounds)
        view.addSubview(composeView)
        composeViewController.didMoveToParentViewController(this)

        setTabs(buildTabs(), animated = false)

        appliedState?.let { pending ->
            appliedState = null
            applyState(pending)
        }
    }

    // The Compose view must sit above the tab's content wrapper but below the bar; UIKit rebuilds
    // the hierarchy on every tab change, so this is re-asserted each layout pass.
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

        // Minus the inherited safe area, otherwise the home indicator inset is counted twice.
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

/** Transparent stand-in for a tab's content; the real content is the Compose child above it. */
private class PassthroughViewController : UIViewController(nibName = null, bundle = null) {
    override fun viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = UIColor.clearColor
        view.setUserInteractionEnabled(false)
    }
}
