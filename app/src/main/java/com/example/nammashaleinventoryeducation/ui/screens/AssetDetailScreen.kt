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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.nammashaleinventoryeducation.data.entity.HealthLog
import com.example.nammashaleinventoryeducation.ui.components.StatusBadge
import com.example.nammashaleinventoryeducation.ui.components.TimelineItem
import com.example.nammashaleinventoryeducation.ui.theme.*
import com.example.nammashaleinventoryeducation.utils.Constants
import com.example.nammashaleinventoryeducation.utils.DateUtils
import com.example.nammashaleinventoryeducation.viewmodel.AssetViewModel
import com.example.nammashaleinventoryeducation.viewmodel.HealthCheckViewModel

import com.example.nammashaleinventoryeducation.utils.ImageUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetDetailScreen(
    assetId: Int,
    assetViewModel: AssetViewModel,
    healthCheckViewModel: HealthCheckViewModel,
    userRole: String,
    onBack: () -> Unit,
    onDeleted: () -> Unit
) {
    LaunchedEffect(assetId) { assetViewModel.loadAsset(assetId) }
    val asset by assetViewModel.selectedAsset.collectAsState()
    val healthLogs by healthCheckViewModel.getLogsForAsset(assetId).collectAsState(initial = emptyList())
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showConditionSheet by remember { mutableStateOf(false) }
    val isAdmin = userRole == com.example.nammashaleinventoryeducation.utils.Constants.ROLE_ADMIN

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Asset") },
            text = { Text("Are you sure you want to delete this asset? This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    asset?.let { assetViewModel.deleteAsset(it) }
                    showDeleteDialog = false
                    onDeleted()
                }, colors = ButtonDefaults.textButtonColors(contentColor = Red40)) {
                    Text("Delete")
                }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") } }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Asset Details", fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        if (asset == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val a = asset!!
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp)
            ) {
                // Image area
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(16.dp)).background(Blue90),
                    contentAlignment = Alignment.Center
                ) {
                    if (!a.imagePath.isNullOrEmpty()) {
                        AsyncImage(
                            model = a.imagePath,
                            contentDescription = a.assetName,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        val categoryImageUrl = ImageUtils.getPlaceholderImageForAsset(a.assetName, a.category)
                        
                        if (categoryImageUrl != null) {
                            AsyncImage(
                                model = categoryImageUrl,
                                contentDescription = a.category,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            val normalizedCategory = a.category.trim().lowercase()
                            val categoryIcon = when {
                                normalizedCategory.contains("computer") -> Icons.Default.DesktopWindows
                                normalizedCategory.contains("laptop") -> Icons.Default.Laptop
                                normalizedCategory.contains("projector") -> Icons.Default.CastForEducation
                                normalizedCategory.contains("printer") -> Icons.Default.Print
                                normalizedCategory.contains("lab") || normalizedCategory.contains("science") -> Icons.Default.Science
                                normalizedCategory.contains("sport") -> Icons.Default.SportsBasketball
                                normalizedCategory.contains("furniture") -> Icons.Default.Chair
                                normalizedCategory.contains("digital") || normalizedCategory.contains("tablet") -> Icons.Default.TabletAndroid
                                normalizedCategory.contains("electrical") -> Icons.Default.ElectricBolt
                                else -> Icons.Default.Inventory2
                            }
                            Icon(categoryIcon, null, Modifier.size(64.dp), tint = Blue50)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))

                // Info card
                Card(shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text(a.assetName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        StatusBadge(a.condition)
                        Spacer(Modifier.height(12.dp))
                        InfoRow("Serial Number", a.serialNumber)
                        InfoRow("Category", a.category)
                        InfoRow("Purchase Date", DateUtils.formatForDisplay(a.purchaseDate))
                        a.notes?.let { InfoRow("Notes", it) }
                    }
                }
                Spacer(Modifier.height(20.dp))

                // History timeline
                if (healthLogs.isNotEmpty()) {
                    Text("Health History", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))
                    Card(shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(2.dp)) {
                        Column(Modifier.padding(16.dp)) {
                            healthLogs.forEachIndexed { index, log ->
                                TimelineItem(
                                    date = DateUtils.formatForDisplay(log.updatedDate),
                                    status = log.status,
                                    isLast = index == healthLogs.lastIndex
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                }

                // Actions
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = { showConditionSheet = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Update, null, Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Update")
                    }
                    if (isAdmin) {
                        Button(
                            onClick = { showDeleteDialog = true },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Red40)
                        ) {
                            Icon(Icons.Default.Delete, null, Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Delete")
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))

                // Condition update section
                if (showConditionSheet) {
                    Card(shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(2.dp)) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Update Condition", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                            Spacer(Modifier.height(12.dp))
                            Constants.CONDITIONS.forEach { condition ->
                                val (color, bgColor) = when (condition) {
                                    Constants.CONDITION_WORKING -> Pair(StatusWorking, StatusWorkingBg)
                                    Constants.CONDITION_NEEDS_REPAIR -> Pair(StatusRepair, StatusRepairBg)
                                    else -> Pair(StatusBroken, StatusBrokenBg)
                                }
                                OutlinedButton(
                                    onClick = {
                                        assetViewModel.updateAsset(a.copy(condition = condition))
                                        showConditionSheet = false
                                    },
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) { Text(condition) }
                            }
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(120.dp))
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}
