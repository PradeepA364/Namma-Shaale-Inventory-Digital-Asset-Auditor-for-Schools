package com.example.nammashaleinventoryeducation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.nammashaleinventoryeducation.ui.theme.Blue50
import com.example.nammashaleinventoryeducation.ui.theme.Green40
import com.example.nammashaleinventoryeducation.utils.Constants

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val requiredRole: String? = null
)

val bottomNavItems = listOf(
    BottomNavItem("dashboard", "Dashboard", Icons.Default.Dashboard),
    BottomNavItem("asset_list/All", "Assets", Icons.Default.Inventory2),
    BottomNavItem("reports", "Reports", Icons.Default.Assessment, Constants.ROLE_ADMIN),
    BottomNavItem("analytics", "Analytics", Icons.Default.PieChart, Constants.ROLE_ADMIN),
    BottomNavItem("profile", "Profile", Icons.Default.Person)
)

@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    userRole: String = Constants.ROLE_TEACHER
) {
    val primaryColor = if (userRole == Constants.ROLE_ADMIN) Blue50 else Green40
    
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = NavigationBarDefaults.Elevation
    ) {
        bottomNavItems.filter { it.requiredRole == null || it.requiredRole == userRole }.forEach { item ->
            val isSelected = if (item.route.startsWith("asset_list")) {
                currentRoute.startsWith("asset_list")
            } else {
                currentRoute == item.route
            }
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label, style = MaterialTheme.typography.labelSmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = primaryColor,
                    selectedTextColor = primaryColor,
                    indicatorColor = primaryColor.copy(alpha = 0.12f)
                )
            )
        }
    }
}
