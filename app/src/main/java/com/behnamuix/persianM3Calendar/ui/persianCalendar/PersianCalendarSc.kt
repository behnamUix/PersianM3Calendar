package com.behnamuix.persianM3Calendar.ui.persianCalendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.behnamuix.persianM3Calendar.model.Reminder
import com.behnamuix.persianM3Calendar.utils.generateCalendarCells
import com.behnamuix.persianM3Calendar.utils.getDaysInMonth
import com.behnamuix.persianM3Calendar.viewModel.PersianCalendarViewModel
import org.koin.androidx.compose.koinViewModel
import saman.zamani.persiandate.PersianDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersianCalendarScreen(
    modifier: Modifier = Modifier,
    persianVm: PersianCalendarViewModel = koinViewModel()
) {
    val today = remember { PersianDate() }

    val currentYear = persianVm.currentYear
    val currentMonth = persianVm.currentMonth
    val selectedDay = persianVm.selectedDay
    val showDialog = remember { mutableStateOf(false) }

    val firstDayOfMonth = remember(currentYear, currentMonth) {
        persianVm.getFirstDayInMonth()
    }

    val daysInMonth = remember(currentYear, currentMonth) {
        getDaysInMonth(currentYear, currentMonth)
    }

    val cells = remember(firstDayOfMonth, daysInMonth) {
        generateCalendarCells(firstDayOfMonth, daysInMonth)
    }

    Column(modifier = modifier) {
        CalendarHeader(
            title = "${persianVm.monthNames[currentMonth - 1]} $currentYear",
            onPrev = { persianVm.prevMonth() },
            onNext = { persianVm.nextMonth() }
        )

        WeekHeader()

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(cells) { day ->
                val isToday = day != null &&
                        currentYear == today.shYear &&
                        currentMonth == today.shMonth &&
                        day == today.shDay

                val reminderCount = if (day == null) 0
                else persianVm.getReminderForDay(currentYear, currentMonth, day).size

                DayCell(
                    day = day,
                    isSelected = day != null && day == selectedDay,
                    isToday = isToday,
                    reminderCount = reminderCount,
                    onClick = {
                        if (day != null) {
                            persianVm.selectDay(day)
                        }
                    }
                )
            }
        }

        // اضافه کردن persianVm.reminders.size برای بروزرسانی خودکار لیست بعد از ثبت
        val reminders = remember(selectedDay, currentMonth, currentYear, persianVm.reminders.size) {
            persianVm.getReminderForDay(
                currentYear,
                currentMonth,
                selectedDay ?: today.shDay
            )
        }

        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "یادآورهای ${selectedDay ?: today.shDay} ${persianVm.monthNames[currentMonth - 1]}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        ElevatedButton(onClick = { showDialog.value = true }) {
                            Text("افزودن")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (reminders.isEmpty()) {
                    item {
                        Text(
                            "هیچ یادآوری برای این روز ثبت نشده است.",
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        )
                    }
                } else {
                    items(reminders) { reminder ->
                        ReminderItem(reminder, persianVm)
                    }
                }
            }
        }
    }

    if (showDialog.value) {
        ReminderDialog(
            show = showDialog.value,
            onDismiss = { showDialog.value = false },
            onSave = { title, note ->
                // ثبت یادآور در ViewModel
                persianVm.addReminder(
                    Reminder(
                        year = currentYear,
                        month = currentMonth,
                        day = selectedDay ?: today.shDay,
                        title = title,
                        note = note
                    )
                )
                showDialog.value = false
            }
        )
    }
}

@Composable
fun ReminderItem(reminder: Reminder, persianVm: PersianCalendarViewModel) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reminder.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(8.dp))
                if (!reminder.note.isNullOrBlank()) {
                    Text(
                        text = reminder.note,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton({  persianVm.removeReminder(reminder)}) {
                Icon(

                    imageVector = Icons.Rounded.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

        }
    }
}

@Composable
fun ReminderDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    if (!show) return

    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "یادآور جدید",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("عنوان") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("توضیحات") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                enabled = title.isNotBlank(),
                onClick = { onSave(title, note) }
            ) {
                Text("ذخیره")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("لغو")
            }
        }
    )
}
