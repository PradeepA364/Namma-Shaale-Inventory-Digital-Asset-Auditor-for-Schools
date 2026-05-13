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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.nammashaleinventoryeducation.ui.theme.*
import com.example.nammashaleinventoryeducation.utils.Constants
import com.example.nammashaleinventoryeducation.viewmodel.HealthCheckViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthCheckScreen(
    viewModel: HealthCheckViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val items by viewModel.healthCheckItems.collectAsState()
    val updated by viewModel.updatedCount.collectAsState()
    val total by viewModel.totalCount.collectAsState()
    val isSubmitted by viewModel.isSubmitted.collectAsState()

    LaunchedEffect(isSubmitted) {
        if (isSubmitted) {
            Toast.makeText(context, "Health check submitted!", Toast.LENGTH_SHORT).show()
            viewModel.resetSubmission()
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Monthly Health Check", fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp)) {
            Spacer(Modifier.height(8.dp))

            // Progress
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Blue95)) {
                Column(Modifier.padding(16.dp).fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.HealthAndSafety, null, tint = Blue50)
                        Spacer(Modifier.width(8.dp))
                        Text("$updated / $total Assets Updated", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { if (total > 0) updated.toFloat() / total else 0f },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = Blue50,
                        trackColor = Blue90
                    )
                }
            }
            Spacer(Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(items, key = { it.asset.id }) { item ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Row(
                            Modifier.padding(12.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(item.asset.assetName, style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text(item.asset.category, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Spacer(Modifier.width(8.dp))
                            // Status buttons
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                listOf(
                                    Triple(Constants.CONDITION_WORKING, StatusWorking, Icons.Default.CheckCircle),
                                    Triple(Constants.CONDITION_NEEDS_REPAIR, StatusRepair, Icons.Default.Build),
                                    Triple(Constants.CONDITION_BROKEN, StatusBroken, Icons.Default.Cancel)
                                ).forEach { (status, color, icon) ->
                                    val selected = item.selectedStatus == status
                                    FilledTonalIconButton(
                                        onClick = { viewModel.updateStatus(item.asset.id, status) },
                                        modifier = Modifier.size(44.dp).padding(horizontal = 2.dp),
                                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                                            containerColor = if (selected) color.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                                            contentColor = if (selected) color else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    ) {
                                        Icon(
                                            icon, status,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { viewModel.submitHealthCheck() },
                enabled = updated > 0,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Green40)
            ) {
                Icon(Icons.Default.CheckCircle, null)
                Spacer(Modifier.width(8.dp))
                Text("Submit Health Check", style = MaterialTheme.typography.titleMedium, color = Color.White)
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}
