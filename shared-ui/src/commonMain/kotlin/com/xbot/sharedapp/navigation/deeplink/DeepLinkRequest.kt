package com.xbot.sharedapp.navigation.deeplink

import io.ktor.http.Url

/**
 * Parse the requested Uri and store it in a easily readable format
 *
 * @param uri the target deeplink uri to link to
 */
internal class DeepLinkRequest(
    val uri: Url
) {
    /**
     * A list of path segments
     */
    val pathSegments: List<String> = uri.pathSegments

    /**
     * A map of query name to query value
     */
    val queries = buildMap {
        uri.parameters.names().forEach { argName ->
            uri.parameters[argName]?.let { this[argName] = it }
        }
    }

    // TODO add parsing for other Uri components, i.e. fragments, mimeType, action
}