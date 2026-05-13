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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nammashaleinventoryeducation.data.entity.IssueLog
import com.example.nammashaleinventoryeducation.ui.components.StatusBadge
import com.example.nammashaleinventoryeducation.ui.theme.*
import com.example.nammashaleinventoryeducation.utils.Constants
import com.example.nammashaleinventoryeducation.utils.DateUtils
import com.example.nammashaleinventoryeducation.viewmodel.IssueRepairViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueRepairScreen(
    viewModel: IssueRepairViewModel,
    userRole: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val allIssues by viewModel.allIssues.collectAsState()
    val pendingRepairs by viewModel.pendingRepairs.collectAsState()
    val assets by viewModel.allAssets.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddDialog by remember { mutableStateOf(false) }
    val isAdmin = userRole == com.example.nammashaleinventoryeducation.utils.Constants.ROLE_ADMIN

    // Add issue dialog
    if (showAddDialog) {
        var selectedAssetId by remember { mutableIntStateOf(assets.firstOrNull()?.id ?: 0) }
        var description by remember { mutableStateOf("") }
        var assetExpanded by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Log New Issue") },
            text = {
                Column {
                    ExposedDropdownMenuBox(expanded = assetExpanded, onExpandedChange = { assetExpanded = !assetExpanded }) {
                        OutlinedTextField(
                            value = assets.find { it.id == selectedAssetId }?.assetName ?: "Select Asset",
                            onValueChange = {}, readOnly = true,
                            label = { Text("Asset") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(assetExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(expanded = assetExpanded, onDismissRequest = { assetExpanded = false }) {
                            assets.forEach { asset ->
                                DropdownMenuItem(
                                    text = { Text(asset.assetName) },
                                    onClick = { selectedAssetId = asset.id; assetExpanded = false }
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = description, onValueChange = { description = it },
                        label = { Text("Issue Description") },
                        minLines = 2, shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (description.isBlank()) {
                        Toast.makeText(context, "Please describe the issue", Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }
                    viewModel.addIssue(IssueLog(
                        assetId = selectedAssetId,
                        issueDescription = description,
                        issueDate = DateUtils.getCurrentDate(),
                        repairStatus = Constants.REPAIR_PENDING
                    ))
                    showAddDialog = false
                    Toast.makeText(context, "Issue logged!", Toast.LENGTH_SHORT).show()
                }) { Text("Save") }
            },
            dismissButton = { TextButton(onClick = { showAddDialog = false }) { Text("Cancel") } }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Issues & Repairs", fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") } }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }, containerColor = Blue50) {
                Icon(Icons.Default.Add, "Add Issue", tint = androidx.compose.ui.graphics.Color.White)
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 },
                    text = { Text("Issues (${allIssues.size})") })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 },
                    text = { Text("Repairs (${pendingRepairs.size})") })
            }

            val displayList = if (selectedTab == 0) allIssues else pendingRepairs

            if (displayList.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No ${if (selectedTab == 0) "issues" else "pending repairs"}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(displayList, key = { it.id }) { issue ->
                        IssueCard(issue = issue, assetName = viewModel.getAssetName(issue.assetId),
                            onStatusChange = if (selectedTab == 1 && isAdmin) { status -> viewModel.updateRepairStatus(issue, status) } else null
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun IssueCard(issue: IssueLog, assetName: String, onStatusChange: ((String) -> Unit)?) {
    val statusColor = when (issue.repairStatus) {
        Constants.REPAIR_PENDING -> StatusRepair
        Constants.REPAIR_IN_PROGRESS -> Blue50
        Constants.REPAIR_COMPLETED -> StatusWorking
        else -> Neutral50
    }
    Card(shape = RoundedCornerShape(14.dp), elevation = CardDefaults.cardElevation(1.dp)) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(assetName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Text(DateUtils.formatForDisplay(issue.issueDate), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                StatusBadge(issue.repairStatus)
            }
            Spacer(Modifier.height(8.dp))
            Text(issue.issueDescription, style = MaterialTheme.typography.bodyMedium)
            if (onStatusChange != null) {
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(onClick = { onStatusChange(Constants.REPAIR_IN_PROGRESS) }, label = { Text("In Progress") })
                    AssistChip(onClick = { onStatusChange(Constants.REPAIR_COMPLETED) }, label = { Text("Complete") })
                }
            }
        }
    }
}
