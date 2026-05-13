package com.example.nammashaleinventoryeducation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nammashaleinventoryeducation.ui.components.*
import com.example.nammashaleinventoryeducation.ui.theme.*
import com.example.nammashaleinventoryeducation.viewmodel.ReportsViewModel

@Composable
fun AnalyticsScreen(
    viewModel: ReportsViewModel,
    userRole: String,
    onNavigate: (String) -> Unit,
    currentRoute: String
) {
    val working by viewModel.workingCount.collectAsState(initial = 0)
    val repair by viewModel.repairCount.collectAsState(initial = 0)
    val broken by viewModel.brokenCount.collectAsState(initial = 0)
    val distribution by viewModel.categoryDistribution.collectAsState()

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
            Text("Inventory Analytics", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))

            // Condition Chart
            Text("Asset Condition Trends", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(12.dp))
            Card(shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    PieChart(
                        entries = listOf(
                            PieChartEntry("Working", working.toFloat(), StatusWorking),
                            PieChartEntry("Repair", repair.toFloat(), StatusRepair),
                            PieChartEntry("Broken", broken.toFloat(), StatusBroken)
                        ),
                        modifier = Modifier.fillMaxWidth().height(200.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))

            // Category Distribution
            Text("Category Distribution", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(12.dp))
            Card(shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(2.dp)) {
                BarChart(
                    entries = distribution.map { (cat, count) ->
                        BarChartEntry(cat, count.toFloat(), Blue50)
                    },
                    modifier = Modifier.padding(16.dp).height(250.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
