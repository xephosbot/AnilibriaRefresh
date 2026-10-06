package com.xbot.sharedapp.logging

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Severity
import io.kotzilla.sdk.KotzillaCore

internal class KotzillaLogWriter(private val kotzilla: KotzillaCore) : LogWriter() {

    override fun isLoggable(tag: String, severity: Severity): Boolean = severity >= Severity.Error

    override fun log(severity: Severity, message: String, tag: String, throwable: Throwable?) {
        val title = message.ifBlank {
            throwable?.message.orEmpty()
        }.ifBlank { tag }.take(MAX_LENGTH)
        val description = throwable?.stackTraceToString()?.take(MAX_LENGTH) ?: title

        kotzilla.createIssue(title, description)
        if (throwable != null) {
            kotzilla.logError(title, throwable)
        }
    }

    private companion object {
        const val MAX_LENGTH = 256
    }
}
