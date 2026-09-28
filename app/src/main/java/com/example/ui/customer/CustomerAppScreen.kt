package com.example.ui.customer

import android.widget.Toast
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import com.example.ui.auth.AuthModalSheet
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import com.example.util.AppFeedbackHelper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
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
import com.example.data.model.UserRole
import com.example.ui.components.AdminDiscreetCornerButton
import com.example.ui.components.AmenityItem
import com.example.ui.components.CashPaymentBadge
import com.example.ui.components.PromotionalLuxuryCardsRow
import com.example.ui.components.QuickBookSection
import com.example.ui.components.SalonAmenitiesRow
import com.example.ui.components.SalonHeroBannerCard
import com.example.ui.components.SalonLocationCard
import com.example.ui.components.SalonStorefrontShowcaseCard
import com.example.ui.components.StatusBadge
import com.example.ui.components.UnisexPromoBannerRow
import com.example.ui.components.UnisexSalonTopBar
import com.example.ui.theme.DarkBorderColor
import com.example.ui.theme.DarkCanvasBackground
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceCardElevated
import com.example.ui.theme.PlayfairDisplayFontFamily
import com.example.ui.theme.PlusJakartaSansFontFamily
import com.example.ui.theme.SalonGoldPill
import com.example.ui.theme.SalonGoldPrimary
import com.example.ui.theme.SalonReviewStar
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import android.content.Intent
import android.net.Uri
import com.example.viewmodel.BookingWizardState
import com.example.viewmodel.SalonViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun CustomerAppScreen(
    viewModel: SalonViewModel,
    onOpenAdmin: () -> Unit = {},
    onOpenNotifications: () -> Unit = {},
    unreadNotifications: Int = 0,
    modifier: Modifier = Modifier
) {
    var selectedNavTab by remember { mutableIntStateOf(0) }
    val wizardState by viewModel.wizardState.collectAsState()
    val salonConfig by viewModel.salonConfig.collectAsState()
    val workingDays by viewModel.workingDays.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val context = LocalContext.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkCanvasBackground,
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("customer_bottom_nav"),
                containerColor = DarkSurfaceCard,
                tonalElevation = 8.dp
            ) {
                val navItemColors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SalonGoldPrimary,
                    selectedTextColor = SalonGoldPrimary,
                    indicatorColor = SalonGoldPrimary.copy(alpha = 0.2f),
                    unselectedIconColor = Color(0xFF9CA3AF),
                    unselectedTextColor = Color(0xFF9CA3AF)
                )

                NavigationBarItem(
                    selected = selectedNavTab == 0,
                    onClick = {
                        if (selectedNavTab != 0) {
                            AppFeedbackHelper.triggerClick(context)
                            selectedNavTab = 0
                        }
                    },
                    icon = { Icon(Icons.Default.Spa, contentDescription = null) },
                    label = { Text("Services", fontFamily = PlusJakartaSansFontFamily, fontSize = 11.sp, fontWeight = if (selectedNavTab == 0) FontWeight.Bold else FontWeight.Normal) },
                    colors = navItemColors,
                    modifier = Modifier.testTag("customer_nav_services")
                )
                NavigationBarItem(
                    selected = selectedNavTab == 1,
                    onClick = {
                        if (selectedNavTab != 1) {
                            AppFeedbackHelper.triggerClick(context)
                            selectedNavTab = 1
                        }
                    },
                    icon = { Icon(Icons.Default.EventNote, contentDescription = null) },
                    label = { Text("My Bookings", fontFamily = PlusJakartaSansFontFamily, fontSize = 11.sp, fontWeight = if (selectedNavTab == 1) FontWeight.Bold else FontWeight.Normal) },
                    colors = navItemColors,
                    modifier = Modifier.testTag("customer_nav_bookings")
                )
                NavigationBarItem(
                    selected = selectedNavTab == 2,
                    onClick = {
                        if (selectedNavTab != 2) {
                            AppFeedbackHelper.triggerClick(context)
                            selectedNavTab = 2
                        }
                    },
                    icon = { Icon(Icons.Default.QuestionAnswer, contentDescription = null) },
                    label = { Text("Support", fontFamily = PlusJakartaSansFontFamily, fontSize = 11.sp, fontWeight = if (selectedNavTab == 2) FontWeight.Bold else FontWeight.Normal) },
                    colors = navItemColors,
                    modifier = Modifier.testTag("customer_nav_support")
                )
                NavigationBarItem(
                    selected = selectedNavTab == 3,
                    onClick = {
                        if (selectedNavTab != 3) {
                            AppFeedbackHelper.triggerClick(context)
                            selectedNavTab = 3
                        }
                    },
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                    label = { Text("Profile", fontFamily = PlusJakartaSansFontFamily, fontSize = 11.sp, fontWeight = if (selectedNavTab == 3) FontWeight.Bold else FontWeight.Normal) },
                    colors = navItemColors,
                    modifier = Modifier.testTag("customer_nav_profile")
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
                0 -> CustomerServicesScreen(
                    viewModel = viewModel,
                    salonConfig = salonConfig,
                    workingDays = workingDays,
                    onOpenAdmin = onOpenAdmin,
                    onOpenNotifications = onOpenNotifications,
                    unreadNotifications = unreadNotifications,
                    onBookService = { service ->
                        AppFeedbackHelper.triggerNotification(context)
                        viewModel.startBookingFlow(service)
                    }
                )
                1 -> CustomerBookingsScreen(
                    viewModel = viewModel,
                    onBookNewClick = {
                        AppFeedbackHelper.triggerClick(context)
                        selectedNavTab = 0
                        viewModel.startBookingFlow()
                    }
                )
                2 -> CustomerSupportScreen(viewModel = viewModel)
                3 -> CustomerProfileScreen(viewModel = viewModel)
            }

            // 6-Step Booking Wizard Dialog / Flow
            if (wizardState.isOpen) {
                BookingWizardSheet(
                    wizardState = wizardState,
                    activeStaff = viewModel.activeStaff.collectAsState().value,
                    workingDays = workingDays,
                    holidays = viewModel.holidays.collectAsState().value,
                    currentUser = currentUser,
                    salonConfig = salonConfig,
                    onClose = { viewModel.closeBookingFlow() },
                    onSetStep = { step -> viewModel.setWizardStep(step) },
                    onSelectService = { service -> viewModel.selectBookingService(service) },
                    onSelectDate = { dateStr -> viewModel.selectBookingDate(dateStr) },
                    onSelectStaff = { staff -> viewModel.selectBookingStaff(staff) },
                    onSelectSlot = { slot -> viewModel.selectBookingSlot(slot) },
                    onCheckCustomSlot = { time, callback ->
                        viewModel.checkCustomTimeSlotAvailability(
                            date = wizardState.date,
                            time = time,
                            staffId = wizardState.staff?.id,
                            durationMins = wizardState.service?.durationMinutes ?: 60,
                            onResult = callback
                        )
                    },
                    onUpdateNotes = { notes -> viewModel.updateCustomerNotes(notes) },
                    onSubmitBooking = {
                        AppFeedbackHelper.triggerSuccess(context)
                        viewModel.submitBookingConfirmation()
                    },
                    onViewBookings = {
                        AppFeedbackHelper.triggerClick(context)
                        viewModel.closeBookingFlow()
                        selectedNavTab = 1 // Switch to My Bookings
                    }
                )
            }
        }
    }
}

