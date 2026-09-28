package com.example.ui.admin

import android.widget.Toast
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.PhoneAndroid
import com.example.ui.components.RealMapPickerDialog
import com.example.ui.components.PhotoPreviewAndCropDialog
import com.example.ui.components.PhotoFitUtils
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.border
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.AmenityEntity
import com.example.data.model.BookingEntity
import com.example.data.model.BookingStatus
import com.example.data.model.SalonBreakEntity
import com.example.data.model.SalonConfigEntity
import com.example.data.model.SalonHolidayEntity
import com.example.data.model.SalonWorkingDayEntity
import com.example.data.model.ServiceEntity
import com.example.data.model.StaffEntity
import com.example.data.model.SupportMessageEntity
import com.example.data.model.UserEntity
import com.example.ui.components.CashPaymentBadge
import com.example.ui.components.StatusBadge
import com.example.util.AppFeedbackHelper
import com.example.util.ImagePickerHelper
import com.example.viewmodel.SalonViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val SalonGoldPrimary = Color(0xFFD4AF37)

@Composable
fun AdminAppScreen(
    viewModel: SalonViewModel,
    modifier: Modifier = Modifier
) {
    var selectedNavTab by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier.testTag("admin_bottom_nav"),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedNavTab == 0,
                    onClick = { selectedNavTab = 0 },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
                    label = { Text("Dashboard") },
                    modifier = Modifier.testTag("admin_nav_dashboard")
                )
                NavigationBarItem(
                    selected = selectedNavTab == 1,
                    onClick = { selectedNavTab = 1 },
                    icon = { Icon(Icons.Default.EventNote, contentDescription = null) },
                    label = { Text("Bookings") },
                    modifier = Modifier.testTag("admin_nav_bookings")
                )
                NavigationBarItem(
                    selected = selectedNavTab == 2,
                    onClick = { selectedNavTab = 2 },
                    icon = { Icon(Icons.Default.Storefront, contentDescription = null) },
                    label = { Text("Salon Ops") },
                    modifier = Modifier.testTag("admin_nav_salon_ops")
                )
                NavigationBarItem(
                    selected = selectedNavTab == 3,
                    onClick = { selectedNavTab = 3 },
                    icon = { Icon(Icons.Default.Group, contentDescription = null) },
                    label = { Text("Users & Help") },
                    modifier = Modifier.testTag("admin_nav_users_support")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedNavTab) {
                0 -> AdminDashboardScreen(
                    viewModel = viewModel,
                    onNavigateToBookings = { selectedNavTab = 1 }
                )
                1 -> AdminBookingsManagementScreen(viewModel = viewModel)
                2 -> AdminSalonOpsScreen(viewModel = viewModel)
                3 -> AdminUsersSupportScreen(viewModel = viewModel)
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 0: ADMIN DASHBOARD
// -------------------------------------------------------------
@Composable
fun AdminDashboardScreen(
    viewModel: SalonViewModel,
    onNavigateToBookings: () -> Unit
) {
    val allBookings by viewModel.allBookings.collectAsState()
    val activeStaff by viewModel.activeStaff.collectAsState()
    val activeServices by viewModel.activeServices.collectAsState()
    val salonConfig by viewModel.salonConfig.collectAsState()

    val todayStr = remember {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        sdf.format(Date())
    }

    val todayBookings = remember(allBookings, todayStr) {
        allBookings.filter { it.bookingDate == todayStr }
    }

    val pendingCount = remember(allBookings) {
        allBookings.count { it.status == BookingStatus.PENDING }
    }

    val totalCashCollected = remember(allBookings) {
        allBookings.filter { it.isCashCollected }.sumOf { it.servicePrice }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Admin Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Salon Manager Hub",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = salonConfig?.salonName ?: "Aura Luxury Beauty Lounge",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Admin Mode",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { viewModel.lockAdmin() }
                            .testTag("lock_admin_mode_header_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Lock Admin Mode",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Lock",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }

        // Metrics Grid (4 Cards)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Pending Approvals
                    ElevatedCard(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateToBookings() },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = if (pendingCount > 0) Color(0xFFFEF3C7) else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Pending",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (pendingCount > 0) Color(0xFFB45309) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Icon(
                                    imageVector = Icons.Default.HourglassTop,
                                    contentDescription = null,
                                    tint = if (pendingCount > 0) Color(0xFFB45309) else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "$pendingCount",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (pendingCount > 0) Color(0xFFB45309) else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (pendingCount > 0) "Needs your approval" else "All caught up",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (pendingCount > 0) Color(0xFFB45309) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Today's Bookings
                    ElevatedCard(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateToBookings() },
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Today",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "${todayBookings.size}",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Appointments today",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Total Cash Registered
                    ElevatedCard(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFF0FDF4))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Cash Collected",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color(0xFF047857)
                                )
                                Icon(
                                    imageVector = Icons.Default.AttachMoney,
                                    contentDescription = null,
                                    tint = Color(0xFF047857),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = (salonConfig?.currencySymbol ?: "₹") + String.format(Locale.US, "%.0f", totalCashCollected),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF047857)
                            )
                            Text(
                                text = "In-salon registers",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF047857)
                            )
                        }
                    }

                    // Active Stylists
                    ElevatedCard(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Stylists",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Icon(
                                    imageVector = Icons.Default.People,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "${activeStaff.size}",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "On duty roster",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Quick Pending Approvals Action Card
        if (pendingCount > 0) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToBookings() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.HourglassTop,
                                contentDescription = null,
                                tint = Color(0xFFB45309)
                            )
                            Column {
                                Text(
                                    text = "$pendingCount Booking Requests Pending",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB45309)
                                )
                                Text(
                                    text = "Tap to review, accept, or reschedule now",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF92400E)
                                )
                            }
                        }

                        Button(
                            onClick = onNavigateToBookings,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB45309))
                        ) {
                            Text("Manage", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Today's Appointments Agenda
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Today's Schedule Agenda ($todayStr)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                if (todayBookings.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No appointments scheduled for today yet.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        todayBookings.forEach { booking ->
                            ElevatedCard(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = "${booking.startTime} - ${booking.endTime}",
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            StatusBadge(status = booking.status)
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "${booking.serviceName} • ${booking.customerName}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = "Stylist: ${booking.staffName}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    CashPaymentBadge(
                                        isCollected = booking.isCashCollected,
                                        amount = booking.servicePrice
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 1: ADMIN BOOKINGS MANAGEMENT
// -------------------------------------------------------------
@Composable
fun AdminBookingsManagementScreen(viewModel: SalonViewModel) {
    val allBookings by viewModel.allBookings.collectAsState()
    val salonConfig by viewModel.salonConfig.collectAsState()
    var selectedFilter by remember { mutableStateOf("All") }

    // Dialog states
    var rejectDialogBooking by remember { mutableStateOf<BookingEntity?>(null) }
    var rejectReasonText by remember { mutableStateOf("") }

    var rescheduleDialogBooking by remember { mutableStateOf<BookingEntity?>(null) }
    var rescheduleDate by remember { mutableStateOf("") }
    var rescheduleTime by remember { mutableStateOf("") }

    var postponeDialogBooking by remember { mutableStateOf<BookingEntity?>(null) }
    var postponeDate by remember { mutableStateOf("") }
    var postponeTime by remember { mutableStateOf("") }
    var postponeReason by remember { mutableStateOf("") }

    var cashCollectionBooking by remember { mutableStateOf<BookingEntity?>(null) }

    val filteredBookings = remember(allBookings, selectedFilter) {
        when (selectedFilter) {
            "Pending" -> allBookings.filter { it.status == BookingStatus.PENDING }
            "Confirmed" -> allBookings.filter { it.status == BookingStatus.CONFIRMED }
            "In Progress" -> allBookings.filter { it.status == BookingStatus.IN_PROGRESS }
            "Completed" -> allBookings.filter { it.status == BookingStatus.COMPLETED }
            "Cancelled/Rejected" -> allBookings.filter { it.status == BookingStatus.CANCELLED || it.status == BookingStatus.REJECTED }
            else -> allBookings
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Bookings Management",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Accept, reject, reschedule, mark progress, and collect cash payments",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filter tabs
        ScrollableTabRow(
            selectedTabIndex = listOf("All", "Pending", "Confirmed", "In Progress", "Completed", "Cancelled/Rejected").indexOf(selectedFilter),
            edgePadding = 0.dp,
            divider = {}
        ) {
            listOf("All", "Pending", "Confirmed", "In Progress", "Completed", "Cancelled/Rejected").forEach { filter ->
                Tab(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    text = {
                        val count = when (filter) {
                            "Pending" -> allBookings.count { it.status == BookingStatus.PENDING }
                            "Confirmed" -> allBookings.count { it.status == BookingStatus.CONFIRMED }
                            "In Progress" -> allBookings.count { it.status == BookingStatus.IN_PROGRESS }
                            else -> null
                        }
                        Text(if (count != null && count > 0) "$filter ($count)" else filter)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (filteredBookings.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No bookings found in this category",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filteredBookings) { booking ->
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_booking_card_${booking.referenceNumber}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            // Header Row: Reference & Status
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = booking.referenceNumber,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "• ${booking.bookingDate}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                StatusBadge(status = booking.status)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = booking.serviceName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Customer & Staff details
                            Text(
                                text = "Guest: ${booking.customerName} (${booking.customerPhone})",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Time: ${booking.startTime} - ${booking.endTime} (${booking.serviceDurationMinutes}m) • Stylist: ${booking.staffName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            if (booking.notes.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Notes: \"${booking.notes}\"",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            if (booking.rejectReason.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Rejection Reason: ${booking.rejectReason}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(10.dp))

                            // Cash Status Badge & Action Controls
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CashPaymentBadge(
                                    isCollected = booking.isCashCollected,
                                    amount = booking.servicePrice,
                                    currencySymbol = salonConfig?.currencySymbol ?: "₹"
                                )

                                // Action: Collect Cash
                                if (!booking.isCashCollected && booking.status != BookingStatus.CANCELLED && booking.status != BookingStatus.REJECTED) {
                                    Button(
                                        onClick = { cashCollectionBooking = booking },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF047857)
                                        ),
                                        modifier = Modifier.testTag("collect_cash_button_${booking.id}")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AttachMoney,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Collect Cash", fontSize = 12.sp)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Action buttons per status
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                when (booking.status) {
                                    BookingStatus.PENDING -> {
                                        Button(
                                            onClick = { viewModel.acceptBooking(booking.id) },
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("accept_booking_button_${booking.id}"),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D4ED8))
                                        ) {
                                            Text("Accept")
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                rejectReasonText = ""
                                                rejectDialogBooking = booking
                                            },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                                        ) {
                                            Text("Reject")
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                postponeDate = booking.bookingDate
                                                postponeTime = booking.startTime
                                                postponeReason = "Front desk schedule adjustment"
                                                postponeDialogBooking = booking
                                            },
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("postpone_pending_button_${booking.id}"),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD97706))
                                        ) {
                                            Text("Postpone")
                                        }
                                    }

                                    BookingStatus.CONFIRMED -> {
                                        Button(
                                            onClick = { viewModel.markProgress(booking.id, BookingStatus.IN_PROGRESS) },
                                            modifier = Modifier.weight(1.3f),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E22CE))
                                        ) {
                                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Start Service")
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                postponeDate = booking.bookingDate
                                                postponeTime = booking.startTime
                                                postponeReason = "Front desk schedule adjustment"
                                                postponeDialogBooking = booking
                                            },
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("postpone_confirmed_button_${booking.id}"),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD97706))
                                        ) {
                                            Text("Postpone")
                                        }
                                    }

                                    BookingStatus.IN_PROGRESS -> {
                                        Button(
                                            onClick = { viewModel.markProgress(booking.id, BookingStatus.COMPLETED) },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857))
                                        ) {
                                            Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Finish & Mark Completed")
                                        }
                                    }

                                    else -> {
                                        // Completed or Cancelled - No pending status transitions needed
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // DIALOG: REJECT BOOKING
    rejectDialogBooking?.let { b ->
        AlertDialog(
            onDismissRequest = { rejectDialogBooking = null },
            title = { Text("Reject Booking #${b.referenceNumber}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Enter the reason for rejecting ${b.customerName}'s request (sent to guest):")
                    OutlinedTextField(
                        value = rejectReasonText,
                        onValueChange = { rejectReasonText = it },
                        placeholder = { Text("e.g. Specialist called in sick, please pick another day.") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val reason = if (rejectReasonText.isNotBlank()) rejectReasonText else "Unable to accommodate at requested time."
                        viewModel.rejectBooking(b.id, reason)
                        rejectDialogBooking = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Confirm Rejection")
                }
            },
            dismissButton = {
                TextButton(onClick = { rejectDialogBooking = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // DIALOG: RESCHEDULE BOOKING
    rescheduleDialogBooking?.let { b ->
        AlertDialog(
            onDismissRequest = { rescheduleDialogBooking = null },
            title = { Text("Reschedule #${b.referenceNumber}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Guest: ${b.customerName} • ${b.serviceName}")
                    OutlinedTextField(
                        value = rescheduleDate,
                        onValueChange = { rescheduleDate = it },
                        label = { Text("New Date (YYYY-MM-DD)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = rescheduleTime,
                        onValueChange = { rescheduleTime = it },
                        label = { Text("New Start Time (HH:mm, e.g. 15:00)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (rescheduleDate.isNotBlank() && rescheduleTime.isNotBlank()) {
                            viewModel.rescheduleBooking(b.id, rescheduleDate.trim(), rescheduleTime.trim())
                            rescheduleDialogBooking = null
                        }
                    }
                ) {
                    Text("Save & Notify Guest")
                }
            },
            dismissButton = {
                TextButton(onClick = { rescheduleDialogBooking = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // DIALOG: POSTPONE BOOKING
    postponeDialogBooking?.let { b ->
        val context = LocalContext.current
        AlertDialog(
            onDismissRequest = { postponeDialogBooking = null },
            icon = { Icon(Icons.Default.Update, contentDescription = null, tint = Color(0xFFD97706)) },
            title = { Text("Postpone Appointment #${b.referenceNumber}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Guest: ${b.customerName} • ${b.serviceName}", fontWeight = FontWeight.Bold)
                    Text("Original Slot: ${b.bookingDate} at ${b.startTime}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    OutlinedTextField(
                        value = postponeDate,
                        onValueChange = { postponeDate = it },
                        label = { Text("New Postponed Date (YYYY-MM-DD)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = postponeTime,
                        onValueChange = { postponeTime = it },
                        label = { Text("New Postponed Time (HH:mm, e.g. 16:30)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = postponeReason,
                        onValueChange = { postponeReason = it },
                        label = { Text("Reason for Postponement") },
                        placeholder = { Text("e.g., Stylist schedule adjustment") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFEF3C7)
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("✓ Customer App will display 'Appointment Postponed' banner with new time", fontSize = 11.sp, color = Color(0xFF92400E), fontWeight = FontWeight.SemiBold)
                            Text("✓ Automated notification will be dispatched to Customer Gmail (${b.customerEmail}) and Admin Gmail (${salonConfig?.adminGmail ?: "admin.unisexsalon@gmail.com"})", fontSize = 11.sp, color = Color(0xFF92400E))
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (postponeDate.isNotBlank() && postponeTime.isNotBlank()) {
                            viewModel.postponeBooking(
                                bookingId = b.id,
                                newDate = postponeDate.trim(),
                                newStartTime = postponeTime.trim(),
                                reason = postponeReason.trim().ifEmpty { "Stylist schedule adjustment" }
                            )
                            AppFeedbackHelper.triggerNotification(context)
                            postponeDialogBooking = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706))
                ) {
                    Text("Confirm Postpone & Notify")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { postponeDialogBooking = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // DIALOG: COLLECT CASH PAYMENT
    cashCollectionBooking?.let { b ->
        AlertDialog(
            onDismissRequest = { cashCollectionBooking = null },
            title = { Text("Confirm Cash Payment") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Collect physical cash payment at the front desk:")
                    Text(
                        text = "Amount: " + (salonConfig?.currencySymbol ?: "₹") + (if (b.servicePrice % 1.0 == 0.0) String.format(Locale.US, "%.0f", b.servicePrice) else String.format(Locale.US, "%.2f", b.servicePrice)),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857)
                    )
                    Text("Guest: ${b.customerName} for ${b.serviceName}")
                    Text(
                        text = "This records the cash receipt into the live database and sends an immediate digital confirmation to the customer.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.collectCashPayment(b.id)
                        cashCollectionBooking = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857))
                ) {
                    Text("Mark Cash Received")
                }
            },
            dismissButton = {
                TextButton(onClick = { cashCollectionBooking = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// -------------------------------------------------------------
// TAB 2: ADMIN SALON OPS (Services, Staff, Hours, Breaks, Holidays, Info)
// -------------------------------------------------------------
data class PhotoCropTarget(
    val type: String,
    val title: String,
    val url: String,
    val currentFit: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSalonOpsScreen(viewModel: SalonViewModel) {
    val context = LocalContext.current
    var subTab by remember { mutableIntStateOf(0) } // 0: Services, 1: Staff, 2: Facilities, 3: Hours/Holidays, 4: Brand & Visuals, 5: Location, 6: Security & Admin

    val services by viewModel.allServices.collectAsState()
    val staffList by viewModel.allStaff.collectAsState()
    val workingDays by viewModel.workingDays.collectAsState()
    val breaks by viewModel.breaks.collectAsState()
    val holidays by viewModel.holidays.collectAsState()
    val salonConfig by viewModel.salonConfig.collectAsState()
    val allAmenities by viewModel.allAmenities.collectAsState()

    // Confirmation Dialog State
    var showConfirmDialog by remember { mutableStateOf(false) }
    var confirmTitle by remember { mutableStateOf("") }
    var confirmMessage by remember { mutableStateOf("") }
    var onConfirmAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    fun promptConfirmation(title: String, message: String, action: () -> Unit) {
        AppFeedbackHelper.triggerNotification(context)
        confirmTitle = title
        confirmMessage = message
        onConfirmAction = action
        showConfirmDialog = true
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            icon = { Icon(Icons.Default.HelpOutline, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            title = { Text(confirmTitle, fontWeight = FontWeight.Bold) },
            text = { Text(confirmMessage) },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        AppFeedbackHelper.triggerSuccess(context)
                        onConfirmAction?.invoke()
                    },
                    modifier = Modifier.testTag("admin_confirm_save_button")
                ) {
                    Text("Confirm & Save")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Dialogs
    var serviceDialogItem by remember { mutableStateOf<ServiceEntity?>(null) }
    var isCreatingService by remember { mutableStateOf(false) }

    var staffDialogItem by remember { mutableStateOf<StaffEntity?>(null) }
    var isCreatingStaff by remember { mutableStateOf(false) }

    var amenityDialogItem by remember { mutableStateOf<AmenityEntity?>(null) }
    var isCreatingAmenity by remember { mutableStateOf(false) }

    var breakDialogItem by remember { mutableStateOf(false) }
    var holidayDialogItem by remember { mutableStateOf(false) }
    var editingWorkingDay by remember { mutableStateOf<SalonWorkingDayEntity?>(null) }
    var activeCropTarget by remember { mutableStateOf<PhotoCropTarget?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Salon Operations",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Real-time synchronization: changes reflect instantly in Customer App",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Subtabs
        ScrollableTabRow(
            selectedTabIndex = subTab,
            edgePadding = 12.dp
        ) {
            Tab(selected = subTab == 0, onClick = { AppFeedbackHelper.triggerClick(context); subTab = 0 }, text = { Text("Services") })
            Tab(selected = subTab == 1, onClick = { AppFeedbackHelper.triggerClick(context); subTab = 1 }, text = { Text("Staff") })
            Tab(selected = subTab == 2, onClick = { AppFeedbackHelper.triggerClick(context); subTab = 2 }, text = { Text("Facilities") }, modifier = Modifier.testTag("admin_subtab_facilities"))
            Tab(selected = subTab == 3, onClick = { AppFeedbackHelper.triggerClick(context); subTab = 3 }, text = { Text("Hours/Holidays") })
            Tab(selected = subTab == 4, onClick = { AppFeedbackHelper.triggerClick(context); subTab = 4 }, text = { Text("Brand & Visuals") }, modifier = Modifier.testTag("admin_subtab_brand"))
            Tab(selected = subTab == 5, onClick = { AppFeedbackHelper.triggerClick(context); subTab = 5 }, text = { Text("Location") }, modifier = Modifier.testTag("admin_subtab_location"))
            Tab(selected = subTab == 6, onClick = { AppFeedbackHelper.triggerClick(context); subTab = 6 }, text = { Text("Security & Admin") }, modifier = Modifier.testTag("admin_subtab_security"))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (subTab) {
            // SUBTAB 0: SERVICES
            0 -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${services.size} Salon Services",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = { isCreatingService = true },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("admin_add_service_button")
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Service")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(services) { service ->
                            ElevatedCard(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = service.name,
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold
                                            )
                                            if (!service.isActive) {
                                                Surface(
                                                    shape = RoundedCornerShape(6.dp),
                                                    color = MaterialTheme.colorScheme.errorContainer
                                                ) {
                                                    Text(
                                                        text = "Inactive",
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = MaterialTheme.colorScheme.error
                                                    )
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "${service.category} • ${service.durationMinutes} mins",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = (salonConfig?.currencySymbol ?: "₹") + (if (service.price % 1.0 == 0.0) String.format(Locale.US, "%.0f", service.price) else String.format(Locale.US, "%.2f", service.price)),
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = { serviceDialogItem = service }) {
                                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                                        }
                                        IconButton(onClick = { viewModel.deleteService(service) }) {
                                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // SUBTAB 1: STAFF
            1 -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${staffList.size} Certified Specialists",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = { isCreatingStaff = true },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("admin_add_staff_button")
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Staff")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(staffList) { staff ->
                            ElevatedCard(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(Color(staff.avatarColorHex)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = staff.name.take(1),
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = staff.name,
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "★ ${staff.rating}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color(0xFFD97706),
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Text(
                                            text = staff.roleTitle,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = staff.specialty,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = { staffDialogItem = staff }) {
                                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                                        }
                                        IconButton(onClick = { viewModel.deleteStaff(staff) }) {
                                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // SUBTAB 2: FACILITIES & AMENITIES (AC, WiFi, Parking, etc.)
            2 -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("admin_facilities_list"),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                                Text(
                                    text = "Salon Facilities & Amenities",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Adjust AC, WiFi, Parking & perks displayed to customers in real-time",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Button(
                                onClick = { isCreatingAmenity = true },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("admin_add_facility_button")
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add Facility")
                            }
                        }
                    }

                    if (allAmenities.isEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(Icons.Default.Spa, contentDescription = null, tint = SalonGoldPrimary, modifier = Modifier.size(36.dp))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("No facilities configured yet", fontWeight = FontWeight.SemiBold)
                                    Text("Tap 'Add Facility' above to add AC, WiFi, Valet Parking, etc.", fontSize = 12.sp, color = Color.Gray)
                                }
                            }
                        }
                    }

                    items(allAmenities, key = { it.id }) { amenity ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("facility_card_${amenity.id}"),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (amenity.isEnabled)
                                    MaterialTheme.colorScheme.surface
                                else
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (amenity.isEnabled)
                                                MaterialTheme.colorScheme.primaryContainer
                                            else
                                                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val iconVector = when (amenity.iconKey.uppercase()) {
                                        "AC", "AIR_CONDITIONING" -> Icons.Default.AcUnit
                                        "WIFI" -> Icons.Default.Wifi
                                        "PARKING", "VALET" -> Icons.Default.LocalParking
                                        "COFFEE", "BEVERAGES" -> Icons.Default.Coffee
                                        "LOUNGE" -> Icons.Default.Weekend
                                        "MUSIC" -> Icons.Default.MusicNote
                                        else -> Icons.Default.Spa
                                    }
                                    Icon(
                                        imageVector = iconVector,
                                        contentDescription = null,
                                        tint = if (amenity.isEnabled)
                                            MaterialTheme.colorScheme.onPrimaryContainer
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = amenity.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (!amenity.isEnabled) {
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = MaterialTheme.colorScheme.errorContainer
                                            ) {
                                                Text(
                                                    text = "Disabled",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = amenity.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Switch(
                                    checked = amenity.isEnabled,
                                    onCheckedChange = { viewModel.toggleAmenityEnabled(amenity) },
                                    modifier = Modifier.testTag("toggle_facility_${amenity.id}")
                                )

                                IconButton(onClick = { amenityDialogItem = amenity }) {
                                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Facility", tint = MaterialTheme.colorScheme.primary)
                                }

                                IconButton(onClick = { viewModel.deleteAmenity(amenity.id) }) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Facility", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }

            // SUBTAB 3: HOURS, BREAKS & HOLIDAYS
            3 -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    // Working Hours Section
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                                Text(
                                    text = "Salon Operating Schedule",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Set daily opening & closing times (Monday to Sunday)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            OutlinedButton(
                                onClick = {
                                    editingWorkingDay = workingDays.firstOrNull() ?: SalonWorkingDayEntity(1, "Monday", true, "09:00", "19:00")
                                },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Bulk Set Hours", fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                workingDays.forEach { day ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { editingWorkingDay = day }
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                Text(
                                                    text = day.dayName,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Surface(
                                                    shape = RoundedCornerShape(4.dp),
                                                    color = if (day.isOpen) Color(0xFF064E3B) else MaterialTheme.colorScheme.errorContainer
                                                ) {
                                                    Text(
                                                        text = if (day.isOpen) "OPEN" else "CLOSED",
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (day.isOpen) Color(0xFF34D399) else MaterialTheme.colorScheme.onErrorContainer,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = if (day.isOpen) "Operating Hours: ${day.openTime} — ${day.closeTime}" else "Closed all day",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = if (day.isOpen) SalonGoldPrimary else Color.Gray,
                                                fontWeight = if (day.isOpen) FontWeight.Medium else FontWeight.Normal
                                            )
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Switch(
                                                checked = day.isOpen,
                                                onCheckedChange = { checked ->
                                                    viewModel.updateWorkingDayHours(day.dayOfWeek, checked, day.openTime, day.closeTime)
                                                },
                                                modifier = Modifier.testTag("toggle_working_day_${day.dayOfWeek}")
                                            )
                                            IconButton(
                                                onClick = { editingWorkingDay = day },
                                                modifier = Modifier.testTag("edit_hours_${day.dayOfWeek}")
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Edit,
                                                    contentDescription = "Edit ${day.dayName} Timings",
                                                    tint = SalonGoldPrimary
                                                )
                                            }
                                        }
                                    }
                                    if (day.dayOfWeek < 7) {
                                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                                    }
                                }
                            }
                        }
                    }

                    // Breaks Section
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Salon Scheduled Breaks",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Blocks bookings during sanitation or team lunch",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Button(onClick = { breakDialogItem = true }) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add Break")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        breaks.forEach { brk ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Coffee, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                        Column {
                                            Text(brk.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                                            Text("${brk.startTime} - ${brk.endTime} (All Staff)", style = MaterialTheme.typography.bodySmall)
                                        }
                                    }

                                    IconButton(onClick = { viewModel.deleteSalonBreak(brk.id) }) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                    }

                    // Holidays Section
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Holidays & Closed Dates",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Completely blocks booking appointments on these days",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Button(onClick = { holidayDialogItem = true }) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add Holiday")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        holidays.forEach { hol ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.EventBusy, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                                        Column {
                                            Text("${hol.name} (${hol.dateString})", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                                            Text(hol.reason, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }

                                    IconButton(onClick = { viewModel.deleteSalonHoliday(hol.id) }) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // SUBTAB 4: BRAND & VISUAL CUSTOMIZATION
            4 -> {
                val context = LocalContext.current
                var salonName by remember(salonConfig) { mutableStateOf(salonConfig?.salonName ?: "") }
                var tagline by remember(salonConfig) { mutableStateOf(salonConfig?.tagline ?: "") }
                var phone by remember(salonConfig) { mutableStateOf(salonConfig?.phone ?: "") }
                var email by remember(salonConfig) { mutableStateOf(salonConfig?.email ?: "") }
                var about by remember(salonConfig) { mutableStateOf(salonConfig?.about ?: "") }
                var cashNotice by remember(salonConfig) { mutableStateOf(salonConfig?.cashNotice ?: "") }

                // Storefront Exterior Photo
                var storefrontImgUrl by remember(salonConfig) { mutableStateOf(salonConfig?.storefrontImageUrl ?: "") }

                // Hero & Banners State
                var heroLine1 by remember(salonConfig) { mutableStateOf(salonConfig?.heroTitleLine1 ?: "LOOK GOOD") }
                var heroLine2 by remember(salonConfig) { mutableStateOf(salonConfig?.heroTitleLine2 ?: "FEEL GOOD") }
                var heroAccent by remember(salonConfig) { mutableStateOf(salonConfig?.heroAccentText ?: "Always —") }
                var heroTags by remember(salonConfig) { mutableStateOf(salonConfig?.heroTags ?: "Hair | Skin | Grooming | Makeup") }
                var heroCta by remember(salonConfig) { mutableStateOf(salonConfig?.heroCtaText ?: "Book Appointment") }
                var heroImgUrl by remember(salonConfig) { mutableStateOf(salonConfig?.heroImageUrl ?: "") }

                var menTitle by remember(salonConfig) { mutableStateOf(salonConfig?.menPromoTitle ?: "Grooming & Style") }
                var menSub by remember(salonConfig) { mutableStateOf(salonConfig?.menPromoSubtitle ?: "FOR MEN") }
                var menImgUrl by remember(salonConfig) { mutableStateOf(salonConfig?.menPromoImageUrl ?: "") }

                var womenTitle by remember(salonConfig) { mutableStateOf(salonConfig?.womenPromoTitle ?: "Beauty & Care") }
                var womenSub by remember(salonConfig) { mutableStateOf(salonConfig?.womenPromoSubtitle ?: "FOR WOMEN") }
                var womenImgUrl by remember(salonConfig) { mutableStateOf(salonConfig?.womenPromoImageUrl ?: "") }

                // Direct Gallery Photo Launchers
                val storefrontPickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickVisualMedia()
                ) { uri ->
                    uri?.let {
                        val savedPath = ImagePickerHelper.copyUriToInternalStorage(context, it)
                        if (savedPath != null) {
                            storefrontImgUrl = savedPath
                            AppFeedbackHelper.triggerSuccess(context)
                            Toast.makeText(context, "Storefront photo loaded from gallery!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                val heroPickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickVisualMedia()
                ) { uri ->
                    uri?.let {
                        val savedPath = ImagePickerHelper.copyUriToInternalStorage(context, it)
                        if (savedPath != null) {
                            heroImgUrl = savedPath
                            AppFeedbackHelper.triggerSuccess(context)
                            Toast.makeText(context, "Hero banner photo loaded from gallery!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                val menPickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickVisualMedia()
                ) { uri ->
                    uri?.let {
                        val savedPath = ImagePickerHelper.copyUriToInternalStorage(context, it)
                        if (savedPath != null) {
                            menImgUrl = savedPath
                            AppFeedbackHelper.triggerSuccess(context)
                            Toast.makeText(context, "Men promo photo loaded from gallery!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                val womenPickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickVisualMedia()
                ) { uri ->
                    uri?.let {
                        val savedPath = ImagePickerHelper.copyUriToInternalStorage(context, it)
                        if (savedPath != null) {
                            womenImgUrl = savedPath
                            AppFeedbackHelper.triggerSuccess(context)
                            Toast.makeText(context, "Women promo photo loaded from gallery!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    // CARD 1: SALON BRAND & PROFILE
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().testTag("admin_brand_card"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text("Salon Brand Profile & Policies", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("Changes sync instantly to the Customer Home Screen, Header Bar, and App Drawers.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                                Text("Quick Name Suggestions:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(listOf("US UNISEX SALON", "Aura Unisex Lounge", "Luxe Unisex Atelier", "Crown Unisex Studio")) { suggestion ->
                                        FilterChip(
                                            selected = salonName == suggestion,
                                            onClick = { salonName = suggestion },
                                            label = { Text(suggestion, style = MaterialTheme.typography.labelSmall) }
                                        )
                                    }
                                }

                                OutlinedTextField(value = salonName, onValueChange = { salonName = it }, label = { Text("Salon Brand Name") }, modifier = Modifier.fillMaxWidth().testTag("admin_brand_name_field"))
                                OutlinedTextField(value = tagline, onValueChange = { tagline = it }, label = { Text("Tagline / Slogan") }, modifier = Modifier.fillMaxWidth())
                                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Customer Concierge Phone") }, modifier = Modifier.fillMaxWidth())
                                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Customer Concierge Email") }, modifier = Modifier.fillMaxWidth())
                                OutlinedTextField(value = about, onValueChange = { about = it }, label = { Text("About Salon") }, modifier = Modifier.fillMaxWidth(), maxLines = 3)
                                OutlinedTextField(value = cashNotice, onValueChange = { cashNotice = it }, label = { Text("Cash Payment Notice Policy") }, modifier = Modifier.fillMaxWidth(), maxLines = 2)

                                Button(
                                    onClick = {
                                        promptConfirmation(
                                            title = "Save Salon Brand Details",
                                            message = "Do you want to update the salon name to '$salonName' and sync all brand policies across customer apps?"
                                        ) {
                                            viewModel.updateSalonInfo(
                                                salonName = salonName,
                                                tagline = tagline,
                                                address = salonConfig?.address ?: "",
                                                phone = phone,
                                                email = email,
                                                about = about,
                                                cashNotice = cashNotice,
                                                adminGmail = salonConfig?.adminGmail ?: "admin.unisexsalon@gmail.com",
                                                adminPhone = salonConfig?.adminPhone ?: "+91 98765 43210",
                                                storefrontImageUrl = storefrontImgUrl
                                            )
                                            Toast.makeText(context, "Salon branding successfully updated & synced!", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().testTag("admin_save_brand_button"),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Save Salon Brand Details")
                                }
                            }
                        }
                    }

                    // CARD 2: STOREFRONT EXTERIOR PHOTO
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().testTag("admin_storefront_card"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text("Salon Storefront Exterior Photo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("Displayed prominently on Customer Home Screen showcasing your salon exterior.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                                if (storefrontImgUrl.isNotBlank()) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(140.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                    ) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(context)
                                                .data(storefrontImgUrl)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = "Storefront Preview",
                                            contentScale = PhotoFitUtils.getContentScaleForFit(salonConfig?.storefrontImageFit ?: "CROP_CENTER"),
                                            alignment = PhotoFitUtils.getAlignmentForFit(salonConfig?.storefrontImageFit ?: "CROP_CENTER"),
                                            modifier = Modifier.fillMaxSize()
                                        )

                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color.Black.copy(alpha = 0.78f),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, SalonGoldPrimary),
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(8.dp)
                                                .clickable {
                                                    activeCropTarget = PhotoCropTarget(
                                                        type = "storefront",
                                                        title = "Salon Exterior Photo",
                                                        url = storefrontImgUrl,
                                                        currentFit = salonConfig?.storefrontImageFit ?: "CROP_CENTER"
                                                    )
                                                }
                                                .testTag("crop_storefront_photo_button")
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(Icons.Default.Crop, contentDescription = null, tint = SalonGoldPrimary, modifier = Modifier.size(13.dp))
                                                Text("Edit / Visible Area", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }

                                Button(
                                    onClick = {
                                        storefrontPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth().testTag("admin_storefront_upload_button"),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Choose Photo from Gallery / Device Files", fontSize = 12.sp)
                                }

                                Text("Preset Exterior Samples:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(listOf(
                                        "Luxe Glass Facade" to "https://images.unsplash.com/photo-1521590832167-7bcbfaa6381f?auto=format&fit=crop&w=1200&q=80",
                                        "Modern Boutique" to "https://images.unsplash.com/photo-1560066984-138dadb4c035?auto=format&fit=crop&w=1200&q=80",
                                        "Minimalist Studio" to "https://images.unsplash.com/photo-1633681926022-84c23e8cb2d6?auto=format&fit=crop&w=1200&q=80"
                                    )) { (label, preset) ->
                                        FilterChip(
                                            selected = storefrontImgUrl == preset,
                                            onClick = { storefrontImgUrl = preset },
                                            label = { Text(label, style = MaterialTheme.typography.labelSmall) }
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        promptConfirmation(
                                            title = "Save Storefront Photo",
                                            message = "Update and apply this storefront exterior photo to the customer home screen?"
                                        ) {
                                            viewModel.updateSalonInfo(
                                                salonName = salonName,
                                                tagline = tagline,
                                                address = salonConfig?.address ?: "",
                                                phone = phone,
                                                email = email,
                                                about = about,
                                                cashNotice = cashNotice,
                                                adminGmail = salonConfig?.adminGmail ?: "admin.unisexsalon@gmail.com",
                                                adminPhone = salonConfig?.adminPhone ?: "+91 98765 43210",
                                                storefrontImageUrl = storefrontImgUrl
                                            )
                                            Toast.makeText(context, "Storefront photo updated!", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().testTag("admin_save_storefront_button"),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Save Storefront Photo")
                                }
                            }
                        }
                    }

                    // CARD 3: HERO BANNER & SLOGANS
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().testTag("admin_hero_card"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text("Hero Banner & Slogans (Customer View)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                                if (heroImgUrl.isNotBlank()) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(110.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                    ) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(context)
                                                .data(heroImgUrl)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = "Hero Preview",
                                            contentScale = PhotoFitUtils.getContentScaleForFit(salonConfig?.heroImageFit ?: "CROP_CENTER"),
                                            alignment = PhotoFitUtils.getAlignmentForFit(salonConfig?.heroImageFit ?: "CROP_CENTER"),
                                            modifier = Modifier.fillMaxSize()
                                        )

                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color.Black.copy(alpha = 0.78f),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, SalonGoldPrimary),
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(8.dp)
                                                .clickable {
                                                    activeCropTarget = PhotoCropTarget(
                                                        type = "hero",
                                                        title = "Hero Banner Photo",
                                                        url = heroImgUrl,
                                                        currentFit = salonConfig?.heroImageFit ?: "CROP_CENTER"
                                                    )
                                                }
                                                .testTag("crop_hero_photo_button")
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(Icons.Default.Crop, contentDescription = null, tint = SalonGoldPrimary, modifier = Modifier.size(13.dp))
                                                Text("Edit / Visible Area", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }

                                Button(
                                    onClick = {
                                        heroPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth().testTag("admin_hero_upload_button"),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Upload Hero Banner from Gallery / Files", fontSize = 12.sp)
                                }

                                Text("Preset Hero Photo Suggestions:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(listOf(
                                        "Modern Interior" to "https://images.unsplash.com/photo-1560066984-138dadb4c035?auto=format&fit=crop&w=1200&q=80",
                                        "Chic Stations" to "https://images.unsplash.com/photo-1521590832167-7bcbfaa6381f?auto=format&fit=crop&w=1200&q=80",
                                        "Aesthetic Lounge" to "https://images.unsplash.com/photo-1633681926022-84c23e8cb2d6?auto=format&fit=crop&w=1200&q=80"
                                    )) { (label, url) ->
                                        FilterChip(
                                            selected = heroImgUrl == url,
                                            onClick = { heroImgUrl = url },
                                            label = { Text(label, style = MaterialTheme.typography.labelSmall) }
                                        )
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(value = heroLine1, onValueChange = { heroLine1 = it }, label = { Text("Line 1 (e.g. LOOK GOOD)") }, modifier = Modifier.weight(1f))
                                    OutlinedTextField(value = heroLine2, onValueChange = { heroLine2 = it }, label = { Text("Line 2 (e.g. FEEL GOOD)") }, modifier = Modifier.weight(1f))
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(value = heroAccent, onValueChange = { heroAccent = it }, label = { Text("Accent (e.g. Always —)") }, modifier = Modifier.weight(1f))
                                    OutlinedTextField(value = heroCta, onValueChange = { heroCta = it }, label = { Text("Button CTA") }, modifier = Modifier.weight(1f))
                                }

                                OutlinedTextField(value = heroTags, onValueChange = { heroTags = it }, label = { Text("Services Subtitle Tags") }, modifier = Modifier.fillMaxWidth())

                                Button(
                                    onClick = {
                                        promptConfirmation(
                                            title = "Save Hero Banner",
                                            message = "Do you want to update the hero banner visual and headline typography on customer home?"
                                        ) {
                                            viewModel.updateSalonVisualsAndBanners(
                                                heroImageUrl = heroImgUrl,
                                                heroTitleLine1 = heroLine1,
                                                heroTitleLine2 = heroLine2,
                                                heroAccentText = heroAccent,
                                                heroTags = heroTags,
                                                heroCtaText = heroCta,
                                                menPromoTitle = menTitle,
                                                menPromoSubtitle = menSub,
                                                menPromoImageUrl = menImgUrl,
                                                womenPromoTitle = womenTitle,
                                                womenPromoSubtitle = womenSub,
                                                womenPromoImageUrl = womenImgUrl,
                                                storefrontImageUrl = storefrontImgUrl
                                            )
                                            Toast.makeText(context, "Hero banner updated and synced to Customer App!", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().testTag("admin_save_hero_button"),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Save Hero Banner Changes")
                                }
                            }
                        }
                    }

                    // CARD 4: GENDER PROMOTIONAL CARDS
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().testTag("admin_gender_promo_card"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text("Gender Promo Banners (Men & Women)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                                Text("Men's Section Customization", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(value = menTitle, onValueChange = { menTitle = it }, label = { Text("Men Title") }, modifier = Modifier.weight(1f))
                                    OutlinedTextField(value = menSub, onValueChange = { menSub = it }, label = { Text("Men Subtitle") }, modifier = Modifier.weight(1f))
                                }

                                if (menImgUrl.isNotBlank()) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(90.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                    ) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(context)
                                                .data(menImgUrl)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = "Men Preview",
                                            contentScale = PhotoFitUtils.getContentScaleForFit(salonConfig?.menPromoImageFit ?: "CROP_CENTER"),
                                            alignment = PhotoFitUtils.getAlignmentForFit(salonConfig?.menPromoImageFit ?: "CROP_CENTER"),
                                            modifier = Modifier.fillMaxSize()
                                        )

                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color.Black.copy(alpha = 0.78f),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, SalonGoldPrimary),
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(6.dp)
                                                .clickable {
                                                    activeCropTarget = PhotoCropTarget(
                                                        type = "men",
                                                        title = "Men Promo Banner",
                                                        url = menImgUrl,
                                                        currentFit = salonConfig?.menPromoImageFit ?: "CROP_CENTER"
                                                    )
                                                }
                                                .testTag("crop_men_photo_button")
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(Icons.Default.Crop, contentDescription = null, tint = SalonGoldPrimary, modifier = Modifier.size(12.dp))
                                                Text("Edit / Visible Area", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }

                                Button(
                                    onClick = {
                                        menPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Upload Men Photo from Gallery / Files", fontSize = 12.sp)
                                }

                                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(listOf(
                                        "Men Beard Model" to "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=800&q=80",
                                        "Sharp Fade Cut" to "https://images.unsplash.com/photo-1621605815971-fbc98d665033?auto=format&fit=crop&w=800&q=80"
                                    )) { (label, url) ->
                                        FilterChip(
                                            selected = menImgUrl == url,
                                            onClick = { menImgUrl = url },
                                            label = { Text(label, style = MaterialTheme.typography.labelSmall) }
                                        )
                                    }
                                }

                                HorizontalDivider()

                                Text("Women's Section Customization", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(value = womenTitle, onValueChange = { womenTitle = it }, label = { Text("Women Title") }, modifier = Modifier.weight(1f))
                                    OutlinedTextField(value = womenSub, onValueChange = { womenSub = it }, label = { Text("Women Subtitle") }, modifier = Modifier.weight(1f))
                                }

                                if (womenImgUrl.isNotBlank()) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(90.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                    ) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(context)
                                                .data(womenImgUrl)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = "Women Preview",
                                            contentScale = PhotoFitUtils.getContentScaleForFit(salonConfig?.womenPromoImageFit ?: "CROP_CENTER"),
                                            alignment = PhotoFitUtils.getAlignmentForFit(salonConfig?.womenPromoImageFit ?: "CROP_CENTER"),
                                            modifier = Modifier.fillMaxSize()
                                        )

                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color.Black.copy(alpha = 0.78f),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, SalonGoldPrimary),
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(6.dp)
                                                .clickable {
                                                    activeCropTarget = PhotoCropTarget(
                                                        type = "women",
                                                        title = "Women Promo Banner",
                                                        url = womenImgUrl,
                                                        currentFit = salonConfig?.womenPromoImageFit ?: "CROP_CENTER"
                                                    )
                                                }
                                                .testTag("crop_women_photo_button")
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(Icons.Default.Crop, contentDescription = null, tint = SalonGoldPrimary, modifier = Modifier.size(12.dp))
                                                Text("Edit / Visible Area", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }

                                Button(
                                    onClick = {
                                        womenPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Upload Women Photo from Gallery / Files", fontSize = 12.sp)
                                }

                                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(listOf(
                                        "Glamour & Hair" to "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=800&q=80",
                                        "Organic Facial Spa" to "https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?auto=format&fit=crop&w=800&q=80"
                                    )) { (label, url) ->
                                        FilterChip(
                                            selected = womenImgUrl == url,
                                            onClick = { womenImgUrl = url },
                                            label = { Text(label, style = MaterialTheme.typography.labelSmall) }
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        promptConfirmation(
                                            title = "Save Promotional Banners",
                                            message = "Do you want to update Men and Women promo banners across the customer app?"
                                        ) {
                                            viewModel.updateSalonVisualsAndBanners(
                                                heroImageUrl = heroImgUrl,
                                                heroTitleLine1 = heroLine1,
                                                heroTitleLine2 = heroLine2,
                                                heroAccentText = heroAccent,
                                                heroTags = heroTags,
                                                heroCtaText = heroCta,
                                                menPromoTitle = menTitle,
                                                menPromoSubtitle = menSub,
                                                menPromoImageUrl = menImgUrl,
                                                womenPromoTitle = womenTitle,
                                                womenPromoSubtitle = womenSub,
                                                womenPromoImageUrl = womenImgUrl,
                                                storefrontImageUrl = storefrontImgUrl
                                            )
                                            Toast.makeText(context, "Gender promo cards updated and synced to Customer App!", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().testTag("admin_save_gender_promo_button"),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Save All Visuals & Banners")
                                }
                            }
                        }
                    }
                }
            }

            // SUBTAB 5: SALON LOCATION & INTERACTIVE MAP COORDINATES
            5 -> {
                val context = LocalContext.current
                var streetAddress by remember(salonConfig) { mutableStateOf(salonConfig?.streetAddress ?: "") }
                var area by remember(salonConfig) { mutableStateOf(salonConfig?.area ?: "") }
                var city by remember(salonConfig) { mutableStateOf(salonConfig?.city ?: "New Delhi") }
                var state by remember(salonConfig) { mutableStateOf(salonConfig?.state ?: "Delhi") }
                var country by remember(salonConfig) { mutableStateOf(salonConfig?.country ?: "India") }
                var pincode by remember(salonConfig) { mutableStateOf(salonConfig?.pincode ?: "110016") }
                var latitude by remember(salonConfig) { mutableStateOf((salonConfig?.latitude ?: 28.5583).toString()) }
                var longitude by remember(salonConfig) { mutableStateOf((salonConfig?.longitude ?: 77.2028).toString()) }

                var showMapPickerSheet by remember { mutableStateOf(false) }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().testTag("admin_location_config_card"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primaryContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Salon Location & Map Pin",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Changes reflect instantly in Customer App, maps and directions",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                // Interactive Mini Map Box Preview
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(160.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF1E293B))
                                        .border(1.dp, SalonGoldPrimary.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                        .clickable {
                                            AppFeedbackHelper.triggerClick(context)
                                            showMapPickerSheet = true
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                                        val w = size.width
                                        val h = size.height
                                        val step = 32f
                                        var x = 0f
                                        while (x < w) {
                                            drawLine(
                                                color = androidx.compose.ui.graphics.Color(0x22FFFFFF),
                                                start = androidx.compose.ui.geometry.Offset(x, 0f),
                                                end = androidx.compose.ui.geometry.Offset(x, h),
                                                strokeWidth = 1f
                                            )
                                            x += step
                                        }
                                        var y = 0f
                                        while (y < h) {
                                            drawLine(
                                                color = androidx.compose.ui.graphics.Color(0x22FFFFFF),
                                                start = androidx.compose.ui.geometry.Offset(0f, y),
                                                end = androidx.compose.ui.geometry.Offset(w, y),
                                                strokeWidth = 1f
                                            )
                                            y += step
                                        }

                                        drawCircle(
                                            color = androidx.compose.ui.graphics.Color(0x33E5A65E),
                                            radius = 48f,
                                            center = androidx.compose.ui.geometry.Offset(w / 2f, h / 2f)
                                        )
                                        drawCircle(
                                            color = androidx.compose.ui.graphics.Color(0x22E5A65E),
                                            radius = 90f,
                                            center = androidx.compose.ui.geometry.Offset(w / 2f, h / 2f)
                                        )
                                    }

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PinDrop,
                                            contentDescription = null,
                                            tint = SalonGoldPrimary,
                                            modifier = Modifier.size(36.dp)
                                        )
                                        Text(
                                            text = if (streetAddress.isNotBlank()) "$streetAddress, $city" else "${salonConfig?.salonName ?: "US UNISEX SALON"} Pin",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = "Lat: $latitude • Lng: $longitude",
                                            color = Color(0xFFA0AEC0),
                                            fontSize = 11.sp
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(20.dp),
                                            color = SalonGoldPrimary
                                        ) {
                                            Text(
                                                text = "TAP TO SELECT FROM MAP",
                                                color = Color.Black,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            AppFeedbackHelper.triggerClick(context)
                                            showMapPickerSheet = true
                                        },
                                        modifier = Modifier.weight(1f).testTag("admin_select_from_map_button"),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Select from Map", fontSize = 12.sp)
                                    }

                                    OutlinedButton(
                                        onClick = {
                                            AppFeedbackHelper.triggerClick(context)
                                            val latD = latitude.toDoubleOrNull() ?: 28.5583
                                            val lngD = longitude.toDoubleOrNull() ?: 77.2028
                                            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:$latD,$lngD?q=$latD,$lngD(${Uri.encode(salonConfig?.salonName ?: "Salon")})"))
                                            context.startActivity(mapIntent)
                                        },
                                        modifier = Modifier.weight(1f).testTag("admin_preview_map_intent_button"),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.NearMe, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Test in Maps App", fontSize = 12.sp)
                                    }
                                }

                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                                Text("Structured Physical Location Details:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)

                                OutlinedTextField(
                                    value = streetAddress,
                                    onValueChange = { streetAddress = it },
                                    label = { Text("Shop / Building & Street Address") },
                                    placeholder = { Text("e.g. Shop #12, Ground Floor, Main Market") },
                                    modifier = Modifier.fillMaxWidth().testTag("admin_input_street_address")
                                )

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(
                                        value = area,
                                        onValueChange = { area = it },
                                        label = { Text("Area / Locality") },
                                        placeholder = { Text("e.g. Green Park") },
                                        modifier = Modifier.weight(1f).testTag("admin_input_area")
                                    )
                                    OutlinedTextField(
                                        value = city,
                                        onValueChange = { city = it },
                                        label = { Text("City") },
                                        placeholder = { Text("e.g. New Delhi") },
                                        modifier = Modifier.weight(1f).testTag("admin_input_city")
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(
                                        value = state,
                                        onValueChange = { state = it },
                                        label = { Text("State / Province") },
                                        placeholder = { Text("e.g. Delhi") },
                                        modifier = Modifier.weight(1f).testTag("admin_input_state")
                                    )
                                    OutlinedTextField(
                                        value = pincode,
                                        onValueChange = { pincode = it },
                                        label = { Text("Pincode / Postal Code") },
                                        placeholder = { Text("e.g. 110016") },
                                        modifier = Modifier.weight(1f).testTag("admin_input_pincode")
                                    )
                                }

                                OutlinedTextField(
                                    value = country,
                                    onValueChange = { country = it },
                                    label = { Text("Country") },
                                    placeholder = { Text("e.g. India") },
                                    modifier = Modifier.fillMaxWidth().testTag("admin_input_country")
                                )

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(
                                        value = latitude,
                                        onValueChange = { latitude = it },
                                        label = { Text("GPS Latitude") },
                                        placeholder = { Text("e.g. 28.5583") },
                                        modifier = Modifier.weight(1f).testTag("admin_input_latitude")
                                    )
                                    OutlinedTextField(
                                        value = longitude,
                                        onValueChange = { longitude = it },
                                        label = { Text("GPS Longitude") },
                                        placeholder = { Text("e.g. 77.2028") },
                                        modifier = Modifier.weight(1f).testTag("admin_input_longitude")
                                    )
                                }

                                Button(
                                    onClick = {
                                        promptConfirmation(
                                            title = "Save Salon Location & Map Pin",
                                            message = "Do you want to save and sync this location ($streetAddress, $city, $country)? It will be instantly updated across all customer dashboards, location cards, and navigation links."
                                        ) {
                                            viewModel.updateSalonLocation(
                                                streetAddress = streetAddress,
                                                area = area,
                                                city = city,
                                                state = state,
                                                country = country,
                                                pincode = pincode,
                                                latitude = latitude.toDoubleOrNull() ?: 28.5583,
                                                longitude = longitude.toDoubleOrNull() ?: 77.2028
                                            )
                                            Toast.makeText(context, "Salon location saved and synced across all customer devices!", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().testTag("admin_save_location_button"),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Save Salon Location")
                                }
                            }
                        }
                    }
                }

                // Real Interactive Map Picker Dialog
                if (showMapPickerSheet) {
                    val currentLat = latitude.toDoubleOrNull() ?: 28.5583
                    val currentLng = longitude.toDoubleOrNull() ?: 77.2028
                    RealMapPickerDialog(
                        initialLatitude = currentLat,
                        initialLongitude = currentLng,
                        initialStreetAddress = streetAddress,
                        initialCity = city,
                        onDismiss = { showMapPickerSheet = false },
                        onLocationConfirmed = { loc ->
                            latitude = String.format(java.util.Locale.US, "%.6f", loc.latitude)
                            longitude = String.format(java.util.Locale.US, "%.6f", loc.longitude)
                            if (loc.streetAddress.isNotBlank()) streetAddress = loc.streetAddress
                            if (loc.city.isNotBlank()) {
                                city = loc.city
                                area = if (loc.area.isNotBlank()) loc.area else loc.city
                            }
                            if (loc.state.isNotBlank()) state = loc.state
                            if (loc.country.isNotBlank()) country = loc.country
                            if (loc.pincode.isNotBlank()) pincode = loc.pincode
                            showMapPickerSheet = false

                            viewModel.updateSalonLocation(
                                streetAddress = streetAddress,
                                area = area,
                                city = city,
                                state = state,
                                country = country,
                                pincode = pincode,
                                latitude = loc.latitude,
                                longitude = loc.longitude
                            )
                            Toast.makeText(context, "Location updated from map and synced to Customer App!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            // SUBTAB 6: SECURITY & ADMIN CONTACTS
            6 -> {
                val context = LocalContext.current
                var newPassword by remember { mutableStateOf("") }
                var confirmPassword by remember { mutableStateOf("") }
                var passwordVisible by remember { mutableStateOf(false) }
                var selectedCurrency by remember(salonConfig) { mutableStateOf(salonConfig?.currencySymbol ?: "₹") }
                var customCurrency by remember { mutableStateOf("") }

                var adminGmailInput by remember(salonConfig) { mutableStateOf(salonConfig?.adminGmail ?: "admin.unisexsalon@gmail.com") }
                var adminPhoneInput by remember(salonConfig) { mutableStateOf(salonConfig?.adminPhone ?: "+91 98765 43210") }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    // Password Management Card
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admin_password_management_card"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primaryContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Admin Access Password",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Required when switching to Admin Mode from the app switcher bar",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                text = "Security Protection",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                            Text(
                                                text = "Password Gate Active",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = MaterialTheme.colorScheme.primary
                                        ) {
                                            Text(
                                                text = "Protected",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    val currentPass = salonConfig?.adminPassword ?: "UnisexSalon#Admin98"
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                text = "Current Active Admin Password:",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSecondaryContainer
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = currentPass,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as? android.content.ClipboardManager
                                                clipboard?.setPrimaryClip(android.content.ClipData.newPlainText("Admin Password", currentPass))
                                                AppFeedbackHelper.triggerSuccess(context)
                                                Toast.makeText(context, "Password copied to clipboard!", Toast.LENGTH_SHORT).show()
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ContentCopy,
                                                contentDescription = "Copy Password",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }

                                OutlinedTextField(
                                    value = newPassword,
                                    onValueChange = { newPassword = it },
                                    label = { Text("New Admin Password") },
                                    placeholder = { Text("Enter at least 4 characters") },
                                    singleLine = true,
                                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                    trailingIcon = {
                                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                            Icon(
                                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                                            )
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("admin_new_password_field")
                                )

                                OutlinedTextField(
                                    value = confirmPassword,
                                    onValueChange = { confirmPassword = it },
                                    label = { Text("Confirm New Password") },
                                    placeholder = { Text("Re-enter new password") },
                                    singleLine = true,
                                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                    isError = confirmPassword.isNotEmpty() && newPassword != confirmPassword,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("admin_confirm_password_field")
                                )

                                if (confirmPassword.isNotEmpty() && newPassword != confirmPassword) {
                                    Text(
                                        text = "Passwords do not match.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }

                                Button(
                                    onClick = {
                                        if (newPassword.trim().length >= 4 && newPassword == confirmPassword) {
                                            promptConfirmation(
                                                title = "Update Admin Password",
                                                message = "Are you sure you want to update the master admin password?"
                                            ) {
                                                viewModel.updateAdminPassword(newPassword.trim())
                                                Toast.makeText(context, "Admin password updated successfully!", Toast.LENGTH_SHORT).show()
                                                newPassword = ""
                                                confirmPassword = ""
                                            }
                                        }
                                    },
                                    enabled = newPassword.trim().length >= 4 && newPassword == confirmPassword,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("save_admin_password_button")
                                ) {
                                    Icon(imageVector = Icons.Default.Security, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Update Admin Password")
                                }

                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                                OutlinedButton(
                                    onClick = { viewModel.lockAdmin() },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("lock_admin_mode_ops_button"),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Lock Admin Mode Now (Return to Customer App)")
                                }
                            }
                        }
                    }

                    // Admin Mobile Device Auto-Login Card
                    item {
                        var isAdminDeviceRemembered by remember { mutableStateOf(viewModel.isAdminDeviceRemembered()) }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admin_device_persistence_card"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(SalonGoldPrimary.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PhoneAndroid,
                                            contentDescription = null,
                                            tint = SalonGoldPrimary,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Admin Device Auto-Login Memory",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Automatically remember this mobile phone for direct Admin launch",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Switch(
                                        checked = isAdminDeviceRemembered,
                                        onCheckedChange = { isChecked ->
                                            viewModel.setAdminDeviceRemembered(isChecked)
                                            isAdminDeviceRemembered = isChecked
                                            if (isChecked) {
                                                Toast.makeText(context, "This mobile is now saved as Admin device! App will open in Admin mode.", Toast.LENGTH_LONG).show()
                                            } else {
                                                Toast.makeText(context, "Device unlinked. App will start in standard Customer mode.", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isAdminDeviceRemembered) Color(0xFF064E3B).copy(alpha = 0.25f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isAdminDeviceRemembered) Color(0xFF34D399).copy(alpha = 0.6f) else MaterialTheme.colorScheme.outlineVariant
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isAdminDeviceRemembered) Icons.Default.CheckCircle else Icons.Default.Security,
                                            contentDescription = null,
                                            tint = if (isAdminDeviceRemembered) Color(0xFF34D399) else Color.Gray,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = if (isAdminDeviceRemembered) "Current Device: Admin Auto-Login Active" else "Current Device: Standard User Device",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = if (isAdminDeviceRemembered) Color(0xFF34D399) else MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = if (isAdminDeviceRemembered)
                                                    "When you open this app on this phone, it opens directly into Admin Mode without asking password. Other devices open customer mode."
                                                else
                                                    "This device is currently recognized as a standard customer device.",
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = "Device Model: ${android.os.Build.MANUFACTURER.replaceFirstChar { it.uppercase() }} ${android.os.Build.MODEL}",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    // Admin Notification Contacts Card (Gmail & Phone)
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admin_notification_contacts_card"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primaryContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Phone,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Admin Registered Contacts & Alerts",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Customer booking notifications & postponements will dispatch to this G-mail",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color(0xFFFEF3C7),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Send,
                                            contentDescription = null,
                                            tint = Color(0xFFB45309),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = "All customer appointments, status updates, and postpone events are mirrored directly to this registered admin G-mail.",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color(0xFF92400E)
                                        )
                                    }
                                }

                                OutlinedTextField(
                                    value = adminGmailInput,
                                    onValueChange = { adminGmailInput = it },
                                    label = { Text("Admin Official G-Mail Address") },
                                    placeholder = { Text("e.g. admin.salon@gmail.com") },
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("admin_registered_gmail_field")
                                )

                                OutlinedTextField(
                                    value = adminPhoneInput,
                                    onValueChange = { adminPhoneInput = it },
                                    label = { Text("Admin Official Phone Number") },
                                    placeholder = { Text("e.g. +91 98765 43210") },
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("admin_registered_phone_field")
                                )

                                Button(
                                    onClick = {
                                        promptConfirmation(
                                            title = "Save Admin Notification Contacts",
                                            message = "Do you want to update the admin registered Gmail to '$adminGmailInput' and phone to '$adminPhoneInput'? All new customer bookings and postponements will be mirrored to this email."
                                        ) {
                                            viewModel.updateAdminContactDetails(
                                                adminGmail = adminGmailInput,
                                                adminPhone = adminPhoneInput
                                            )
                                            Toast.makeText(context, "Admin notification contacts saved!", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("save_admin_contacts_button")
                                ) {
                                    Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Save Admin G-Mail & Phone")
                                }
                            }
                        }
                    }

                    // Currency Setting Card
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("currency_setting_card"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.secondaryContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = salonConfig?.currencySymbol ?: "₹",
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Salon Currency Setting",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Synchronized across all service prices, receipts & registers",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Text(
                                    text = "Quick Presets:",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf("₹" to "Rupee (₹)", "$" to "USD ($)", "€" to "EUR (€)", "£" to "GBP (£)").forEach { (symbol, label) ->
                                        FilterChip(
                                            selected = (salonConfig?.currencySymbol ?: "₹") == symbol,
                                            onClick = {
                                                promptConfirmation(
                                                    title = "Change Salon Currency",
                                                    message = "Switch global salon currency to $symbol across all prices?"
                                                ) {
                                                    selectedCurrency = symbol
                                                    viewModel.updateCurrencySymbol(symbol)
                                                }
                                            },
                                            label = { Text(label, fontWeight = FontWeight.SemiBold) },
                                            modifier = Modifier.testTag("currency_chip_$symbol")
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = customCurrency,
                                        onValueChange = { customCurrency = it },
                                        label = { Text("Custom Symbol") },
                                        placeholder = { Text("e.g. ₹ or Rs.") },
                                        singleLine = true,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Button(
                                        onClick = {
                                            if (customCurrency.isNotBlank()) {
                                                promptConfirmation(
                                                    title = "Change Salon Currency",
                                                    message = "Set custom salon currency to '$customCurrency'?"
                                                ) {
                                                    viewModel.updateCurrencySymbol(customCurrency.trim())
                                                    customCurrency = ""
                                                }
                                            }
                                        },
                                        enabled = customCurrency.isNotBlank(),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text("Apply")
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color(0xFFF0FDF4),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = Color(0xFF047857),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = "Active Currency: ${salonConfig?.currencySymbol ?: "₹"} — Live updates in Customer App instantly",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color(0xFF047857),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        }
    }

    // DIALOG: EDIT OPERATING HOURS
    editingWorkingDay?.let { day ->
        var tempOpenTime by remember(day) { mutableStateOf(day.openTime) }
        var tempCloseTime by remember(day) { mutableStateOf(day.closeTime) }
        var tempIsOpen by remember(day) { mutableStateOf(day.isOpen) }
        var applyToAll by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { editingWorkingDay = null },
            icon = { Icon(Icons.Default.Schedule, contentDescription = null, tint = SalonGoldPrimary) },
            title = { Text("Edit ${day.dayName} Hours", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Open on ${day.dayName}:", fontWeight = FontWeight.SemiBold)
                        Switch(checked = tempIsOpen, onCheckedChange = { tempIsOpen = it })
                    }

                    if (tempIsOpen) {
                        Text("Opens At:", style = MaterialTheme.typography.labelMedium, color = SalonGoldPrimary)
                        OutlinedTextField(
                            value = tempOpenTime,
                            onValueChange = { tempOpenTime = it },
                            placeholder = { Text("09:00") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(listOf("08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00")) { t ->
                                FilterChip(
                                    selected = tempOpenTime == t,
                                    onClick = { tempOpenTime = t },
                                    label = { Text(t, fontSize = 11.sp) }
                                )
                            }
                        }

                        Text("Closes At:", style = MaterialTheme.typography.labelMedium, color = SalonGoldPrimary)
                        OutlinedTextField(
                            value = tempCloseTime,
                            onValueChange = { tempCloseTime = it },
                            placeholder = { Text("19:00") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(listOf("18:00", "19:00", "20:00", "20:30", "21:00", "21:30", "22:00")) { t ->
                                FilterChip(
                                    selected = tempCloseTime == t,
                                    onClick = { tempCloseTime = t },
                                    label = { Text(t, fontSize = 11.sp) }
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().clickable { applyToAll = !applyToAll },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            androidx.compose.material3.Checkbox(
                                checked = applyToAll,
                                onCheckedChange = { applyToAll = it }
                            )
                            Text("Apply this timing ($tempOpenTime - $tempCloseTime) to all operating days", fontSize = 12.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (applyToAll && tempIsOpen) {
                            viewModel.applyHoursToAllDays(tempOpenTime, tempCloseTime)
                        } else {
                            viewModel.updateWorkingDayHours(day.dayOfWeek, tempIsOpen, tempOpenTime, tempCloseTime)
                        }
                        editingWorkingDay = null
                        Toast.makeText(context, "Operating hours saved!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SalonGoldPrimary, contentColor = Color.Black)
                ) {
                    Text("Save Hours", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { editingWorkingDay = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // DIALOG: PREVIEW & PHOTO CROP
    activeCropTarget?.let { cropTarget ->
        PhotoPreviewAndCropDialog(
            photoUrl = cropTarget.url,
            photoTitle = cropTarget.title,
            currentFit = cropTarget.currentFit,
            onDismiss = { activeCropTarget = null },
            onSaveFit = { newFit ->
                viewModel.updatePhotoFit(cropTarget.type, newFit)
                activeCropTarget = null
                Toast.makeText(context, "Saved visible area for ${cropTarget.title}!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // DIALOG: CREATE / EDIT SERVICE
    if (isCreatingService || serviceDialogItem != null) {
        val current = serviceDialogItem
        var sName by remember(current) { mutableStateOf(current?.name ?: "") }
        var sCat by remember(current) { mutableStateOf(current?.category ?: "Hair Cut") }
        var sDesc by remember(current) { mutableStateOf(current?.description ?: "") }
        var sDuration by remember(current) { mutableStateOf((current?.durationMinutes ?: 60).toString()) }
        var sPrice by remember(current) { mutableStateOf((current?.price ?: 95.0).toString()) }
        var sImageUrl by remember(current) { mutableStateOf(current?.imageUrl ?: "") }

        val photoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            if (uri != null) {
                sImageUrl = uri.toString()
            }
        }

        Dialog(
            onDismissRequest = {
                isCreatingService = false
                serviceDialogItem = null
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.75f))
                    .imePadding()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 520.dp)
                        .heightIn(max = 700.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Title bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (current != null) "Edit Service" else "New Salon Service",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(
                                onClick = {
                                    isCreatingService = false
                                    serviceDialogItem = null
                                }
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Close")
                            }
                        }

                        Text("Quick Presets / Suggestions:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(listOf(
                                Triple("Classic Cut", "Hair Cut", "https://images.unsplash.com/photo-1599351431202-1e0f0137899a?auto=format&fit=crop&w=600&q=80") to ("Signature Haircut & Styling" to ("45" to "350")),
                                Triple("Balayage Colour", "Colour & Highlights", "https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?auto=format&fit=crop&w=600&q=80") to ("Balayage & Gloss" to ("120" to "2500")),
                                Triple("Royal Beard", "Beard Grooming", "https://images.unsplash.com/photo-1503951914875-452162b0f3f1?auto=format&fit=crop&w=600&q=80") to ("Royal Beard Grooming" to ("30" to "400")),
                                Triple("Hydra Facial", "Facial", "https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?auto=format&fit=crop&w=600&q=80") to ("Hydra-Luxe Facial" to ("60" to "1200")),
                                Triple("Glam Makeup", "Makeup", "https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?auto=format&fit=crop&w=600&q=80") to ("Signature Occasion Makeup" to ("90" to "3500"))
                            )) { (meta, details) ->
                                FilterChip(
                                    selected = sName == details.first,
                                    onClick = {
                                        sName = details.first
                                        sCat = meta.second
                                        sImageUrl = meta.third
                                        sDuration = details.second.first
                                        sPrice = details.second.second
                                        sDesc = "High-end bespoke unisex service with premium botanical products"
                                    },
                                    label = { Text(meta.first, style = MaterialTheme.typography.labelSmall) }
                                )
                            }
                        }

                        OutlinedTextField(
                            value = sName,
                            onValueChange = { sName = it },
                            label = { Text("Service Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = sCat,
                            onValueChange = { sCat = it },
                            label = { Text("Category (Hair Cut, Colour, Facial, Beard)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = sDesc,
                            onValueChange = { sDesc = it },
                            label = { Text("Description") },
                            maxLines = 2,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Duration and Price in clearly readable side-by-side fields
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = sDuration,
                                onValueChange = { sDuration = it },
                                label = { Text("Duration (mins)") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = sPrice,
                                onValueChange = { sPrice = it },
                                label = { Text("Price (" + (salonConfig?.currencySymbol ?: "₹") + ")") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Done
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Photo from Media Picker (Replaces URL system)
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Service Photo",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            if (sImageUrl.isNotBlank()) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(140.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        AsyncImage(
                                            model = sImageUrl,
                                            contentDescription = "Service Image Preview",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                        )
                                        Row(
                                            modifier = Modifier
                                                .align(Alignment.BottomEnd)
                                                .padding(8.dp),
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Button(
                                                onClick = {
                                                    photoPickerLauncher.launch(
                                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                                    )
                                                },
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Change", style = MaterialTheme.typography.labelSmall)
                                            }
                                            IconButton(
                                                onClick = { sImageUrl = "" },
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                            ) {
                                                Icon(Icons.Default.Delete, contentDescription = "Remove photo", tint = Color.White, modifier = Modifier.size(18.dp))
                                            }
                                        }
                                    }
                                }
                            } else {
                                Surface(
                                    onClick = {
                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PhotoLibrary,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = "Add Photo from Gallery / Device",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Text(
                                                text = "Select photo directly from your media storage",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    isCreatingService = false
                                    serviceDialogItem = null
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Cancel")
                            }
                            Button(
                                onClick = {
                                    val dur = sDuration.toIntOrNull() ?: 60
                                    val prc = sPrice.toDoubleOrNull() ?: 50.0
                                    val entity = ServiceEntity(
                                        id = current?.id ?: 0L,
                                        name = sName.ifBlank { "Salon Service" },
                                        category = sCat.ifBlank { "Hair Cut" },
                                        description = sDesc,
                                        durationMinutes = dur,
                                        price = prc,
                                        isActive = true,
                                        imageUrl = sImageUrl
                                    )
                                    viewModel.saveService(entity)
                                    isCreatingService = false
                                    serviceDialogItem = null
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Save Service")
                            }
                        }
                    }
                }
            }
        }
    }

    // DIALOG: CREATE / EDIT STAFF
    if (isCreatingStaff || staffDialogItem != null) {
        val current = staffDialogItem
        var stName by remember(current) { mutableStateOf(current?.name ?: "") }
        var stTitle by remember(current) { mutableStateOf(current?.roleTitle ?: "") }
        var stSpec by remember(current) { mutableStateOf(current?.specialty ?: "") }

        AlertDialog(
            onDismissRequest = {
                isCreatingStaff = false
                staffDialogItem = null
            },
            title = { Text(if (current != null) "Edit Specialist" else "Add Salon Staff") },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(value = stName, onValueChange = { stName = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = stTitle, onValueChange = { stTitle = it }, label = { Text("Title (e.g. Master Stylist)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = stSpec, onValueChange = { stSpec = it }, label = { Text("Specialty") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val entity = StaffEntity(
                            id = current?.id ?: 0L,
                            name = stName,
                            roleTitle = stTitle,
                            specialty = stSpec,
                            rating = current?.rating ?: 4.95f,
                            isActive = true
                        )
                        viewModel.saveStaff(entity)
                        isCreatingStaff = false
                        staffDialogItem = null
                    }
                ) {
                    Text("Save Specialist")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    isCreatingStaff = false
                    staffDialogItem = null
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    // DIALOG: ADD BREAK
    if (breakDialogItem) {
        var bTitle by remember { mutableStateOf("Lunch & Prep") }
        var bStart by remember { mutableStateOf("13:00") }
        var bEnd by remember { mutableStateOf("14:00") }

        AlertDialog(
            onDismissRequest = { breakDialogItem = false },
            title = { Text("Add Salon Break") },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(value = bTitle, onValueChange = { bTitle = it }, label = { Text("Break Title") })
                    OutlinedTextField(value = bStart, onValueChange = { bStart = it }, label = { Text("Start Time (HH:mm)") })
                    OutlinedTextField(value = bEnd, onValueChange = { bEnd = it }, label = { Text("End Time (HH:mm)") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.addSalonBreak(bTitle, bStart, bEnd)
                    breakDialogItem = false
                }) {
                    Text("Add Break")
                }
            },
            dismissButton = {
                TextButton(onClick = { breakDialogItem = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // DIALOG: ADD HOLIDAY
    if (holidayDialogItem) {
        var hDate by remember { mutableStateOf("") }
        var hName by remember { mutableStateOf("") }
        var hReason by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { holidayDialogItem = false },
            title = { Text("Add Salon Holiday / Closed Day") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = hDate, onValueChange = { hDate = it }, label = { Text("Date (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = hName, onValueChange = { hName = it }, label = { Text("Holiday Name") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = hReason, onValueChange = { hReason = it }, label = { Text("Reason / Notice") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (hDate.isNotBlank() && hName.isNotBlank()) {
                        viewModel.addSalonHoliday(hDate.trim(), hName.trim(), hReason.trim())
                        holidayDialogItem = false
                    }
                }) {
                    Text("Add Holiday")
                }
            },
            dismissButton = {
                TextButton(onClick = { holidayDialogItem = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // DIALOG: CREATE / EDIT FACILITY
    if (isCreatingAmenity || amenityDialogItem != null) {
        val currentAmenity = amenityDialogItem
        var aName by remember(currentAmenity) { mutableStateOf(currentAmenity?.name ?: "") }
        var aDesc by remember(currentAmenity) { mutableStateOf(currentAmenity?.description ?: "") }
        var aIcon by remember(currentAmenity) { mutableStateOf(currentAmenity?.iconKey ?: "AC") }

        AlertDialog(
            onDismissRequest = {
                isCreatingAmenity = false
                amenityDialogItem = null
            },
            title = { Text(if (currentAmenity != null) "Edit Salon Facility" else "Add Salon Facility") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Facility Name (e.g., Central AC, High-Speed WiFi, Valet Parking):",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    OutlinedTextField(
                        value = aName,
                        onValueChange = { aName = it },
                        label = { Text("Facility Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = aDesc,
                        onValueChange = { aDesc = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "Choose Facility Icon Category:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                    val iconOptions = listOf("AC", "WIFI", "PARKING", "COFFEE", "LOUNGE", "MUSIC", "SPA")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(iconOptions) { opt ->
                            FilterChip(
                                selected = aIcon == opt,
                                onClick = { aIcon = opt },
                                label = { Text(opt) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (aName.isNotBlank()) {
                            if (currentAmenity != null) {
                                viewModel.updateAmenity(
                                    currentAmenity.copy(
                                        name = aName.trim(),
                                        description = aDesc.trim(),
                                        iconKey = aIcon
                                    )
                                )
                            } else {
                                viewModel.addAmenity(
                                    name = aName.trim(),
                                    description = aDesc.trim(),
                                    iconKey = aIcon
                                )
                            }
                            isCreatingAmenity = false
                            amenityDialogItem = null
                        }
                    }
                ) {
                    Text(if (currentAmenity != null) "Save Changes" else "Add Facility")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isCreatingAmenity = false
                        amenityDialogItem = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

// -------------------------------------------------------------
// TAB 3: ADMIN USERS & SUPPORT MANAGEMENT
// -------------------------------------------------------------
@Composable
fun AdminUsersSupportScreen(viewModel: SalonViewModel) {
    var subTab by remember { mutableIntStateOf(0) } // 0: Support Inbox, 1: Customers Directory

    val allUsers by viewModel.allUsers.collectAsState()
    val allMessages by viewModel.allSupportMessages.collectAsState()
    val allBookings by viewModel.allBookings.collectAsState()

    var activeChatCustomerId by remember { mutableStateOf<String?>(null) }
    var activeChatCustomerName by remember { mutableStateOf("") }
    var replyText by remember { mutableStateOf("") }

    val customers = remember(allUsers) {
        allUsers.filter { it.role == com.example.data.model.UserRole.CUSTOMER }
    }

    // Group messages by customer
    val tickets = remember(allMessages) {
        allMessages.groupBy { it.ticketCustomerId }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Users & Support Management",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Respond live to guest inquiries and review customer directory",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        TabRow(selectedTabIndex = subTab) {
            Tab(selected = subTab == 0, onClick = { subTab = 0 }, text = { Text("Support Inbox (${tickets.size})") })
            Tab(selected = subTab == 1, onClick = { subTab = 1 }, text = { Text("Customer Directory (${customers.size})") })
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (subTab) {
            // SUPPORT INBOX
            0 -> {
                if (tickets.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No active support inquiries.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(tickets.keys.toList()) { custId ->
                            val msgList = tickets[custId] ?: emptyList()
                            val lastMsg = msgList.lastOrNull()
                            val custName = lastMsg?.customerName ?: "Customer"

                            ElevatedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        activeChatCustomerId = custId
                                        activeChatCustomerName = custName
                                        replyText = ""
                                    },
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primaryContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = custName.take(1),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(custName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                            Text(
                                                text = "${msgList.size} msgs",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(2.dp))

                                        Text(
                                            text = lastMsg?.message ?: "",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            activeChatCustomerId = custId
                                            activeChatCustomerName = custName
                                            replyText = ""
                                        },
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("Reply", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // CUSTOMER DIRECTORY
            1 -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(customers) { cust ->
                        val custBookings = allBookings.filter { it.customerId == cust.id }

                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.secondaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = cust.avatarInitial,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(cust.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                    Text("${cust.phone} • ${cust.email}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${custBookings.size} Appointments Booked",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // DIALOG: REPLY TO SUPPORT CHAT
    activeChatCustomerId?.let { custId ->
        val conversation = allMessages.filter { it.ticketCustomerId == custId }

        AlertDialog(
            onDismissRequest = { activeChatCustomerId = null },
            title = { Text("Support Chat with $activeChatCustomerName") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(conversation) { msg ->
                            val isAdmin = msg.senderRole == com.example.data.model.UserRole.ADMIN
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = if (isAdmin) Arrangement.End else Arrangement.Start
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isAdmin) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.widthIn(max = 240.dp)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text(
                                            text = if (isAdmin) "You (Admin)" else msg.senderName,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isAdmin) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f) else MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = msg.message,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = if (isAdmin) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedTextField(
                            value = replyText,
                            onValueChange = { replyText = it },
                            placeholder = { Text("Type reply to guest...") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        IconButton(
                            onClick = {
                                if (replyText.isNotBlank()) {
                                    viewModel.sendAdminSupportReply(custId, activeChatCustomerName, replyText)
                                    replyText = ""
                                }
                            },
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(imageVector = Icons.Default.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { activeChatCustomerId = null }) {
                    Text("Close")
                }
            }
        )
    }
}
