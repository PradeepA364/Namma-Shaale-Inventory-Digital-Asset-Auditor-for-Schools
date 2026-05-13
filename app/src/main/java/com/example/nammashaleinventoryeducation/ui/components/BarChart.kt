package com.example.nammashaleinventoryeducation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

data class BarChartEntry(val label: String, val value: Float, val color: Color)

@Composable
fun BarChart(
    entries: List<BarChartEntry>,
    modifier: Modifier = Modifier
) {
    val maxVal = entries.maxOfOrNull { it.value } ?: 1f
    
    Column(modifier = modifier) {
        if (entries.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No data available", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(entries) { entry ->
                    val animatedWidth by animateFloatAsState(
                        targetValue = if (maxVal > 0) entry.value / maxVal else 0f,
                        animationSpec = tween(1000),
                        label = "BarWidth"
                    )
                    
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = entry.label,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = entry.value.toInt().toString(),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = entry.color
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .background(entry.color.copy(alpha = 0.1f), shape = MaterialTheme.shapes.extraSmall)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(animatedWidth)
                                    .fillMaxHeight()
                                    .background(entry.color, shape = MaterialTheme.shapes.extraSmall)
                            )
                        }
                    }
                }
            }
        }
    }
}
