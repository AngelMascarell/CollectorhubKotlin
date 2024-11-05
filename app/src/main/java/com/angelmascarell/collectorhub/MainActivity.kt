package com.angelmascarell.collectorhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.angelmascarell.collectorhub.ui.theme.CollectorHubTheme
import com.angelmascarell.collectorhub.ui.theme.MyNormalBlue
import com.angelmascarell.collectorhub.ui.theme.MySoftBlue
import com.angelmascarell.collectorhubApp.core.routes.AppNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestDarkMode()
        }
    }

    @Composable
    fun TestDarkMode() {
        val myLightColorScheme = lightColorScheme(
            background = MySoftBlue,
            // other light mode colors
        )

        val myDarkColorScheme = darkColorScheme(
            background = MyNormalBlue,
            // other dark mode colors
        )

        MaterialTheme(colorScheme = if (isSystemInDarkTheme()) myDarkColorScheme else myLightColorScheme) {
            AppNavigation()
        }
    }
}