// SCREEN 1: SERVICES & BOOK
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerServicesScreen(
    viewModel: SalonViewModel,
    salonConfig: SalonConfigEntity?,
    workingDays: List<SalonWorkingDayEntity>,
    onOpenAdmin: () -> Unit = {},
    onOpenNotifications: () -> Unit = {},
    unreadNotifications: Int = 0,
    onBookService: (ServiceEntity) -> Unit
) {
    val services by viewModel.activeServices.collectAsState()
    val activeAmenities by viewModel.activeAmenities.collectAsState()
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var showSalonDetailsSheet by remember { mutableStateOf(false) }
    var selectedAmenityModal by remember { mutableStateOf<AmenityItem?>(null) }
    val context = LocalContext.current

    val onGetDirections: () -> Unit = {
        val address = salonConfig?.address ?: "123 Wellness Street, Green Park, Delhi"
        val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(address)}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        try {
            context.startActivity(mapIntent)
        } catch (e: Exception) {
            showSalonDetailsSheet = true
        }
    }

    val categories = remember(services) {
        listOf("All") + services.map { it.category }.distinct()
    }

    val filteredServices = remember(services, selectedCategory, searchQuery) {
        services.filter { service ->
            val matchesCategory = (selectedCategory == "All" || service.category.equals(selectedCategory, ignoreCase = true))
            val matchesQuery = searchQuery.isBlank() ||
                    service.name.contains(searchQuery, ignoreCase = true) ||
                    service.description.contains(searchQuery, ignoreCase = true) ||
                    service.category.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkCanvasBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 4.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Luxury Top Brand Header: Hamburger Menu + UNISEX SALON + Notifications + Admin lock
        item {
            UnisexSalonTopBar(
                unreadCount = unreadNotifications,
                onMenuClick = { showSalonDetailsSheet = true },
                onNotificationClick = onOpenNotifications,
                onAdminClick = onOpenAdmin,
                salonConfig = salonConfig
            )
        }

        // Luxury Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("services_search_input"),
                placeholder = {
                    Text(
                        text = "Search hair, beard, facial, spa...",
                        fontFamily = PlusJakartaSansFontFamily,
                        fontSize = 13.sp,
                        color = Color(0xFF888888)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = SalonGoldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = Color(0xFFA0A0A0),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DarkSurfaceCard,
                    unfocusedContainerColor = DarkSurfaceCard,
                    focusedBorderColor = SalonGoldPrimary,
                    unfocusedBorderColor = DarkBorderColor,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = SalonGoldPrimary
                )
            )
        }

        // Hero Banner Carousel: "Premium Care for Everyone" with indicators & pill CTA
        item {
            SalonHeroBannerCard(
                salonConfig = salonConfig,
                workingDays = workingDays,
                onBookClick = { viewModel.startBookingFlow() }
            )
        }

        // Salon Location Card: "Our Salon • 123 Wellness Street, Green Park, Delhi"
        item {
            SalonLocationCard(
                salonConfig = salonConfig,
                onDirectionsClick = onGetDirections,
                onExpandClick = { showSalonDetailsSheet = true }
            )
        }

        // Salon VIP Amenities Row: AC, Wi-Fi, Parking, Hygiene, Lounge
        if (activeAmenities.isNotEmpty()) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "SALON AMENITIES",
                        fontFamily = PlusJakartaSansFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        letterSpacing = 1.2.sp,
                        color = Color(0xFF9CA3AF)
                    )
                    SalonAmenitiesRow(
                        activeAmenities = activeAmenities,
                        onAmenityClick = { amenity ->
                            selectedAmenityModal = amenity
                        }
                    )
                }
            }
        }

        // Quick Book Carousel: Most popular services
        if (services.isNotEmpty()) {
            item {
                QuickBookSection(
                    services = services,
                    onServiceClick = { service -> onBookService(service) },
                    onViewAllClick = {
                        selectedCategory = "All"
                        searchQuery = ""
                    }
                )
            }
        }

        // Promotional Luxury Banners: "Be Your Best Self" & "Luxury Care for Every You"
        item {
            PromotionalLuxuryCardsRow(
                salonConfig = salonConfig,
                onExploreClick = {
                    selectedCategory = categories.firstOrNull { it.contains("Beard", ignoreCase = true) || it.contains("Hair", ignoreCase = true) } ?: "All"
                },
                onBookClick = {
                    selectedCategory = categories.firstOrNull { it.contains("Facial", ignoreCase = true) || it.contains("Skin", ignoreCase = true) } ?: "All"
                }
            )
        }

        // Storefront Showcase Card
        item {
            SalonStorefrontShowcaseCard(
                salonConfig = salonConfig,
                onDirectionsClick = onGetDirections
            )
        }

        // Curated Salon Services Header & Chips
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Curated Salon Services",
                            fontFamily = PlayfairDisplayFontFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Choose from our premium unisex treatments",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontSize = 11.sp,
                            color = Color(0xFFA0A0A0)
                        )
                    }

                    CashPaymentBadge()
                }

                // Horizontal Category Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        val isSelected = selectedCategory == category
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) SalonGoldPill else DarkSurfaceCard,
                            border = androidx.compose.foundation.BorderStroke(
                                0.5.dp,
                                if (isSelected) SalonGoldPrimary else DarkBorderColor
                            ),
                            modifier = Modifier
                                .clickable { selectedCategory = category }
                                .testTag("filter_chip_$category")
                        ) {
                            Text(
                                text = category,
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp,
                                color = if (isSelected) Color(0xFF141414) else Color(0xFFD1D5DB),
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                            )
                        }
                    }
                }
            }
        }

        // Service Items List
        if (filteredServices.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, DarkBorderColor)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No services found",
                            fontFamily = PlayfairDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Try adjusting your search or category filter",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontSize = 12.sp,
                            color = Color(0xFFA0A0A0)
                        )
                    }
                }
            }
        } else {
            items(filteredServices) { service ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("service_card_${service.id}"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, DarkBorderColor)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            if (service.imageUrl.isNotBlank()) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(service.imageUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = service.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(74.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .border(0.5.dp, DarkBorderColor, RoundedCornerShape(14.dp)),
                                    placeholder = painterResource(id = R.drawable.salon_hero_banner),
                                    error = painterResource(id = R.drawable.salon_hero_banner)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = DarkSurfaceCardElevated,
                                    border = androidx.compose.foundation.BorderStroke(0.5.dp, DarkBorderColor)
                                ) {
                                    Text(
                                        text = service.category,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        fontFamily = PlusJakartaSansFontFamily,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SalonGoldPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = service.name,
                                    fontFamily = PlusJakartaSansFontFamily,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = (salonConfig?.currencySymbol ?: "₹ ") +
                                        (if (service.price % 1.0 == 0.0) String.format(Locale.US, "%.0f", service.price) else String.format(Locale.US, "%.2f", service.price)),
                                fontFamily = PlusJakartaSansFontFamily,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = SalonGoldPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = service.description,
                            fontFamily = PlusJakartaSansFontFamily,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFA0A0A0)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = Color(0xFFA0A0A0),
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "${service.durationMinutes} mins",
                                    fontFamily = PlusJakartaSansFontFamily,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color(0xFFA0A0A0)
                                )
                            }

                            Button(
                                onClick = { onBookService(service) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SalonGoldPill,
                                    contentColor = Color(0xFF141414)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                modifier = Modifier.testTag("book_service_button_${service.id}")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "Book",
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Interactive Amenity Detail Dialog
    selectedAmenityModal?.let { amenity ->
        AlertDialog(
            onDismissRequest = { selectedAmenityModal = null },
            icon = {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(DarkSurfaceCardElevated)
                        .border(1.dp, SalonGoldPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = amenity.icon,
                        contentDescription = amenity.label,
                        tint = SalonGoldPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "${amenity.label} Experience",
                    fontFamily = PlayfairDisplayFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = amenity.description,
                        fontFamily = PlusJakartaSansFontFamily,
                        fontSize = 13.sp,
                        color = Color(0xFFD1D5DB),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Included complimentary with all appointments at Unisex Salon.",
                        fontFamily = PlusJakartaSansFontFamily,
                        fontSize = 11.sp,
                        color = SalonGoldPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { selectedAmenityModal = null }
                ) {
                    Text(
                        text = "Got It",
                        fontFamily = PlusJakartaSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = SalonGoldPrimary
                    )
                }
            },
            containerColor = DarkSurfaceCard,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Salon Details & Directions Bottom Sheet
    if (showSalonDetailsSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showSalonDetailsSheet = false },
            sheetState = sheetState,
            containerColor = DarkSurfaceCard,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .width(40.dp)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4B5563))
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = salonConfig?.salonName ?: "UNISEX SALON",
                                fontFamily = PlayfairDisplayFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.White
                            )
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified",
                                tint = SalonGoldPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = salonConfig?.tagline?.takeIf { it.isNotBlank() } ?: "PREMIUM CARE FOR EVERYONE",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.sp,
                            letterSpacing = 1.2.sp,
                            color = SalonGoldPrimary
                        )
                    }

                    IconButton(onClick = { showSalonDetailsSheet = false }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color(0xFFA0A0A0)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Storefront photo
                val storefrontPhoto = salonConfig?.storefrontImageUrl?.takeIf { it.isNotBlank() }
                if (storefrontPhoto != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(storefrontPhoto)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Salon Exterior",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = com.example.ui.components.PhotoFitUtils.getContentScaleForFit(salonConfig?.storefrontImageFit ?: "CROP_CENTER"),
                        alignment = com.example.ui.components.PhotoFitUtils.getAlignmentForFit(salonConfig?.storefrontImageFit ?: "CROP_CENTER"),
                        placeholder = painterResource(id = R.drawable.img_salon_storefront),
                        error = painterResource(id = R.drawable.img_salon_storefront)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.img_salon_storefront),
                        contentDescription = "Salon Exterior",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Address & Directions Button
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCardElevated),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, DarkBorderColor)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = SalonGoldPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Salon Location & Address",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))

                        val street = salonConfig?.streetAddress?.takeIf { it.isNotBlank() }
                        val area = salonConfig?.area?.takeIf { it.isNotBlank() }
                        val city = salonConfig?.city?.takeIf { it.isNotBlank() }
                        val state = salonConfig?.state?.takeIf { it.isNotBlank() }
                        val country = salonConfig?.country?.takeIf { it.isNotBlank() }
                        val pincode = salonConfig?.pincode?.takeIf { it.isNotBlank() }

                        if (street != null || city != null) {
                            Text(
                                text = listOfNotNull(street, area).joinToString(", "),
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                            Text(
                                text = listOfNotNull(city, state, pincode, country).joinToString(", "),
                                fontFamily = PlusJakartaSansFontFamily,
                                fontSize = 12.sp,
                                color = Color(0xFFD1D5DB)
                            )
                        } else {
                            Text(
                                text = salonConfig?.address ?: "123 Wellness Street, Green Park, Delhi",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontSize = 12.sp,
                                color = Color(0xFFD1D5DB)
                            )
                        }

                        if (salonConfig?.latitude != null && salonConfig?.latitude != 0.0) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "GPS Coordinates: ${String.format(Locale.US, "%.4f", salonConfig!!.latitude)}° N, ${String.format(Locale.US, "%.4f", salonConfig!!.longitude)}° E",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontSize = 11.sp,
                                color = SalonGoldPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = onGetDirections,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SalonGoldPill,
                                contentColor = Color(0xFF141414)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.NearMe,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Open in Maps / Get Directions",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Weekly Schedule
                Text(
                    text = "Weekly Schedule",
                    fontFamily = PlusJakartaSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(6.dp))

                val dayNames = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
                val currentCalDay = remember {
                    val cal = Calendar.getInstance()
                    when (cal.get(Calendar.DAY_OF_WEEK)) {
                        Calendar.MONDAY -> 1
                        Calendar.TUESDAY -> 2
                        Calendar.WEDNESDAY -> 3
                        Calendar.THURSDAY -> 4
                        Calendar.FRIDAY -> 5
                        Calendar.SATURDAY -> 6
                        Calendar.SUNDAY -> 7
                        else -> 1
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurfaceCardElevated)
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    (1..7).forEach { dayNum ->
                        val daySchedule = workingDays.find { it.dayOfWeek == dayNum }
                        val isToday = dayNum == currentCalDay
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                if (isToday) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF34D399))
                                    )
                                }
                                Text(
                                    text = dayNames[dayNum - 1],
                                    fontFamily = PlusJakartaSansFontFamily,
                                    fontSize = 11.sp,
                                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isToday) SalonGoldPrimary else Color(0xFFD1D5DB)
                                )
                            }
                            Text(
                                text = if (daySchedule?.isOpen == true) "${daySchedule.openTime} - ${daySchedule.closeTime}" else "Closed",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontSize = 11.sp,
                                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                color = if (isToday) Color(0xFF34D399) else Color(0xFFA0A0A0)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Cash Payment Assurance
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x22FEF3C7))
                        .border(0.5.dp, Color(0x66F59E0B), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = null,
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(20.dp)
                    )
                    Column {
                        Text(
                            text = "Cash on Arrival Accepted",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color(0xFFFCD34D)
                        )
                        Text(
                            text = "Book without paying online. Pay conveniently at the counter after your appointment.",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontSize = 11.sp,
                            color = Color(0xFFD1D5DB)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

// SCREEN 2: MY BOOKINGS
@Composable
fun CustomerBookingsScreen(
    viewModel: SalonViewModel,
    onBookNewClick: () -> Unit
) {
    val bookings by viewModel.customerBookings.collectAsState()
    val salonConfig by viewModel.salonConfig.collectAsState()
    var selectedFilter by remember { mutableStateOf("All") }
    var cancelConfirmationBooking by remember { mutableStateOf<BookingEntity?>(null) }

    val filteredBookings = remember(bookings, selectedFilter) {
        when (selectedFilter) {
            "Active" -> bookings.filter {
                it.status == BookingStatus.PENDING || it.status == BookingStatus.CONFIRMED || it.status == BookingStatus.IN_PROGRESS
            }
            "Past" -> bookings.filter {
                it.status == BookingStatus.COMPLETED || it.status == BookingStatus.REJECTED || it.status == BookingStatus.CANCELLED
            }
            else -> bookings
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "My Appointments",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Track your salon bookings & cash receipts",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(
                onClick = onBookNewClick,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("book_new_appointment_button")
            ) {
                Text("+ New Booking")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Pills
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("All", "Active", "Past").forEach { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) }
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Text(
                        text = "No appointments found",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Browse our luxury services and schedule your visit.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
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
                            .testTag("customer_booking_card_${booking.referenceNumber}"),
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
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = booking.referenceNumber,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                StatusBadge(status = booking.status)
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = booking.serviceName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Date, Time, Staff Details
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = booking.bookingDate,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "${booking.startTime} - ${booking.endTime}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Stylist: ${booking.staffName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            if (booking.isPostponed || booking.status == BookingStatus.POSTPONED) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color(0x33F59E0B),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF59E0B)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = "⚠️ Appointment Postponed",
                                                fontFamily = PlusJakartaSansFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = Color(0xFFFCD34D)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = "New Time: ${booking.postponedNewDate.ifEmpty { booking.bookingDate }} at ${booking.postponedNewTime.ifEmpty { booking.startTime }}",
                                            fontFamily = PlusJakartaSansFontFamily,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 11.sp,
                                            color = Color.White
                                        )
                                        if (booking.postponeReason.isNotBlank()) {
                                            Text(
                                                text = "Reason: ${booking.postponeReason}",
                                                fontFamily = PlusJakartaSansFontFamily,
                                                fontSize = 11.sp,
                                                color = Color(0xFFE5E7EB)
                                            )
                                        }
                                        Text(
                                            text = "Email alert dispatched to: ${booking.customerEmail.ifEmpty { "registered email" }}",
                                            fontFamily = PlusJakartaSansFontFamily,
                                            fontSize = 10.sp,
                                            color = Color(0xFFA0A0A0)
                                        )
                                    }
                                }
                            }

                            if (booking.rejectReason.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Reason: ${booking.rejectReason}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(12.dp))

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

                                if (booking.status == BookingStatus.PENDING || booking.status == BookingStatus.CONFIRMED) {
                                    OutlinedButton(
                                        onClick = { cancelConfirmationBooking = booking },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = MaterialTheme.colorScheme.error
                                        )
                                    ) {
                                        Text("Cancel", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Cancel confirmation dialog
    cancelConfirmationBooking?.let { b ->
        AlertDialog(
            onDismissRequest = { cancelConfirmationBooking = null },
            title = { Text("Cancel Appointment?") },
            text = {
                Text("Are you sure you want to cancel ${b.serviceName} scheduled for ${b.bookingDate} at ${b.startTime}?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.cancelBooking(b.id)
                        cancelConfirmationBooking = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Confirm Cancellation")
                }
            },
            dismissButton = {
                TextButton(onClick = { cancelConfirmationBooking = null }) {
                    Text("Keep Appointment")
                }
            }
        )
    }
}

// SCREEN 3: LIVE SUPPORT
@Composable
fun CustomerSupportScreen(viewModel: SalonViewModel) {
    val messages by viewModel.customerSupportMessages.collectAsState()
    var inputMessage by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Concierge & Support",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Chat live with our salon host and master stylists",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Quick inquiry suggestion chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val suggestions = listOf(
                "How do I prepare for colouring?",
                "What is your cash payment policy?",
                "Do you provide complimentary valet parking?"
            )
            items(suggestions) { prompt ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.clickable {
                        viewModel.sendCustomerSupportMessage(prompt)
                    }
                ) {
                    Text(
                        text = prompt,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Messages Stream
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { msg ->
                val isMe = msg.senderRole == UserRole.CUSTOMER
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                ) {
                    Surface(
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isMe) 16.dp else 4.dp,
                            bottomEnd = if (isMe) 4.dp else 16.dp
                        ),
                        color = if (isMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.widthIn(max = 280.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (isMe) "You" else msg.senderName,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isMe) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f) else MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = msg.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isMe) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Send Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputMessage,
                onValueChange = { inputMessage = it },
                placeholder = { Text("Ask anything...") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("support_input_field"),
                shape = RoundedCornerShape(24.dp)
            )

            IconButton(
                onClick = {
                    if (inputMessage.isNotBlank()) {
                        viewModel.sendCustomerSupportMessage(inputMessage)
                        inputMessage = ""
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .testTag("support_send_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

// SCREEN 4: PROFILE & REAL ACCOUNT AUTHENTICATION
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomerProfileScreen(viewModel: SalonViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsState()
    val bookings by viewModel.customerBookings.collectAsState()
    val allServices by viewModel.activeServices.collectAsState()
    val listState = rememberLazyListState()

    var name by remember(currentUser) { mutableStateOf(currentUser.name) }
    var phone by remember(currentUser) { mutableStateOf(currentUser.phone) }
    var email by remember(currentUser) { mutableStateOf(currentUser.email) }
    var gender by remember(currentUser) { mutableStateOf(currentUser.gender.ifEmpty { "Not Specified" }) }
    var birthday by remember(currentUser) { mutableStateOf(currentUser.birthday) }
    var personalNotes by remember(currentUser) { mutableStateOf(currentUser.notes) }

    var selectedPreferredServices by remember(currentUser) {
        mutableStateOf(
            if (currentUser.preferredServices.isBlank()) emptySet()
            else currentUser.preferredServices.split(",").map { it.trim() }.filter { it.isNotEmpty() }.toSet()
        )
    }

    var showAuthSheet by remember { mutableStateOf(false) }
    var authInitialTab by remember { mutableIntStateOf(0) }
    var showGoogleAccountPicker by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showGoogleAccountPicker) {
        com.example.ui.auth.GoogleAccountChooserDialog(
            onDismiss = { showGoogleAccountPicker = false },
            onAccountChosen = { gName, gEmail ->
                showGoogleAccountPicker = false
                viewModel.signInWithGoogle(
                    name = gName,
                    email = gEmail,
                    context = context,
                    onSuccess = {
                        AppFeedbackHelper.playSuccessSoundWithMediaPlayer(context)
                        Toast.makeText(context, "Verified Google Account linked: $gEmail", Toast.LENGTH_SHORT).show()
                    },
                    onError = { err ->
                        Toast.makeText(context, err, Toast.LENGTH_LONG).show()
                    }
                )
            }
        )
    }

    if (showAuthSheet) {
        AuthModalSheet(
            viewModel = viewModel,
            initialTab = authInitialTab,
            onDismiss = { showAuthSheet = false },
            onSuccess = {
                showAuthSheet = false
            }
        )
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 260.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Customer Account & Preferences",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Manage your appointments, preferred treatments & personal details",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Real Login / Registration Prompt Card (If guest / not logged in)
        if (!isUserLoggedIn) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "Sign in to Unisex Salon",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "Authenticate with Google or enter your Gmail and mobile number with OTP verification to sync booking receipts across devices.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Direct Google Sign In Button
                        Surface(
                            onClick = {
                                AppFeedbackHelper.triggerClick(context)
                                showGoogleAccountPicker = true
                            },
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                com.example.ui.auth.GoogleBrandLogo(modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Continue with Google",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = {
                                    authInitialTab = 0
                                    showAuthSheet = true
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("button_open_signin"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Email Sign In")
                            }

                            OutlinedButton(
                                onClick = {
                                    authInitialTab = 1
                                    showAuthSheet = true
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("button_open_signup"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Register with OTP")
                            }
                        }
                    }
                }
            }
        }

        // Avatar Card & Stats
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (currentUser.avatarInitial.isNotBlank()) currentUser.avatarInitial else "U",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentUser.name.ifEmpty { "Guest Client" },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            if (isUserLoggedIn) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verified Member",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Text(
                            text = if (currentUser.phone.isNotBlank()) currentUser.phone else "No mobile linked",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (currentUser.email.isNotBlank()) {
                            Text(
                                text = currentUser.email,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${bookings.size} Total Bookings • ${bookings.count { it.status == BookingStatus.COMPLETED }} Completed",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (isUserLoggedIn) {
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedButton(
                                onClick = {
                                    AppFeedbackHelper.triggerClick(context)
                                    viewModel.logoutCustomer()
                                    Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                                },
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Log Out", style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }
            }
        }

        // PREFERRED SERVICES CARD
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Spa,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "My Preferred Services",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Select your favorite treatments & services to personalize your appointment bookings:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (srv in allServices) {
                            val isSelected = selectedPreferredServices.contains(srv.name)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    AppFeedbackHelper.triggerSelection(context)
                                    selectedPreferredServices = if (isSelected) {
                                        selectedPreferredServices - srv.name
                                    } else {
                                        selectedPreferredServices + srv.name
                                    }
                                },
                                label = { Text(srv.name, fontSize = 12.sp) },
                                leadingIcon = if (isSelected) {
                                    {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                } else null,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }

                    if (selectedPreferredServices.isNotEmpty()) {
                        Text(
                            text = "Selected (${selectedPreferredServices.size}): ${selectedPreferredServices.joinToString(", ")}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // PERSONAL DETAILS & PREFERENCES FORM
        item {
            val isEmailInvalid = email.isNotBlank() && !com.example.ui.auth.isValidEmailAddress(email)
            val isPhoneInvalid = phone.isNotBlank() && !com.example.ui.auth.isValidMobileNumber(phone)

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = if (isUserLoggedIn) "Edit Personal Profile & Preferences" else "Contact & Personal Information",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        placeholder = { Text("e.g. Naitik Sahu") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null)
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Mobile Phone (10 Digits)") },
                        placeholder = { Text("e.g. 9876543210") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Call, contentDescription = null)
                        },
                        isError = isPhoneInvalid,
                        supportingText = {
                            if (isPhoneInvalid) {
                                Text(
                                    text = "Invalid phone number. Must be a valid 10-digit mobile number.",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        singleLine = true,
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Gmail or Real Email Address") },
                        placeholder = { Text("e.g. name@gmail.com") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Email, contentDescription = null)
                        },
                        isError = isEmailInvalid,
                        supportingText = {
                            if (isEmailInvalid) {
                                Text(
                                    text = "Invalid email format. Enter a valid email address (e.g. name@gmail.com).",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        singleLine = true,
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Email
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Gender Preference
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Gender Preference",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (g in listOf("Female", "Male", "Other", "Prefer Not")) {
                                val isSelected = gender.equals(g, ignoreCase = true)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        AppFeedbackHelper.triggerSelection(context)
                                        gender = g
                                    },
                                    label = { Text(g, fontSize = 11.sp) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // Birthday / Date of Birth
                    OutlinedTextField(
                        value = birthday,
                        onValueChange = { birthday = it },
                        label = { Text("Birthday / Date of Birth (Optional)") },
                        placeholder = { Text("e.g. 15 Aug 1998") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null)
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Personal Styling & Allergy Notes
                    OutlinedTextField(
                        value = personalNotes,
                        onValueChange = { personalNotes = it },
                        label = { Text("Hair/Skin Styling Notes & Allergies (Optional)") },
                        placeholder = { Text("e.g. Allergic to ammonia bleach; prefers organic oil massage") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Info, contentDescription = null)
                        },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = {
                            if (isEmailInvalid) {
                                AppFeedbackHelper.triggerError(context)
                                Toast.makeText(context, "Invalid email! Please enter a valid email address (e.g. name@gmail.com)", Toast.LENGTH_LONG).show()
                                return@Button
                            }
                            if (isPhoneInvalid) {
                                AppFeedbackHelper.triggerError(context)
                                Toast.makeText(context, "Invalid phone! Please enter a 10-digit mobile number", Toast.LENGTH_LONG).show()
                                return@Button
                            }

                            // Play calm, non-intrusive sound effect with MediaPlayer
                            AppFeedbackHelper.playSuccessSoundWithMediaPlayer(context)

                            val prefServicesString = selectedPreferredServices.joinToString(",")
                            viewModel.updateCurrentUserProfile(
                                name = name,
                                phone = phone,
                                email = email,
                                preferredServices = prefServicesString,
                                gender = gender,
                                birthday = birthday,
                                notes = personalNotes
                            )
                            Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("save_profile_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Save Profile",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Salon Payment Notice Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Cash Policy: Unisex Salon accepts cash settlement upon completion of your styling or spa session. An itemized paper receipt will be provided at the host counter.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 6-STEP BOOKING WIZARD MODAL BOTTOM SHEET
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BookingWizardSheet(
    wizardState: BookingWizardState,
    activeStaff: List<StaffEntity>,
    workingDays: List<SalonWorkingDayEntity>,
    holidays: List<SalonHolidayEntity>,
    currentUser: UserEntity,
    salonConfig: SalonConfigEntity? = null,
    onClose: () -> Unit,
    onSetStep: (Int) -> Unit,
    onSelectService: (ServiceEntity) -> Unit,
    onSelectDate: (String) -> Unit,
    onSelectStaff: (StaffEntity?) -> Unit,
    onSelectSlot: (String) -> Unit,
    onCheckCustomSlot: ((String, (Boolean, String) -> Unit) -> Unit)? = null,
    onUpdateNotes: (String) -> Unit,
    onSubmitBooking: () -> Unit,
    onViewBookings: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    var customTimeInput by remember { mutableStateOf("") }
    var customSlotChecking by remember { mutableStateOf(false) }
    var customSlotError by remember { mutableStateOf<String?>(null) }
    var customSlotSuccess by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = sheetState,
        modifier = Modifier
            .testTag("booking_wizard_modal")
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Header with Step Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Step ${wizardState.step} of 6",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = when (wizardState.step) {
                            1 -> "Select Service"
                            2 -> "Choose Date"
                            3 -> "Select Specialist"
                            4 -> "Available Time Slots"
                            5 -> "Review Booking"
                            6 -> "Booking Confirmed!"
                            else -> "Book Service"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(onClick = onClose) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            // Progress bar
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (i in 1..6) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(CircleShape)
                            .background(
                                if (i <= wizardState.step) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Error display if validation failed
            wizardState.errorMessage?.let { errorMsg ->
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.errorContainer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        text = errorMsg,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // STEP CONTENT
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 380.dp, max = 560.dp)
            ) {
                when (wizardState.step) {
                    // STEP 1: SERVICE (if opened directly without pre-selection)
                    1 -> {
                        Text("Please pick a service to begin.", style = MaterialTheme.typography.bodyMedium)
                    }

                    // STEP 2: SELECT DATE
                    2 -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = "Selected Service: ${wizardState.service?.name ?: "Service"}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Choose an appointment date (showing next 14 days):",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            // Date Cards
                            val dateCards = remember {
                                val list = mutableListOf<Triple<String, String, String>>()
                                val cal = Calendar.getInstance()
                                val sdfFull = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                                val sdfDay = SimpleDateFormat("EEE", Locale.US)
                                val sdfNum = SimpleDateFormat("d MMM", Locale.US)

                                for (i in 0 until 14) {
                                    val date = cal.time
                                    list.add(
                                        Triple(
                                            sdfFull.format(date),
                                            sdfDay.format(date),
                                            sdfNum.format(date)
                                        )
                                    )
                                    cal.add(Calendar.DAY_OF_YEAR, 1)
                                }
                                list
                            }

                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                items(dateCards) { (fullDate, dayName, dayNum) ->
                                    val isSelected = wizardState.date == fullDate
                                    val isHoliday = holidays.any { it.dateString == fullDate }

                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(enabled = !isHoliday) {
                                                onSelectDate(fullDate)
                                            }
                                            .testTag("date_slot_$fullDate"),
                                        shape = RoundedCornerShape(12.dp),
                                        color = when {
                                            isSelected -> MaterialTheme.colorScheme.primaryContainer
                                            isHoliday -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                            else -> MaterialTheme.colorScheme.surface
                                        },
                                        border = if (isSelected) androidx.compose.foundation.BorderStroke(
                                            2.dp,
                                            MaterialTheme.colorScheme.primary
                                        ) else androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            MaterialTheme.colorScheme.outlineVariant
                                        )
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(14.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                                            ) {
                                                Text(
                                                    text = dayName,
                                                    style = MaterialTheme.typography.titleSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isHoliday) Color.Gray else MaterialTheme.colorScheme.onSurface
                                                )
                                                Text(
                                                    text = dayNum,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = if (isHoliday) Color.Gray else MaterialTheme.colorScheme.onSurface
                                                )
                                            }

                                            if (isHoliday) {
                                                Surface(
                                                    shape = RoundedCornerShape(6.dp),
                                                    color = MaterialTheme.colorScheme.errorContainer
                                                ) {
                                                    Text(
                                                        text = "Salon Holiday",
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = MaterialTheme.colorScheme.onErrorContainer
                                                    )
                                                }
                                            } else if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = { onSetStep(3) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("continue_to_staff_button"),
                                enabled = wizardState.date.isNotEmpty(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Continue to Specialist Selection")
                            }
                        }
                    }

                    // STEP 3: SELECT STAFF
                    3 -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = "Choose your preferred stylist or select Any Available:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                // "Any Available Specialist" Option
                                item {
                                    val isSelected = wizardState.staff == null
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onSelectStaff(null) }
                                            .testTag("staff_any_available"),
                                        shape = RoundedCornerShape(14.dp),
                                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                                        border = if (isSelected) androidx.compose.foundation.BorderStroke(
                                            2.dp,
                                            MaterialTheme.colorScheme.primary
                                        ) else androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            MaterialTheme.colorScheme.outlineVariant
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
                                                    .clip(CircleShape)
                                                    .background(MaterialTheme.colorScheme.primary),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Star,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onPrimary
                                                )
                                            }
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = "Any Available Specialist",
                                                    style = MaterialTheme.typography.titleSmall,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = "Fastest availability for your chosen date",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                    }
                                }

                                items(activeStaff) { staff ->
                                    val isSelected = wizardState.staff?.id == staff.id
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onSelectStaff(staff) }
                                            .testTag("staff_card_${staff.id}"),
                                        shape = RoundedCornerShape(14.dp),
                                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                                        border = if (isSelected) androidx.compose.foundation.BorderStroke(
                                            2.dp,
                                            MaterialTheme.colorScheme.primary
                                        ) else androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            MaterialTheme.colorScheme.outlineVariant
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
                                                    .clip(CircleShape)
                                                    .background(Color(staff.avatarColorHex)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = staff.name.take(1),
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold,
                                                    style = MaterialTheme.typography.titleMedium
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
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }

                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { onSetStep(2) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Back")
                                }
                                Button(
                                    onClick = { onSetStep(4) },
                                    modifier = Modifier
                                        .weight(1.5f)
                                        .testTag("continue_to_slots_button"),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("View Time Slots")
                                }
                            }
                        }
                    }

                    // STEP 4: AVAILABLE TIME SLOTS & CUSTOM TIME SELECTION
                    4 -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            LazyColumn(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                item {
                                    Text(
                                        text = "Select Appointment Time",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Choose from calculated collision-free slots or enter a custom time.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                // Auto Computed Slots Section
                                item {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                        )
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Schedule,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Text(
                                                    text = "Quick Available Slots",
                                                    style = MaterialTheme.typography.titleSmall,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(8.dp))

                                            if (wizardState.isComputingSlots) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(60.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                                }
                                            } else if (wizardState.availableSlots.isEmpty()) {
                                                Text(
                                                    text = "No preset slots available for this specialist/date. Use the custom time picker below to check specific slot availability!",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            } else {
                                                FlowRow(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    wizardState.availableSlots.forEach { slot ->
                                                        val isSelected = wizardState.selectedSlot == slot
                                                        Surface(
                                                            modifier = Modifier
                                                                .clickable {
                                                                    AppFeedbackHelper.triggerSelection(context)
                                                                    onSelectSlot(slot)
                                                                    customSlotError = null
                                                                    customSlotSuccess = "Selected slot $slot"
                                                                }
                                                                .testTag("slot_pill_$slot"),
                                                            shape = RoundedCornerShape(12.dp),
                                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                                            border = if (isSelected) androidx.compose.foundation.BorderStroke(
                                                                2.dp,
                                                                MaterialTheme.colorScheme.primary
                                                            ) else null
                                                        ) {
                                                            Text(
                                                                text = slot,
                                                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                                                style = MaterialTheme.typography.labelMedium,
                                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                // CUSTOM TIME INPUT SECTION
                                item {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                        ),
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (customSlotSuccess != null) Color(0xFF2E7D32).copy(alpha = 0.6f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                        )
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.EventNote,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Text(
                                                    text = "Enter Custom Time",
                                                    style = MaterialTheme.typography.titleSmall,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            Text(
                                                text = "Need a specific slot? Type your desired time (24-hr HH:mm format). If booked or closed, the system will alert you; if available, it will reserve it for your booking.",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )

                                            Spacer(modifier = Modifier.height(10.dp))

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                OutlinedTextField(
                                                    value = customTimeInput,
                                                    onValueChange = {
                                                        customTimeInput = it
                                                        customSlotError = null
                                                        customSlotSuccess = null
                                                    },
                                                    label = { Text("Custom Time (HH:mm)") },
                                                    placeholder = { Text("e.g. 14:30") },
                                                    singleLine = true,
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .testTag("custom_time_input"),
                                                    shape = RoundedCornerShape(10.dp)
                                                )

                                                Button(
                                                    onClick = {
                                                        val raw = customTimeInput.trim()
                                                        if (!Regex("^([01]?[0-9]|2[0-3]):[0-5][0-9]$").matches(raw)) {
                                                            customSlotError = "Please enter time in HH:mm format (e.g. 11:30 or 15:45)"
                                                            customSlotSuccess = null
                                                            AppFeedbackHelper.triggerError(context)
                                                            return@Button
                                                        }
                                                        val parts = raw.split(":")
                                                        val formatted = String.format(Locale.US, "%02d:%02d", parts[0].toInt(), parts[1].toInt())
                                                        customSlotChecking = true
                                                        customSlotError = null
                                                        customSlotSuccess = null

                                                        if (onCheckCustomSlot != null) {
                                                            onCheckCustomSlot(formatted) { isAvail, reason ->
                                                                customSlotChecking = false
                                                                if (isAvail) {
                                                                    customSlotSuccess = "Custom time $formatted is available!"
                                                                    customSlotError = null
                                                                    onSelectSlot(formatted)
                                                                    AppFeedbackHelper.triggerSuccess(context)
                                                                } else {
                                                                    customSlotError = "Time not available: $reason"
                                                                    customSlotSuccess = null
                                                                    AppFeedbackHelper.triggerError(context)
                                                                }
                                                            }
                                                        } else {
                                                            customSlotChecking = false
                                                            onSelectSlot(formatted)
                                                            customSlotSuccess = "Selected custom time: $formatted"
                                                            AppFeedbackHelper.triggerSuccess(context)
                                                        }
                                                    },
                                                    enabled = !customSlotChecking && customTimeInput.isNotBlank(),
                                                    shape = RoundedCornerShape(10.dp),
                                                    modifier = Modifier.testTag("apply_custom_time_button")
                                                ) {
                                                    if (customSlotChecking) {
                                                        CircularProgressIndicator(
                                                            modifier = Modifier.size(16.dp),
                                                            color = MaterialTheme.colorScheme.onPrimary,
                                                            strokeWidth = 2.dp
                                                        )
                                                    } else {
                                                        Text("Check & Apply")
                                                    }
                                                }
                                            }

                                            // Quick Preset Chips
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "Quick Presets:",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                listOf("10:15", "11:45", "14:15", "16:45", "18:15").forEach { preset ->
                                                    SuggestionChip(
                                                        onClick = {
                                                            customTimeInput = preset
                                                            customSlotError = null
                                                            customSlotSuccess = null
                                                            AppFeedbackHelper.triggerSelection(context)
                                                        },
                                                        label = { Text(preset, fontSize = 11.sp) }
                                                    )
                                                }
                                            }

                                            // Feedback Banners
                                            if (customSlotError != null) {
                                                Spacer(modifier = Modifier.height(10.dp))
                                                Surface(
                                                    color = MaterialTheme.colorScheme.errorContainer,
                                                    shape = RoundedCornerShape(10.dp),
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Row(
                                                        modifier = Modifier.padding(10.dp),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.ErrorOutline,
                                                            contentDescription = null,
                                                            tint = MaterialTheme.colorScheme.error,
                                                            modifier = Modifier.size(20.dp)
                                                        )
                                                        Text(
                                                            text = customSlotError ?: "",
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = MaterialTheme.colorScheme.onErrorContainer,
                                                            fontWeight = FontWeight.SemiBold
                                                        )
                                                    }
                                                }
                                            }

                                            if (customSlotSuccess != null) {
                                                Spacer(modifier = Modifier.height(10.dp))
                                                Surface(
                                                    color = Color(0xFF1B5E20).copy(alpha = 0.15f),
                                                    shape = RoundedCornerShape(10.dp),
                                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.4f)),
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Row(
                                                        modifier = Modifier.padding(10.dp),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.CheckCircle,
                                                            contentDescription = null,
                                                            tint = Color(0xFF2E7D32),
                                                            modifier = Modifier.size(20.dp)
                                                        )
                                                        Text(
                                                            text = customSlotSuccess ?: "",
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = Color(0xFF2E7D32),
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                // Selected Slot Confirmation Card
                                item {
                                    if (wizardState.selectedSlot != null) {
                                        Surface(
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Column {
                                                    Text(
                                                        text = "Selected Appointment Time",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                                    )
                                                    Text(
                                                        text = "${wizardState.date} at ${wizardState.selectedSlot}",
                                                        style = MaterialTheme.typography.titleMedium,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                                    )
                                                }
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { onSetStep(3) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Back")
                                }
                                Button(
                                    onClick = { onSetStep(5) },
                                    modifier = Modifier
                                        .weight(1.5f)
                                        .testTag("continue_to_review_button"),
                                    enabled = wizardState.selectedSlot != null,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Review Booking")
                                }
                            }
                        }
                    }

                    // STEP 5: REVIEW BOOKING
                    5 -> {
                        val service = wizardState.service
                        val staff = wizardState.staff ?: activeStaff.firstOrNull()

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .imePadding()
                        ) {
                            Text(
                                text = "Please review your appointment summary:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Service:", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(service?.name ?: "", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Duration & Price:", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        val curSym = salonConfig?.currencySymbol ?: "₹"
                                        val prcVal = service?.price ?: 0.0
                                        val formattedPrc = if (prcVal % 1.0 == 0.0) String.format(Locale.US, "%.0f", prcVal) else String.format(Locale.US, "%.2f", prcVal)
                                        Text(
                                            "${service?.durationMinutes} min • $curSym$formattedPrc",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Date & Time:", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(
                                            "${wizardState.date} at ${wizardState.selectedSlot}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Specialist:", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(staff?.name ?: "First Available", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Guest Name:", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(currentUser.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Payment Mode:", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text("CASH on Arrival", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = wizardState.customerNotes,
                                onValueChange = onUpdateNotes,
                                label = { Text("Special Requests / Allergy Notes (Optional)") },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2,
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { onSetStep(4) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Back")
                                }
                                Button(
                                    onClick = onSubmitBooking,
                                    modifier = Modifier
                                        .weight(1.5f)
                                        .testTag("confirm_booking_cash_button"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text("Confirm with Cash Payment")
                                }
                            }
                        }
                    }

                    // STEP 6: CONFIRMATION RECEIPT
                    6 -> {
                        val confirmed = wizardState.confirmedBooking
                        androidx.compose.runtime.LaunchedEffect(confirmed?.referenceNumber) {
                            AppFeedbackHelper.playSuccessSoundWithMediaPlayer(context)
                        }
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFD1FAE5)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF059669),
                                    modifier = Modifier.size(44.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Appointment Reserved!",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Reference: ${confirmed?.referenceNumber ?: "BK-SUCCESS"}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            ) {
                                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = "${confirmed?.serviceName}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "With ${confirmed?.staffName} on ${confirmed?.bookingDate} at ${confirmed?.startTime}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    val cSym = salonConfig?.currencySymbol ?: "₹"
                                    val cPrc = confirmed?.servicePrice ?: 0.0
                                    val formattedCPrc = if (cPrc % 1.0 == 0.0) String.format(Locale.US, "%.0f", cPrc) else String.format(Locale.US, "%.2f", cPrc)
                                    Text(
                                        text = "Amount Due at Salon: $cSym$formattedCPrc in Cash",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFB45309)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = onViewBookings,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("view_my_bookings_button"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("View in My Bookings")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
