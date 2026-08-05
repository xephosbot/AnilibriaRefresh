@file:OptIn(KotzillaInternalApi::class)

package com.xbot.logger

import co.touchlab.kermit.Logger
import io.kotzilla.annotation.KotzillaInternalApi
import io.kotzilla.sdk.KotzillaCore
import org.koin.core.annotation.Singleton

@Singleton
internal class KotzillaAppLogger : AppLogger {

    override fun log(message: String, tag: String) {
        Logger.withTag(tag).i { message }
        KotzillaCore.getDefaultInstanceOrNull()?.log(message)
    }

    override fun reportError(throwable: Throwable, message: String?) {
        Logger.withTag("App").e(throwable) { message ?: throwable.message.orEmpty() }

        KotzillaCore.getDefaultInstanceOrNull()?.let { core ->
            val title = (message ?: throwable.message ?: throwable::class.simpleName ?: "Error").take(256)
            val description = throwable.stackTraceToString().take(256).ifBlank { title }
            core.createIssue(title, description)
            core.logError(title, throwable)
        }
    }
}
