package com.angelmascarell.collectorhub.ui.theme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ThemeManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val THEME_KEY = booleanPreferencesKey("dark_theme")

    // Leer el tema desde el DataStore
    val isDarkTheme: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[THEME_KEY] ?: false // Predeterminado en modo claro
        }

    // Guardar la preferencia del tema
    suspend fun setDarkTheme(isDark: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDark
        }
    }
}