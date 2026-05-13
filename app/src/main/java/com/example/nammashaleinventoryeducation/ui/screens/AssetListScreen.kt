package com.example.nammashaleinventoryeducation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nammashaleinventoryeducation.ui.components.*
import com.example.nammashaleinventoryeducation.viewmodel.AssetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetListScreen(
    viewModel: AssetViewModel,
    userRole: String,
    onAssetClick: (Int) -> Unit,
    onAddAsset: () -> Unit,
    onNavigate: (String) -> Unit,
    currentRoute: String
) {
    val assets by viewModel.filteredAssets.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val filters = listOf("All", "Working", "Needs Repair", "Broken")

    Scaffold(
        bottomBar = { BottomNavBar(currentRoute = currentRoute, onNavigate = onNavigate, userRole = userRole) },
        floatingActionButton = {
            if (userRole == com.example.nammashaleinventoryeducation.utils.Constants.ROLE_ADMIN) {
                FloatingActionButton(
                    onClick = onAddAsset,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Asset")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Assets", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            AppSearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.setSearchQuery(it) }
            )
            Spacer(modifier = Modifier.height(12.dp))

            FilterChipRow(
                filters = filters,
                selectedFilter = selectedFilter,
                onFilterSelected = { viewModel.setFilter(it) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (assets.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No assets found", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(assets, key = { it.id }) { asset ->
                        AssetCard(
                            asset = asset,
                            onClick = { onAssetClick(asset.id) }
                        )
                    }
                }
            }
        }
    }
}
