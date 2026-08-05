package com.xbot.logger

/** App-wide logging & error reporting facade. Implementations decide the sink (Kotzilla, Kermit, ...). */
interface AppLogger {
    fun log(message: String, tag: String = "App")
    fun reportError(throwable: Throwable, message: String? = null)
}
