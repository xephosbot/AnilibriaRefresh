package com.xbot.sharedapp.navigation.deeplink

import com.eygraber.uri.Uri

/**
 * Parse the requested Uri and store it in a easily readable format
 *
 * @param uri the target deeplink uri to link to
 */
internal class DeepLinkRequest(
    val uri: Uri
) {
    /**
     * A list of path segments
     */
    val pathSegments: List<String> = uri.pathSegments

    /**
     * A map of query name to query value
     */
    val queries = buildMap {
        uri.getQueryParameterNames().forEach { argName ->
            uri.getQueryParameter(argName)?.let { this[argName] = it }
        }
    }

    // TODO add parsing for other Uri components, i.e. fragments, mimeType, action
}
