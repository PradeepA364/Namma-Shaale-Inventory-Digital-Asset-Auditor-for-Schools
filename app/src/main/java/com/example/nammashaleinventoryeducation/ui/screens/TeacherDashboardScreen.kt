package com.example.nammashaleinventoryeducation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nammashaleinventoryeducation.ui.components.*
import com.example.nammashaleinventoryeducation.ui.navigation.Screen
import com.example.nammashaleinventoryeducation.ui.theme.*
import com.example.nammashaleinventoryeducation.viewmodel.DashboardViewModel

@Composable
fun TeacherDashboardScreen(
    viewModel: DashboardViewModel,
    onNavigate: (String) -> Unit,
    currentRoute: String
) {
    val total by viewModel.totalAssets.collectAsState(initial = 0)
    val working by viewModel.workingCount.collectAsState(initial = 0)
    val repair by viewModel.repairCount.collectAsState(initial = 0)
    val broken by viewModel.brokenCount.collectAsState(initial = 0)
    val userRole = com.example.nammashaleinventoryeducation.utils.Constants.ROLE_TEACHER

    // Teacher specific theme colors (Green-focused)
    val primaryColor = Green40
    val secondaryColor = Green95

    Scaffold(
        bottomBar = { BottomNavBar(currentRoute = currentRoute, onNavigate = onNavigate, userRole = userRole) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Greeting Section (Professional & Clean)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${viewModel.getGreeting()},",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = viewModel.getUserName().ifEmpty { "Teacher" },
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )
                }
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(secondaryColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = primaryColor)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            // Quick Search Bar
            AppSearchBar(
                query = "", 
                onQueryChange = { onNavigate(Screen.AssetList.createRoute()) }, 
                placeholder = "Find an asset..."
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            // Task Focused Statistics (operational view)
            Text("Operational Overview", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Total Assets", "$total", Icons.Default.Inventory2, primaryColor, Modifier.weight(1f)) {
                    onNavigate(Screen.AssetList.createRoute("All"))
                }
                StatCard("Needs Repair", "$repair", Icons.Default.Handyman, StatusRepair, Modifier.weight(1f)) {
                    onNavigate(Screen.AssetList.createRoute("Needs Repair"))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Broken Assets", "$broken", Icons.Default.Warning, StatusBroken, Modifier.weight(1f)) {
                    onNavigate(Screen.AssetList.createRoute("Broken"))
                }
                // Placeholder for balance
                Box(modifier = Modifier.weight(1f))
            }
            
            Spacer(modifier = Modifier.height(32.dp))

            // Quick Operations Section
            Text("Quick Actions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(12.dp))
            
            Card(
                onClick = { onNavigate(Screen.HealthCheck.route) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = secondaryColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.HealthAndSafety, contentDescription = null, tint = primaryColor, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Monthly Health Check", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = primaryColor)
                        Text("Update condition for assigned assets", style = MaterialTheme.typography.bodySmall, color = primaryColor.copy(alpha = 0.7f))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(Icons.Default.ArrowForwardIos, contentDescription = null, tint = primaryColor, modifier = Modifier.size(16.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                QuickActionButton(
                    title = "View Assets", 
                    icon = Icons.Default.List, 
                    iconTint = primaryColor, 
                    onClick = { onNavigate(Screen.AssetList.createRoute()) }, 
                    modifier = Modifier.weight(1f)
                )
                QuickActionButton(
                    title = "Log Issue", 
                    icon = Icons.Default.ReportProblem, 
                    iconTint = StatusBroken, 
                    onClick = { onNavigate(Screen.IssueRepair.route) }, 
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
