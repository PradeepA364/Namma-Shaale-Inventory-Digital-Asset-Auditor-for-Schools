package com.example.nammashaleinventoryeducation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

data class PieChartEntry(val label: String, val value: Float, val color: Color)

@Composable
fun PieChart(
    entries: List<PieChartEntry>,
    modifier: Modifier = Modifier
) {
    val total = entries.sumOf { it.value.toDouble() }.toFloat()
    if (total == 0f) return

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Canvas(modifier = Modifier.size(140.dp)) {
            val canvasSize = size.minDimension
            val radius = canvasSize / 2
            val strokeWidth = 32f
            var startAngle = -90f

            entries.forEach { entry ->
                val sweep = (entry.value / total) * 360f
                drawArc(
                    color = entry.color,
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = strokeWidth),
                    topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                    size = Size(canvasSize - strokeWidth, canvasSize - strokeWidth)
                )
                startAngle += sweep
            }
        }

        Column(verticalArrangement = Arrangement.Center) {
            entries.forEach { entry ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Box(modifier = Modifier.size(12.dp).background(entry.color))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${entry.label}: ${entry.value.toInt()}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}


