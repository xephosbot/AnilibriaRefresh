package com.xbot.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.xbot.domain.models.enums.ThemeOption
import com.xbot.domain.repository.AppearanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class DefaultAppearanceRepository(
    private val dataStore: DataStore<Preferences>
) : AppearanceRepository {

    private object Keys {
        val themeOption = stringPreferencesKey("theme_option")
        val isDynamicTheme = booleanPreferencesKey("is_dynamic_theme")
        val isPureBlack = booleanPreferencesKey("is_pure_black")
        val isExpressiveColor = booleanPreferencesKey("is_expressive_color")
    }

    override val themeOption: Flow<ThemeOption> = dataStore.data.map { preferences ->
        val savedValue = preferences[Keys.themeOption]
        if (savedValue != null) {
            try {
                ThemeOption.valueOf(savedValue)
            } catch (e: IllegalArgumentException) {
                ThemeOption.System
            }
        } else {
            ThemeOption.System
        }
    }

    override val isDynamicTheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[Keys.isDynamicTheme] ?: false
    }

    override val isPureBlack: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[Keys.isPureBlack] ?: true
    }

    override val isExpressiveColor: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[Keys.isExpressiveColor] ?: false
    }

    override suspend fun setThemeOption(option: ThemeOption) {
        dataStore.edit { preferences ->
            preferences[Keys.themeOption] = option.name
        }
    }

    override suspend fun setDynamicTheme(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[Keys.isDynamicTheme] = enabled
        }
    }

    override suspend fun setPureBlack(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[Keys.isPureBlack] = enabled
        }
    }

    override suspend fun setExpressiveColor(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[Keys.isExpressiveColor] = enabled
        }
    }
}