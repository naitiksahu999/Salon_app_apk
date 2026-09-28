package com.example.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.SalonWorkingDayEntity
import com.example.ui.theme.SalonGoldPrimary
import com.example.util.AppFeedbackHelper

/**
 * Dedicated, intuitive dialog to edit salon operating timings for any specific day
 * (e.g. Monday, Tuesday, Sunday) with preset chips and options to apply across the week.
 */
@Composable
fun DayTimingsEditorDialog(
    day: SalonWorkingDayEntity,
    onDismissRequest: () -> Unit,
    onSaveHours: (dayOfWeek: Int, isOpen: Boolean, openTime: String, closeTime: String, applyToAllWeekdays: Boolean, applyToAllDays: Boolean) -> Unit
) {
    val context = LocalContext.current

    var isOpen by remember { mutableStateOf(day.isOpen) }
    var openTime by remember { mutableStateOf(day.openTime.ifBlank { "09:00" }) }
    var closeTime by remember { mutableStateOf(day.closeTime.ifBlank { "19:00" }) }

    var applyToAllWeekdays by remember { mutableStateOf(false) }
    var applyToAllDays by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xD9000000))
                .imePadding()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("day_timings_editor_dialog"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Title Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Edit ${day.dayName} Hours",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Salon Opening & Closing Schedule",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        IconButton(onClick = onDismissRequest) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    // Open / Closed Toggle Card
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isOpen) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isOpen) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f) else MaterialTheme.colorScheme.error.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (isOpen) "Salon is Open on ${day.dayName}" else "Salon is Closed on ${day.dayName}",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isOpen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                )
                                Text(
                                    text = if (isOpen) "Customers can book slots on this day" else "No appointments can be booked on this day",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Switch(
                                checked = isOpen,
                                onCheckedChange = {
                                    AppFeedbackHelper.triggerClick(context)
                                    isOpen = it
                                },
                                modifier = Modifier.testTag("switch_is_open_${day.dayOfWeek}")
                            )
                        }
                    }

                    if (isOpen) {
                        // Opening Time Section
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Opening Time (24h format e.g. 09:00):",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = openTime,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 13.sp
                                )
                            }

                            OutlinedTextField(
                                value = openTime,
                                onValueChange = { openTime = it },
                                leadingIcon = { Icon(Icons.Default.AccessTime, contentDescription = null, modifier = Modifier.size(18.dp)) },
                                modifier = Modifier.fillMaxWidth().testTag("input_open_time"),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp)
                            )

                            // Quick preset chips for opening
                            val openPresets = listOf("08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00")
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                contentPadding = PaddingValues(vertical = 2.dp)
                            ) {
                                items(openPresets) { time ->
                                    FilterChip(
                                        selected = openTime == time,
                                        onClick = {
                                            AppFeedbackHelper.triggerSelection(context)
                                            openTime = time
                                        },
                                        label = { Text(time, fontSize = 11.sp) },
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                }
                            }
                        }

                        // Closing Time Section
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Closing Time (24h format e.g. 20:00):",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = closeTime,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 13.sp
                                )
                            }

                            OutlinedTextField(
                                value = closeTime,
                                onValueChange = { closeTime = it },
                                leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(18.dp)) },
                                modifier = Modifier.fillMaxWidth().testTag("input_close_time"),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp)
                            )

                            // Quick preset chips for closing
                            val closePresets = listOf("17:00", "18:00", "19:00", "20:00", "20:30", "21:00", "22:00")
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                contentPadding = PaddingValues(vertical = 2.dp)
                            ) {
                                items(closePresets) { time ->
                                    FilterChip(
                                        selected = closeTime == time,
                                        onClick = {
                                            AppFeedbackHelper.triggerSelection(context)
                                            closeTime = time
                                        },
                                        label = { Text(time, fontSize = 11.sp) },
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                }
                            }
                        }

                        // Batch Copy Options
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("Quick Apply to other days:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    FilterChip(
                                        selected = applyToAllWeekdays,
                                        onClick = {
                                            AppFeedbackHelper.triggerSelection(context)
                                            applyToAllWeekdays = !applyToAllWeekdays
                                            if (applyToAllWeekdays) applyToAllDays = false
                                        },
                                        label = { Text("Mon - Fri (Weekdays)", fontSize = 11.sp) },
                                        leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp)) },
                                        modifier = Modifier.weight(1f)
                                    )
                                    FilterChip(
                                        selected = applyToAllDays,
                                        onClick = {
                                            AppFeedbackHelper.triggerSelection(context)
                                            applyToAllDays = !applyToAllDays
                                            if (applyToAllDays) applyToAllWeekdays = false
                                        },
                                        label = { Text("All 7 Days", fontSize = 11.sp) },
                                        leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp)) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismissRequest,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Cancel")
                        }

                        Button(
                            onClick = {
                                AppFeedbackHelper.triggerSuccess(context)
                                onSaveHours(
                                    day.dayOfWeek,
                                    isOpen,
                                    openTime.trim(),
                                    closeTime.trim(),
                                    applyToAllWeekdays,
                                    applyToAllDays
                                )
                            },
                            modifier = Modifier.weight(1.3f).testTag("save_day_hours_button"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SalonGoldPrimary, contentColor = Color(0xFF141414))
                        ) {
                            Text("Save Schedule", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
