package com.angelmascarell.collectorhub.core.routes

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
import com.angelmascarell.collectorhub.signin.presentation.SignInScreen
import com.angelmascarell.collectorhub.home.presentation.HomeScreen
import com.angelmascarell.collectorhub.ui.view.GetMangaScreen
import com.angelmascarell.collectorhub.ui.view.PremiumScreen

// Global variable
val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController found!") }

@Composable
fun AppNavigation(appNavigationViewModel: AppNavigationViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    // Estado para manejar los valores de usuario y contraseña
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
                composable(route = Routes.RoomDetailsScreenRoute.route) {
                    //RoomDetailsScreen()
                }
                composable(route = Routes.BookingSummaryScreenRoute.route) {
                    //BookingSummaryScreen()
                }
                composable(route = Routes.PaymentGatewayScreenRoute.route) {
                    //PaymentGatewayScreen()
                }
                composable(route = Routes.PaidScreenRoute.route) {
                    //PaidScreen()
                }
                composable(route = Routes.ProfileScreenRoute.route) {
                    //ProfileScreen()
                }
                composable(route = Routes.MyBookingsScreenRoute.route) {
                    //MyBookingsScreen()
                }
            }
        }
    }
}
