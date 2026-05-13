package com.example.nammashaleinventoryeducation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nammashaleinventoryeducation.ui.components.*
import com.example.nammashaleinventoryeducation.ui.navigation.Screen
import com.example.nammashaleinventoryeducation.ui.theme.*
import com.example.nammashaleinventoryeducation.viewmodel.DashboardViewModel

@Composable
fun AdminDashboardScreen(
    viewModel: DashboardViewModel,
    onNavigate: (String) -> Unit,
    currentRoute: String
) {
    val total by viewModel.totalAssets.collectAsState(initial = 0)
    val working by viewModel.workingCount.collectAsState(initial = 0)
    val repair by viewModel.repairCount.collectAsState(initial = 0)
    val broken by viewModel.brokenCount.collectAsState(initial = 0)
    val userRole = com.example.nammashaleinventoryeducation.utils.Constants.ROLE_ADMIN

    // Admin theme colors (Blue-focused)
    val primaryColor = Blue50
    val secondaryColor = Blue95
    val darkBlue = Color(0xFF1E3A8A)

    Scaffold(
        bottomBar = { BottomNavBar(currentRoute = currentRoute, onNavigate = onNavigate, userRole = userRole) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header with Statistics Cards
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(darkBlue, primaryColor)
                        ),
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        text = "Admin Management Portal",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        text = viewModel.getUserName().ifEmpty { "Admin" },
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Stats Row
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        StatCardMini("Total Assets", "$total", Icons.Default.Inventory2, Modifier.weight(1f))
                        StatCardMini("Working", "$working", Icons.Default.CheckCircle, Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        StatCardMini("Repair", "$repair", Icons.Default.Build, Modifier.weight(1f))
                        StatCardMini("Broken", "$broken", Icons.Default.Error, Modifier.weight(1f))
                    }
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(modifier = Modifier.height(24.dp))

                // Analytics Preview
                Text("System Analytics", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        PieChart(
                            entries = listOf(
                                PieChartEntry("Working", working.toFloat(), StatusWorking),
                                PieChartEntry("Repair", repair.toFloat(), StatusRepair),
                                PieChartEntry("Broken", broken.toFloat(), StatusBroken)
                            ),
                            modifier = Modifier.fillMaxWidth().height(180.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Overall inventory health status distribution",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Admin Controls Grid
                Text("Inventory Management", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AdminActionButton("Add Asset", Icons.Default.Add, primaryColor, { onNavigate(Screen.AddAsset.route) }, Modifier.weight(1f))
                    AdminActionButton("Reports", Icons.Default.Assessment, Color(0xFFD97706), { onNavigate(Screen.Reports.route) }, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AdminActionButton("Repair Hub", Icons.Default.Handyman, Color(0xFFDC2626), { onNavigate(Screen.IssueRepair.route) }, Modifier.weight(1f))
                    AdminActionButton("Audits", Icons.Default.Assignment, Color(0xFF16A34A), { onNavigate(Screen.HealthCheck.route) }, Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun StatCardMini(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = Color.White.copy(alpha = 0.15f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(title, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.7f))
                Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun AdminActionButton(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}
