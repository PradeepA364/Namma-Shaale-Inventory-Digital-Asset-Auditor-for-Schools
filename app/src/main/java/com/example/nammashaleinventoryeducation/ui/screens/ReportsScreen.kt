package com.example.nammashaleinventoryeducation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nammashaleinventoryeducation.ui.components.*
import com.example.nammashaleinventoryeducation.ui.theme.*
import com.example.nammashaleinventoryeducation.utils.PdfGenerator
import com.example.nammashaleinventoryeducation.viewmodel.ReportsViewModel
import java.io.File

@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel,
    userRole: String,
    onNavigate: (String) -> Unit,
    currentRoute: String
) {
    val context = LocalContext.current
    val assets by viewModel.allAssets.collectAsState()
    val total by viewModel.totalCount.collectAsState(initial = 0)
    val working by viewModel.workingCount.collectAsState(initial = 0)
    val repair by viewModel.repairCount.collectAsState(initial = 0)
    val broken by viewModel.brokenCount.collectAsState(initial = 0)
    
    val isGenerating by viewModel.isGenerating.collectAsState()
    val pdfFile by viewModel.pdfFile.collectAsState()

    // Mock recent reports (In a real app, these would come from a database or file system)
    val recentReports = remember {
        val dir = context.getExternalFilesDir(android.os.Environment.DIRECTORY_DOCUMENTS)
        dir?.listFiles { file -> file.extension == "pdf" }?.toList()?.sortedByDescending { it.lastModified() } ?: emptyList()
    }

    LaunchedEffect(pdfFile) {
        pdfFile?.let { file ->
            Toast.makeText(context, "Report generated: ${file.name}", Toast.LENGTH_LONG).show()
            PdfGenerator.shareReport(context, file)
            viewModel.resetPdfFile()
        }
    }

    Scaffold(
        bottomBar = { BottomNavBar(currentRoute = currentRoute, onNavigate = onNavigate, userRole = userRole) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            item {
                Spacer(Modifier.height(24.dp))
                Text(
                    "Inventory Reports",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Blue50
                )
                Text(
                    "Generate and manage school inventory audit reports",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(24.dp))
            }

            // Summary Cards
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard("Total Assets", "$total", Icons.Default.Inventory2, Blue50, Modifier.weight(1f))
                    StatCard("Working", "$working", Icons.Default.CheckCircle, StatusWorking, Modifier.weight(1f))
                }
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard("Needs Repair", "$repair", Icons.Default.Handyman, StatusRepair, Modifier.weight(1f))
                    StatCard("Broken", "$broken", Icons.Default.Error, StatusBroken, Modifier.weight(1f))
                }
                Spacer(Modifier.height(32.dp))
            }

            // Action Buttons
            item {
                Text("Report Actions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                
                Button(
                    onClick = { viewModel.generateReport() },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = !isGenerating,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Blue50)
                ) {
                    if (isGenerating) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    } else {
                        Icon(Icons.Default.PictureAsPdf, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Generate PDF Report", style = MaterialTheme.typography.titleMedium)
                    }
                }
                
                Spacer(Modifier.height(12.dp))
                
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = { 
                            recentReports.firstOrNull()?.let { PdfGenerator.shareReport(context, it) }
                            ?: Toast.makeText(context, "No reports found", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.Share, null, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Share Latest")
                    }
                    
                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "Check Downloads folder for PDF files", Toast.LENGTH_LONG).show()
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.Download, null, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Download")
                    }
                }
                Spacer(Modifier.height(32.dp))
            }

            // Recent Reports
            item {
                Text("Recent Reports", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                if (recentReports.isEmpty()) {
                    Text(
                        "No reports generated yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }

            items(recentReports.take(5)) { report ->
                ReportItem(report, onOpen = { PdfGenerator.shareReport(context, it) })
                Spacer(Modifier.height(8.dp))
            }
            
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun ReportItem(file: File, onOpen: (File) -> Unit) {
    Card(
        onClick = { onOpen(file) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Description, contentDescription = null, tint = Blue50)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(file.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, maxLines = 1)
                Text(
                    "Generated: ${java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(file.lastModified())}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
