package com.xbot.network.client.auth

import com.xbot.network.AnilibriaApi

/**
 * Configuration for authentication behavior.
 */
class AuthConfig {
    /**
     * URLs that require authentication
     */
    private val authenticatedUrls = mutableSetOf<String>()

    /**
     * URL patterns that require authentication (using regex)
     */
    private val authenticatedPatterns = mutableSetOf<Regex>()

    /**
     * Add specific URL that requires authentication
     */
    fun requireAuth(url: String) {
        authenticatedUrls.add(url)
    }

    /**
     * Add URL pattern that requires authentication
     */
    fun requireAuth(pattern: Regex) {
        authenticatedPatterns.add(pattern)
    }

    /**
     * Add multiple URLs that require authentication
     */
    fun requireAuth(vararg urls: String) {
        authenticatedUrls.addAll(urls)
    }

    /**
     * Check if URL requires authentication
     */
    fun requiresAuth(url: String): Boolean {
        // Check exact matches
        if (authenticatedUrls.contains(url)) return true

        // Check pattern matches
        return authenticatedPatterns.any { pattern ->
            pattern.matches(url)
        }
    }

    companion object {
        /**
         * Create default auth configuration for Anilibria API
         */
        fun createDefault(baseUrl: String): AuthConfig {
            val config = AuthConfig()

            config.requireAuth(
                "$baseUrl/accounts/users/auth/logout",
                "$baseUrl/accounts/users/me/profile",
                "$baseUrl/accounts/users/me/collections/ids",
                "$baseUrl/accounts/users/me/collections/releases",
                "$baseUrl/accounts/users/me/collections",
                "$baseUrl/accounts/users/me/favorites/ids",
                "$baseUrl/accounts/users/me/favorites/releases",
                "$baseUrl/accounts/users/me/favorites",
                "$baseUrl/accounts/users/me/views/timecodes",
                "$baseUrl/accounts/otp/accept"
            )

            config.requireAuth(Regex("$baseUrl/accounts/users/me/.*"))
            config.requireAuth(Regex("$baseUrl/accounts/otp/accept"))

            return config
        }
    }
}