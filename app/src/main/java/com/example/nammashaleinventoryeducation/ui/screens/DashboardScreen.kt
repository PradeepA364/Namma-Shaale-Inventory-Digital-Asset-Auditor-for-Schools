package com.example.nammashaleinventoryeducation.ui.screens

import androidx.compose.runtime.Composable
import com.example.nammashaleinventoryeducation.utils.Constants
import com.example.nammashaleinventoryeducation.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigate: (String) -> Unit,
    currentRoute: String
) {
    val userRole = viewModel.sessionManager.getUserRole()

    if (userRole == Constants.ROLE_ADMIN) {
        AdminDashboardScreen(
            viewModel = viewModel,
            onNavigate = onNavigate,
            currentRoute = currentRoute
        )
    } else {
        TeacherDashboardScreen(
            viewModel = viewModel,
            onNavigate = onNavigate,
            currentRoute = currentRoute
        )
    }
}
