package com.example.nammashaleinventoryeducation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.nammashaleinventoryeducation.data.entity.Asset
import com.example.nammashaleinventoryeducation.ui.theme.Blue90
import com.example.nammashaleinventoryeducation.ui.theme.Blue50

import com.example.nammashaleinventoryeducation.utils.ImageUtils

import android.util.Log

@Composable
fun AssetCard(
    asset: Asset,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Log for debugging mapping
    LaunchedEffect(asset.id) {
        Log.d("AssetCard", "Asset: ${asset.assetName}, Category: ${asset.category}, HasPath: ${!asset.imagePath.isNullOrEmpty()}")
    }

    val categoryImageUrl = ImageUtils.getPlaceholderImageForAsset(asset.assetName, asset.category)

    // Normalize strings for consistent mapping
    val normalizedCategory = asset.category.trim().lowercase()

    val categoryIcon: ImageVector = when {
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

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Blue90),
                contentAlignment = Alignment.Center
            ) {
                if (!asset.imagePath.isNullOrEmpty()) {
                    AsyncImage(
                        model = asset.imagePath,
                        contentDescription = asset.assetName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else if (categoryImageUrl != null) {
                    AsyncImage(
                        model = categoryImageUrl,
                        contentDescription = asset.category,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = categoryIcon,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                        tint = Blue50
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = asset.assetName,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = asset.category,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            StatusBadge(condition = asset.condition)
        }
    }
}
