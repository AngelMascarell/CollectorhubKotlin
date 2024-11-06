package com.angelmascarell.collectorhub.core.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.angelmascarell.collectorhub.ui.theme.MySoftBlue
import com.angelmascarell.collectorhub.ui.theme.MyUltraBlue
import com.angelmascarell.collectorhub.core.routes.LocalNavController
import com.angelmascarell.collectorhub.core.routes.NavigationItem

@Composable
fun BottomNavigationBar(route: String) {
    val navController = LocalNavController.current
    val items = listOf(
        NavigationItem.HomeItem,
        NavigationItem.MyBookingsItem,
        NavigationItem.ProfileItem
    )

    var selectedItem by remember {
        mutableStateOf(items.firstOrNull { it.route == route } ?: items.first())
    }

    BottomAppBar(containerColor = MySoftBlue) {
        items.forEach { item ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors().copy(
                    selectedIndicatorColor = Color.Transparent
                ),
                icon = {
                    Box(
                        modifier = Modifier
                            /*.background(
                                color = if (selectedItem == item) Color.Black else Color.Transparent,
                                shape = CircleShape
                            )*/
                            .padding(8.dp) // Adjust padding as needed
                    ) {
                        Icon(
                            item.icon,
                            contentDescription = null,
                            tint = if (selectedItem == item) MyUltraBlue else Color.Gray
                        )
                    }
                },
                label = null,
                selected = selectedItem == item,
                onClick = {
                    if (item.route != route) {
                        selectedItem = item
                        navController.navigate(item.route)
                    }
                }
            )
        }
    }
}