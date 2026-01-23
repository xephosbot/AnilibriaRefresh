package com.xbot.preference.language

import androidx.lifecycle.ViewModel
import com.xbot.localization.AppLanguage
import com.xbot.localization.LocaleManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LanguageViewModel : ViewModel() {

    private val _state = MutableStateFlow(LanguageScreenState())
    val state: StateFlow<LanguageScreenState> = _state.asStateFlow()

    init {
        val currentLanguage = LocaleManager.getLocale()
        _state.update { it.copy(selectedLanguage = currentLanguage) }
    }

    fun onLanguageSelected(language: AppLanguage) {
        LocaleManager.setLocale(language)
        _state.update { it.copy(selectedLanguage = language) }
    }
}

data class LanguageScreenState(
    val selectedLanguage: AppLanguage = AppLanguage.English
)
