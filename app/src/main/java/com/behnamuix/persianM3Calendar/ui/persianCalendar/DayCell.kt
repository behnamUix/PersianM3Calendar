package com.behnamuix.persianM3Calendar.ui.persianCalendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun DayCell(
    modifier: Modifier = Modifier,
    day: Int?,
    isSelected: Boolean,
    isToday: Boolean,
    reminderCount: Int,
    onClick: () -> Unit
) {

    val containerColor =
        when {
            isSelected -> MaterialTheme.colorScheme.primary
            isToday -> MaterialTheme.colorScheme.primaryContainer
            else -> MaterialTheme.colorScheme.surface
        }

    val contentColor =
        when {
            isSelected -> MaterialTheme.colorScheme.onPrimary
            isToday -> MaterialTheme.colorScheme.onPrimaryContainer
            else -> MaterialTheme.colorScheme.onSurface
        }

    Box(
        modifier = modifier
            .padding(4.dp)
            .size(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(containerColor)
            .clickable(enabled = day != null) { onClick() },
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = day?.toString() ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor
            )

            if (reminderCount > 0) {
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.error)
                )
            }
        }
    }
}