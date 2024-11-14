package com.angelmascarell.collectorhub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelmascarell.collectorhub.ui.theme.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeManager: ThemeManager
) : ViewModel() {

    // Exponer el tema como un StateFlow
    val isDarkTheme: StateFlow<Boolean> = themeManager.isDarkTheme
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    // Cambiar el tema y guardar la preferencia en DataStore
    fun toggleTheme() {
        viewModelScope.launch {
            val newTheme = !isDarkTheme.value
            themeManager.setDarkTheme(newTheme)
        }
    }
}