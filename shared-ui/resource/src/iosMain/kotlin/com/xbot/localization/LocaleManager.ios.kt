package com.xbot.localization

import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.Foundation.preferredLanguages
import platform.Foundation.setValue

actual object LocaleManager {
    private const val APPLE_LANGUAGES_KEY = "AppleLanguages"

    actual fun setLocale(language: AppLanguage) {
        val userDefaults = NSUserDefaults.standardUserDefaults
        // We put the selected language first in priority
        userDefaults.setObject(listOf(language.isoCode), forKey = APPLE_LANGUAGES_KEY)
        userDefaults.synchronize()
    }

    actual fun getLocale(): AppLanguage {
        val userDefaults = NSUserDefaults.standardUserDefaults
        val languages = userDefaults.arrayForKey(APPLE_LANGUAGES_KEY) as? List<*>
        val preferredLang = languages?.firstOrNull()?.toString()
            ?: NSLocale.preferredLanguages.firstOrNull()?.toString()
            ?: NSLocale.currentLocale.languageCode
            ?: "en"
        
        val isoCode = preferredLang.split("-").first()
        return AppLanguage.getByIsoCode(isoCode)
    }
}