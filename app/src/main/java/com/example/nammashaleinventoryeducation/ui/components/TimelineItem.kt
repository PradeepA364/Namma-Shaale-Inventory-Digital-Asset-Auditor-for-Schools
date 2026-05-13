package com.example.nammashaleinventoryeducation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.nammashaleinventoryeducation.ui.theme.*
import com.example.nammashaleinventoryeducation.utils.Constants

@Composable
fun TimelineItem(
    date: String,
    status: String,
    isLast: Boolean = false,
    modifier: Modifier = Modifier
) {
    val dotColor = when (status) {
        Constants.CONDITION_WORKING -> StatusWorking
        Constants.CONDITION_NEEDS_REPAIR -> StatusRepair
        Constants.CONDITION_BROKEN -> StatusBroken
        else -> Neutral50
    }

    Row(modifier = modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        // Timeline dot and line
        Canvas(modifier = Modifier.width(24.dp).height(48.dp)) {
            drawCircle(color = dotColor, radius = 8f, center = Offset(12f, 12f))
            if (!isLast) {
                drawLine(
                    color = Color.LightGray,
                    start = Offset(12f, 24f),
                    end = Offset(12f, size.height),
                    strokeWidth = 2f
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = date, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            StatusBadge(condition = status)
        }
    }
}
