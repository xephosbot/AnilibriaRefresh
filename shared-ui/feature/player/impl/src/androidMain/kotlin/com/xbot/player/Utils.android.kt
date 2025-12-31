package com.xbot.player

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * Find the closest Activity in a given Context
 */
internal inline fun <reified T : Activity> Context.findActivity(): T {
    var context = this
    while (context is ContextWrapper) {
        if (context is T) return context
        context = context.baseContext
    }
    throw IllegalStateException("PiP should be called in Activity context")
}
