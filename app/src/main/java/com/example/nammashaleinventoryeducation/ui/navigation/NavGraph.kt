package com.example.nammashaleinventoryeducation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.nammashaleinventoryeducation.ui.screens.*
import com.example.nammashaleinventoryeducation.utils.SessionManager
import com.example.nammashaleinventoryeducation.viewmodel.*

@Composable
fun NavGraph(
    navController: NavHostController,
    sessionManager: SessionManager,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit
) {
    val startDest = if (sessionManager.isLoggedIn()) Screen.Dashboard.route else Screen.Splash.route
    val userRole = sessionManager.getUserRole()

    NavHost(navController = navController, startDestination = startDest) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                sessionManager = sessionManager
            )
        }

        composable(Screen.Login.route) {
            val authViewModel: AuthViewModel = viewModel()
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Dashboard.route) {
            val dashboardViewModel: DashboardViewModel = viewModel()
            DashboardScreen(
                viewModel = dashboardViewModel,
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                },
                currentRoute = Screen.Dashboard.route
            )
        }

        composable(
            route = Screen.AssetList.route,
            arguments = listOf(navArgument("filter") { defaultValue = "All" })
        ) { backStackEntry ->
            val filter = backStackEntry.arguments?.getString("filter") ?: "All"
            val assetViewModel: AssetViewModel = viewModel()
            
            // Set the initial filter from navigation argument
            LaunchedEffect(filter) {
                assetViewModel.setFilter(filter)
            }
            
            AssetListScreen(
                viewModel = assetViewModel,
                userRole = userRole,
                onAssetClick = { assetId ->
                    navController.navigate(Screen.AssetDetail.createRoute(assetId))
                },
                onAddAsset = { navController.navigate(Screen.AddAsset.route) },
                onNavigate = { route ->
                    navController.navigate(route) { launchSingleTop = true }
                },
                currentRoute = Screen.AssetList.createRoute(filter)
            )
        }

        composable(Screen.AddAsset.route) {
            val assetViewModel: AssetViewModel = viewModel()
            AddAssetScreen(
                viewModel = assetViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.AssetDetail.route,
            arguments = listOf(navArgument("assetId") { type = NavType.IntType })
        ) { backStackEntry ->
            val assetId = backStackEntry.arguments?.getInt("assetId") ?: return@composable
            val assetViewModel: AssetViewModel = viewModel()
            val healthCheckViewModel: HealthCheckViewModel = viewModel()
            AssetDetailScreen(
                assetId = assetId,
                assetViewModel = assetViewModel,
                healthCheckViewModel = healthCheckViewModel,
                userRole = userRole,
                onBack = { navController.popBackStack() },
                onDeleted = {
                    navController.navigate(Screen.AssetList.createRoute()) {
                        popUpTo(Screen.Dashboard.route)
                    }
                }
            )
        }

        composable(Screen.HealthCheck.route) {
            val healthCheckViewModel: HealthCheckViewModel = viewModel()
            HealthCheckScreen(
                viewModel = healthCheckViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.IssueRepair.route) {
            val issueRepairViewModel: IssueRepairViewModel = viewModel()
            IssueRepairScreen(
                viewModel = issueRepairViewModel,
                userRole = userRole,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Reports.route) {
            val reportsViewModel: ReportsViewModel = viewModel()
            ReportsScreen(
                viewModel = reportsViewModel,
                userRole = userRole,
                onNavigate = { route ->
                    navController.navigate(route) { launchSingleTop = true }
                },
                currentRoute = Screen.Reports.route
            )
        }

        composable(Screen.Analytics.route) {
            val reportsViewModel: ReportsViewModel = viewModel()
            AnalyticsScreen(
                viewModel = reportsViewModel,
                userRole = userRole,
                onNavigate = { route ->
                    navController.navigate(route) { launchSingleTop = true }
                },
                currentRoute = Screen.Analytics.route
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                sessionManager = sessionManager,
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode,
                onLogout = {
                    sessionManager.clearSession()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigate = { route ->
                    navController.navigate(route) { launchSingleTop = true }
                },
                currentRoute = Screen.Profile.route
            )
        }
    }
}
