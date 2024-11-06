package com.angelmascarell.collectorhub.core.routes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(
    val route: String,
    val icon: ImageVector
) {
    object ProfileItem : NavigationItem(
        route = Routes.ProfileScreenRoute.route,
        icon = Icons.Default.AccountCircle
    )
    object HomeItem : NavigationItem(
        route = Routes.HomeScreenRoute.route,
        icon = Icons.Default.Home
    )
    object MyBookingsItem : NavigationItem(
        route = Routes.MyBookingsScreenRoute.route,
        icon = Icons.Default.ShoppingCart
    )
}
