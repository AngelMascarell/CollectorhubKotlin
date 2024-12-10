package com.angelmascarell.collectorhub

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.angelmascarell.collectorhub.ui.theme.MyNormalBlue
import com.angelmascarell.collectorhub.ui.theme.MySoftBlue
import com.angelmascarell.collectorhub.core.routes.AppNavigation
import com.angelmascarell.collectorhub.ui.theme.MyUltraBlue
import com.angelmascarell.collectorhub.viewmodel.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val isDarkTheme = themeViewModel.isDarkTheme.collectAsState(initial = false)

            TestDarkMode(isDarkTheme.value)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @Composable
    fun TestDarkMode(isDarkTheme: Boolean) {
        val myLightColorScheme = lightColorScheme(
            primary = MyUltraBlue,
            secondary = Color(0xFFFFA726),
            background = Color.White,
            surface = Color(0xFFF5F5F5),
            onPrimary = Color.White,
            onSecondary = Color.Black,
            onBackground = Color.Black,
            onSurface = Color.Black
        )

        val myDarkColorScheme = darkColorScheme(
            primary = MyUltraBlue,
            secondary = Color(0xFFFFA726),
            background = Color(0xFF1A1A1A),
            surface = Color(0xFF1E1E1E),
            onPrimary = Color.White,
            onSecondary = Color.Black,
            onBackground = Color(0xFFE0E0E0),
            onSurface = Color.White
        )


        MaterialTheme(
            colorScheme = if (isDarkTheme) myDarkColorScheme else myLightColorScheme
        ) {
            AppNavigation()
        }
    }
}
