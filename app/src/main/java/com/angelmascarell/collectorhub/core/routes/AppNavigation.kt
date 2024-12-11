package com.angelmascarell.collectorhub.core.routes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.angelmascarell.collectorhub.ui.view.HomeScreen
import com.angelmascarell.collectorhub.ui.view.SignInScreen
import com.angelmascarell.collectorhub.ui.view.CollectionScreen
import com.angelmascarell.collectorhub.ui.view.DesiredMangasScreen
import com.angelmascarell.collectorhub.ui.view.ErrorScreen
import com.angelmascarell.collectorhub.ui.view.GetMangaScreen
import com.angelmascarell.collectorhub.ui.view.NewMangasScreen
import com.angelmascarell.collectorhub.ui.view.PremiumScreen
import com.angelmascarell.collectorhub.ui.view.ProfileScreen
import com.angelmascarell.collectorhub.ui.view.SignUpScreen

// Global variable
val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController found!") }

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(appNavigationViewModel: AppNavigationViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(username, password) {
        if (username.isNotBlank() && password.isNotBlank()) {
            appNavigationViewModel.getFirstScreen(username, password)
        }
    }

    val firstScreen by appNavigationViewModel.firstScreen.observeAsState(Routes.SignInScreenRoute.route)

    CompositionLocalProvider(LocalNavController provides navController) {
        firstScreen?.let {
            NavHost(navController = navController, startDestination = Routes.SignInScreenRoute.route) {
                composable(route = Routes.HomeScreenRoute.route) {
                    HomeScreen()
                }
                composable(route = Routes.SignInScreenRoute.route) {
                    SignInScreen()
                }
                composable(route = Routes.PremiumScreenRoute.route) {
                    PremiumScreen()
                }
                composable(route = Routes.GetMangaScreenRoute.route) {
                    GetMangaScreen()
                }
                composable(route = Routes.ErrorScreenRoute.route) {
                    ErrorScreen()
                }
                composable(route = Routes.CollectionScreenRoute.route) {
                    CollectionScreen()
                }
                composable(route = Routes.ProfileScreenRoute.route) {
                    ProfileScreen()
                }
                composable(route = Routes.SignUpScreenRoute.route) {
                    SignUpScreen()
                }
                composable(route = Routes.DesiredMangasScreenRoute.route) {
                    DesiredMangasScreen()
                }
                composable(route = Routes.NewMangasScreenRoute.route) {
                    NewMangasScreen()
                }
            }
        }
    }
}
