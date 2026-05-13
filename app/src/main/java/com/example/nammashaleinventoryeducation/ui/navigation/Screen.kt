package com.example.nammashaleinventoryeducation.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object AssetList : Screen("asset_list/{filter}") {
        fun createRoute(filter: String = "All") = "asset_list/$filter"
    }
    object AddAsset : Screen("add_asset")
    object AssetDetail : Screen("asset_detail/{assetId}") {
        fun createRoute(assetId: Int) = "asset_detail/$assetId"
    }
    object HealthCheck : Screen("health_check")
    object IssueRepair : Screen("issue_repair")
    object Reports : Screen("reports")
    object Analytics : Screen("analytics")
    object Profile : Screen("profile")
}
