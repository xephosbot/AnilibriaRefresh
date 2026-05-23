package com.xbot.navigation

import androidx.navigation3.runtime.deeplink.DeepLinkRequest

object ExternalUriHandler {

    private var cached: DeepLinkRequest? = null

    var listener: ((request: DeepLinkRequest) -> Unit)? = null
        set(value) {
            field = value
            if (value != null) {
                cached?.let { request ->
                    value.invoke(request)
                }
                cached = null
            }
        }

    fun onNewUri(request: DeepLinkRequest) {
        if (listener != null) {
            listener?.invoke(request)
            cached = null
        } else {
            cached = request
        }
    }

    fun onNewUri(uri: String) {
        onNewUri(DeepLinkRequest.fromUriString(uri))
    }
}