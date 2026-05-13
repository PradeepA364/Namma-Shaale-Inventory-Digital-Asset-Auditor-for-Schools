package com.example.nammashaleinventoryeducation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.nammashaleinventoryeducation.ui.theme.*
import com.example.nammashaleinventoryeducation.utils.Constants

@Composable
fun StatusBadge(condition: String, modifier: Modifier = Modifier) {
    val (bgColor, textColor) = when (condition) {
        Constants.CONDITION_WORKING -> Pair(StatusWorkingBg, StatusWorking)
        Constants.CONDITION_NEEDS_REPAIR -> Pair(StatusRepairBg, StatusRepair)
        Constants.CONDITION_BROKEN -> Pair(StatusBrokenBg, StatusBroken)
        else -> Pair(Neutral90, Neutral40)
    }
    Text(
        text = condition,
        style = MaterialTheme.typography.labelSmall,
        color = textColor,
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}
