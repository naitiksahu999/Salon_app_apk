package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.data.model.BookingStatus
import com.example.data.model.NotificationEntity
import com.example.data.model.SalonConfigEntity
import com.example.data.model.SalonWorkingDayEntity
import com.example.data.model.ServiceEntity
import com.example.data.model.UserEntity
import com.example.ui.theme.DarkBorderColor
import com.example.ui.theme.DarkCanvasBackground
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceCardElevated
import com.example.ui.theme.PlayfairDisplayFontFamily
import com.example.ui.theme.PlusJakartaSansFontFamily
import com.example.ui.theme.SalonGoldPill
import com.example.ui.theme.SalonGoldPrimary
import com.example.ui.theme.SalonReviewStar
import com.example.viewmodel.AppMode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun AppRoleSwitcherBar(
    activeMode: AppMode,
    currentUser: UserEntity,
    unreadNotifications: Int,
    onSwitchMode: (AppMode) -> Unit,
    onOpenNotifications: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("app_role_switcher_bar"),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f),
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Live Sync indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF10B981))
                    )
                    Text(
                        text = "Unified Live DB",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Notification Bell
                IconButton(
                    onClick = onOpenNotifications,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("notification_bell_button")
                ) {
                    BadgedBox(badge = {
                        if (unreadNotifications > 0) {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            ) {
                                Text(
                                    text = if (unreadNotifications > 9) "9+" else "$unreadNotifications",
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Two app toggle pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Customer App Button
                val isCustomer = activeMode == AppMode.CUSTOMER
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (isCustomer) MaterialTheme.colorScheme.primary else Color.Transparent
                        )
                        .clickable { onSwitchMode(AppMode.CUSTOMER) }
                        .padding(vertical = 8.dp)
                        .testTag("switch_to_customer_app_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = if (isCustomer) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Customer App",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isCustomer) FontWeight.Bold else FontWeight.Normal,
                            color = if (isCustomer) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Admin App Button
                val isAdmin = activeMode == AppMode.ADMIN
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (isAdmin) MaterialTheme.colorScheme.primary else Color.Transparent
                        )
                        .clickable { onSwitchMode(AppMode.ADMIN) }
                        .padding(vertical = 8.dp)
                        .testTag("switch_to_admin_app_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = if (isAdmin) Icons.Default.AdminPanelSettings else Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (isAdmin) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (isAdmin) "Admin App" else "Admin (Protected)",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isAdmin) FontWeight.Bold else FontWeight.Normal,
                            color = if (isAdmin) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (status) {
        BookingStatus.PENDING -> Triple(Color(0xFFFEF3C7), Color(0xFFB45309), "Pending Approval")
        BookingStatus.POSTPONED -> Triple(Color(0xFFFEF3C7), Color(0xFFD97706), "Postponed")
        BookingStatus.CONFIRMED -> Triple(Color(0xFFDBEAFE), Color(0xFF1D4ED8), "Confirmed")
        BookingStatus.IN_PROGRESS -> Triple(Color(0xFFF3E8FF), Color(0xFF7E22CE), "In Progress")
        BookingStatus.COMPLETED -> Triple(Color(0xFFD1FAE5), Color(0xFF047857), "Completed")
        BookingStatus.REJECTED -> Triple(Color(0xFFFEE2E2), Color(0xFFB91C1C), "Rejected")
        BookingStatus.CANCELLED -> Triple(Color(0xFFF3F4F6), Color(0xFF4B5563), "Cancelled")
        else -> Triple(Color(0xFFF3F4F6), Color(0xFF4B5563), status)
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = bgColor
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun CashPaymentBadge(
    isCollected: Boolean = false,
    amount: Double? = null,
    modifier: Modifier = Modifier,
    currencySymbol: String = "₹"
) {
    val amountSuffix = if (amount != null) {
        val formatted = if (amount % 1.0 == 0.0) String.format(Locale.US, "%.0f", amount) else String.format(Locale.US, "%.2f", amount)
        " ($currencySymbol$formatted)"
    } else ""

    val (bgColor, textColor, text) = if (isCollected) {
        Triple(
            Color(0xFFCCFBF1),
            Color(0xFF0F766E),
            "Paid in Cash$amountSuffix"
        )
    } else {
        Triple(
            Color(0xFFFFFBEB),
            Color(0xFFB45309),
            "Cash on Arrival$amountSuffix"
        )
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = bgColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AttachMoney,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )
        }
    }
}

@Composable
fun UnisexSalonTopBar(
    unreadCount: Int,
    onMenuClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onAdminClick: () -> Unit,
    salonConfig: SalonConfigEntity? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("unisex_salon_top_bar"),
        color = DarkCanvasBackground
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Hamburger Menu
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(DarkSurfaceCard)
                    .border(0.5.dp, DarkBorderColor, CircleShape)
                    .testTag("top_bar_menu_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Brand Header: Scissor Icon + Salon Name + Slogan / Tagline
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCut,
                        contentDescription = null,
                        tint = SalonGoldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = salonConfig?.salonName?.takeIf { it.isNotBlank() } ?: "UNISEX SALON",
                        fontFamily = PlayfairDisplayFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        letterSpacing = 1.2.sp,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = salonConfig?.tagline?.takeIf { it.isNotBlank() } ?: "LOOK GOOD • FEEL GREAT",
                    fontFamily = PlusJakartaSansFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 9.sp,
                    letterSpacing = 1.5.sp,
                    color = SalonGoldPrimary.copy(alpha = 0.9f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Action Icons: Notifications & Admin
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onNotificationClick,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(DarkSurfaceCard)
                        .border(0.5.dp, DarkBorderColor, CircleShape)
                        .testTag("top_bar_notification_button")
                ) {
                    BadgedBox(
                        badge = {
                            if (unreadCount > 0) {
                                Badge(
                                    containerColor = Color(0xFFEF4444),
                                    contentColor = Color.White
                                ) {
                                    Text(
                                        text = if (unreadCount > 9) "9+" else unreadCount.toString(),
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                AdminDiscreetCornerButton(
                    onClick = onAdminClick,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

data class HeroSlide(
    val titleLine1: String,
    val titleLine2Prefix: String,
    val titleAccent: String,
    val tags: String,
    val ctaText: String
)

@Composable
fun SalonHeroBannerCard(
    salonConfig: SalonConfigEntity?,
    workingDays: List<SalonWorkingDayEntity>,
    onBookClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var currentSlideIndex by remember { mutableIntStateOf(0) }

    val slides = remember(salonConfig) {
        listOf(
            HeroSlide(
                titleLine1 = salonConfig?.heroTitleLine1 ?: "Premium Care",
                titleLine2Prefix = "for ",
                titleAccent = salonConfig?.heroAccentText ?: "Everyone",
                tags = salonConfig?.heroTags ?: "Hair | Skin | Grooming | Beauty",
                ctaText = salonConfig?.heroCtaText ?: "Book Your Appointment"
            ),
            HeroSlide(
                titleLine1 = "More Than",
                titleLine2Prefix = "Just a ",
                titleAccent = "Salon",
                tags = "Your style • Our passion",
                ctaText = "Book Your Appointment"
            ),
            HeroSlide(
                titleLine1 = "Be Your",
                titleLine2Prefix = "Best ",
                titleAccent = "Self",
                tags = "Expert care • Premium products",
                ctaText = "Book Your Appointment"
            )
        )
    }

    val currentSlide = slides[currentSlideIndex % slides.size]

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("salon_hero_banner_card"),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(235.dp)
        ) {
            // Background Image: generated luxury salon interior
            val customUrl = salonConfig?.heroImageUrl?.takeIf { it.isNotBlank() }
            if (customUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(customUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Salon Interior",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(235.dp),
                    contentScale = PhotoFitUtils.getContentScaleForFit(salonConfig?.heroImageFit ?: "CROP_CENTER"),
                    alignment = PhotoFitUtils.getAlignmentForFit(salonConfig?.heroImageFit ?: "CROP_CENTER"),
                    placeholder = painterResource(id = R.drawable.img_salon_interior),
                    error = painterResource(id = R.drawable.img_salon_interior)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.img_salon_interior),
                    contentDescription = "Salon Interior",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(235.dp),
                    contentScale = ContentScale.Crop
                )
            }

            // Cinematic Gradient Overlay matching reference dark atmosphere
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(235.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x33000000),
                                Color(0x77000000),
                                Color(0xEE0A0A0A),
                                Color(0xFC050505)
                            )
                        )
                    )
            )

            // Content Overlay
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
                    .align(Alignment.BottomStart)
            ) {
                // Today's Open Status Badge
                val dayOfWeek = remember {
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
                val todaySchedule = workingDays.find { it.dayOfWeek == dayOfWeek }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (todaySchedule?.isOpen == true) Color(0x2E10B981) else Color(0x2EEF4444),
                    border = androidx.compose.foundation.BorderStroke(
                        0.5.dp,
                        if (todaySchedule?.isOpen == true) Color(0x6634D399) else Color(0x66F87171)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (todaySchedule?.isOpen == true) Color(0xFF34D399) else Color(0xFFF87171)
                                )
                        )
                        Text(
                            text = if (todaySchedule?.isOpen == true)
                                "Open Today • ${todaySchedule.openTime} - ${todaySchedule.closeTime}"
                            else "Closed Today",
                            fontFamily = PlusJakartaSansFontFamily,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Hero Headline Line 1 & Line 2 with elegant Playfair Display styling
                AnimatedContent(
                    targetState = currentSlide,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "hero_slide_transition"
                ) { slide ->
                    Column {
                        Text(
                            text = slide.titleLine1,
                            fontFamily = PlayfairDisplayFontFamily,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            lineHeight = 28.sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = slide.titleLine2Prefix,
                                fontFamily = PlayfairDisplayFontFamily,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                lineHeight = 28.sp
                            )
                            Text(
                                text = slide.titleAccent,
                                fontFamily = PlayfairDisplayFontFamily,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic,
                                color = SalonGoldPrimary,
                                lineHeight = 28.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = slide.tags,
                            fontFamily = PlusJakartaSansFontFamily,
                            fontSize = 12.sp,
                            color = Color(0xFFC0C0C0),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Row with Carousel Indicators (Left), CTA Pill Button (Center), Next Arrow (Right)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Indicator Dots
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        slides.indices.forEach { index ->
                            val isSelected = index == currentSlideIndex % slides.size
                            Box(
                                modifier = Modifier
                                    .size(if (isSelected) 8.dp else 6.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) SalonGoldPrimary else Color(0x66FFFFFF)
                                    )
                                    .clickable { currentSlideIndex = index }
                            )
                        }
                    }

                    // Luxury Pill Button: "Book Your Appointment →"
                    Button(
                        onClick = { onBookClick?.invoke() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SalonGoldPill,
                            contentColor = Color(0xFF161616)
                        ),
                        shape = RoundedCornerShape(24.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("hero_book_appointment_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = currentSlide.ctaText,
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    // Next Slide Arrow Button
                    IconButton(
                        onClick = { currentSlideIndex = (currentSlideIndex + 1) % slides.size },
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0x33FFFFFF))
                            .border(0.5.dp, Color(0x44FFFFFF), CircleShape)
                            .testTag("hero_next_slide_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Next Slide",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SalonLocationCard(
    salonConfig: SalonConfigEntity?,
    onDirectionsClick: () -> Unit,
    onExpandClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onExpandClick?.invoke() }
            .testTag("salon_location_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, DarkBorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(DarkSurfaceCardElevated)
                        .border(0.5.dp, DarkBorderColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = SalonGoldPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column {
                    Text(
                        text = salonConfig?.salonName?.takeIf { it.isNotBlank() } ?: "Our Salon",
                        fontFamily = PlusJakartaSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                    val fullLocation = listOfNotNull(
                        salonConfig?.streetAddress?.takeIf { it.isNotBlank() },
                        salonConfig?.area?.takeIf { it.isNotBlank() },
                        salonConfig?.city?.takeIf { it.isNotBlank() },
                        salonConfig?.pincode?.takeIf { it.isNotBlank() }
                    ).joinToString(", ").ifBlank {
                        salonConfig?.address?.takeIf { it.isNotBlank() } ?: "123 Wellness Street, Green Park, Delhi"
                    }
                    Text(
                        text = fullLocation,
                        fontFamily = PlusJakartaSansFontFamily,
                        fontSize = 11.sp,
                        color = Color(0xFFA0A0A0),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            TextButton(
                onClick = onDirectionsClick,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                modifier = Modifier.testTag("location_get_directions_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Get Directions",
                        fontFamily = PlusJakartaSansFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = SalonGoldPrimary
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = SalonGoldPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SalonStorefrontShowcaseCard(
    salonConfig: SalonConfigEntity?,
    onDirectionsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("salon_storefront_showcase_card"),
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
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Storefront photo thumbnail with reactive custom image support
                val storefrontUrl = salonConfig?.storefrontImageUrl?.takeIf { it.isNotBlank() }
                if (storefrontUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(storefrontUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Storefront",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = PhotoFitUtils.getContentScaleForFit(salonConfig?.storefrontImageFit ?: "CROP_CENTER"),
                        alignment = PhotoFitUtils.getAlignmentForFit(salonConfig?.storefrontImageFit ?: "CROP_CENTER"),
                        placeholder = painterResource(id = R.drawable.img_salon_storefront),
                        error = painterResource(id = R.drawable.img_salon_storefront)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.img_salon_storefront),
                        contentDescription = "Storefront",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = salonConfig?.salonName ?: "UNISEX SALON",
                            fontFamily = PlayfairDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified Salon",
                            tint = SalonGoldPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = SalonReviewStar,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "4.8 (120+ reviews)",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE5E7EB)
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Hours",
                            tint = Color(0xFF34D399),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Open • 9:00 AM - 9:00 PM",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontSize = 11.sp,
                            color = Color(0xFFA0A0A0)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = DarkBorderColor, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = SalonGoldPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = salonConfig?.address ?: "123 Wellness Street, Green Park, Delhi",
                        fontFamily = PlusJakartaSansFontFamily,
                        fontSize = 11.sp,
                        color = Color(0xFFA0A0A0),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                TextButton(
                    onClick = onDirectionsClick,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.testTag("showcase_directions_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Get Directions",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            color = SalonGoldPrimary
                        )
                        Icon(
                            imageVector = Icons.Default.NearMe,
                            contentDescription = null,
                            tint = SalonGoldPrimary,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}

data class AmenityItem(
    val icon: ImageVector,
    val label: String,
    val description: String,
    val id: String = ""
)

fun getAmenityIcon(iconKey: String): ImageVector {
    return when (iconKey.uppercase()) {
        "AC", "AIR_CONDITIONING" -> Icons.Default.AcUnit
        "WIFI" -> Icons.Default.Wifi
        "PARKING", "VALET" -> Icons.Default.LocalParking
        "HYGIENE", "SHIELD" -> Icons.Default.Shield
        "LOUNGE" -> Icons.Default.Weekend
        "COFFEE", "BEVERAGES" -> Icons.Default.Star
        "MUSIC" -> Icons.Default.PlayArrow
        else -> Icons.Default.Spa
    }
}

@Composable
fun SalonAmenitiesRow(
    onAmenityClick: (AmenityItem) -> Unit,
    modifier: Modifier = Modifier,
    activeAmenities: List<com.example.data.model.AmenityEntity>? = null
) {
    val items: List<AmenityItem> = if (activeAmenities != null) {
        activeAmenities.map { entity ->
            AmenityItem(
                icon = getAmenityIcon(entity.iconKey),
                label = entity.name,
                description = entity.description,
                id = entity.id
            )
        }
    } else {
        listOf(
            AmenityItem(Icons.Default.AcUnit, "AC", "Full climate control for a relaxing sanctuary.", "ac"),
            AmenityItem(Icons.Default.Wifi, "Wi-Fi", "Complimentary high-speed gigabit Wi-Fi.", "wifi"),
            AmenityItem(Icons.Default.LocalParking, "Parking", "Dedicated VIP valet parking at entrance.", "parking"),
            AmenityItem(Icons.Default.Shield, "Hygiene", "Hospital-grade autoclave sterilization for all tools.", "hygiene"),
            AmenityItem(Icons.Default.Weekend, "Lounge", "Luxury refreshment lounge with espresso and infused water.", "lounge")
        )
    }

    if (items.isEmpty()) return

    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag("salon_amenities_row"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onAmenityClick(item) }
                    .padding(vertical = 4.dp)
                    .testTag("amenity_${item.label.lowercase()}")
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(DarkSurfaceCard)
                        .border(0.5.dp, DarkBorderColor, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = SalonGoldPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = item.label,
                    fontFamily = PlusJakartaSansFontFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFD1D5DB)
                )
            }
        }
    }
}

@Composable
fun QuickBookCard(
    service: ServiceEntity,
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(190.dp)
            .clickable { onBookClick() }
            .testTag("quick_book_card_${service.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, DarkBorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(service.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = service.name,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.salon_hero_banner),
                error = painterResource(id = R.drawable.salon_hero_banner)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service.name,
                    fontFamily = PlusJakartaSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "₹ ${service.price.toInt()}",
                    fontFamily = PlusJakartaSansFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = SalonGoldPrimary
                )
                Text(
                    text = "${service.durationMinutes} mins",
                    fontFamily = PlusJakartaSansFontFamily,
                    fontSize = 10.sp,
                    color = Color(0xFFA0A0A0)
                )
            }

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(SalonGoldPill)
                    .clickable { onBookClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Book",
                    tint = Color(0xFF141414),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun QuickBookSection(
    services: List<ServiceEntity>,
    onServiceClick: (ServiceEntity) -> Unit,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    text = "Quick Book",
                    fontFamily = PlayfairDisplayFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Text(
                    text = "Our most popular services",
                    fontFamily = PlusJakartaSansFontFamily,
                    fontSize = 11.sp,
                    color = Color(0xFFA0A0A0)
                )
            }

            TextButton(
                onClick = onViewAllClick,
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
                modifier = Modifier.testTag("quick_book_view_all_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "View All",
                        fontFamily = PlusJakartaSansFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = SalonGoldPrimary
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = SalonGoldPrimary,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(end = 8.dp)
        ) {
            items(services.take(5)) { service ->
                QuickBookCard(
                    service = service,
                    onBookClick = { onServiceClick(service) }
                )
            }
        }
    }
}

@Composable
fun PromotionalLuxuryCardsRow(
    salonConfig: SalonConfigEntity?,
    onExploreClick: () -> Unit,
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Card 1: "Be Your Best Self"
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clickable { onExploreClick() }
                .testTag("promo_card_best_self"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
            border = androidx.compose.foundation.BorderStroke(0.5.dp, DarkBorderColor)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                val menUrl = salonConfig?.menPromoImageUrl?.takeIf { it.isNotBlank() }
                if (menUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(menUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Men Grooming",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = PhotoFitUtils.getContentScaleForFit(salonConfig?.menPromoImageFit ?: "CROP_CENTER"),
                        alignment = PhotoFitUtils.getAlignmentForFit(salonConfig?.menPromoImageFit ?: "CROP_CENTER"),
                        placeholder = painterResource(id = R.drawable.img_premium_products),
                        error = painterResource(id = R.drawable.img_premium_products)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.img_premium_products),
                        contentDescription = "Premium Products",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xF00F0F0F),
                                    Color(0xBB0F0F0F),
                                    Color(0x33000000)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = salonConfig?.menPromoTitle ?: "Be Your Best Self",
                        fontFamily = PlayfairDisplayFontFamily,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = salonConfig?.menPromoSubtitle ?: "Expert care. Premium products. Unmatched experience.",
                        fontFamily = PlusJakartaSansFontFamily,
                        fontSize = 11.sp,
                        color = Color(0xFFC0C0C0),
                        modifier = Modifier.widthIn(max = 240.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = SalonGoldPill,
                        modifier = Modifier.clickable { onExploreClick() }
                    ) {
                        Text(
                            text = "Explore More",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color(0xFF141414),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Card 2: "Luxury Care for Every You"
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clickable { onBookClick() }
                .testTag("promo_card_luxury_care"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
            border = androidx.compose.foundation.BorderStroke(0.5.dp, DarkBorderColor)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                val womenUrl = salonConfig?.womenPromoImageUrl?.takeIf { it.isNotBlank() }
                    ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=800&q=80"
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(womenUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Luxury Care",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = PhotoFitUtils.getContentScaleForFit(salonConfig?.womenPromoImageFit ?: "CROP_CENTER"),
                    alignment = PhotoFitUtils.getAlignmentForFit(salonConfig?.womenPromoImageFit ?: "CROP_CENTER")
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xF00F0F0F),
                                    Color(0xBB0F0F0F),
                                    Color(0x33000000)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = salonConfig?.womenPromoTitle ?: "Luxury Care",
                        fontFamily = PlayfairDisplayFontFamily,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = salonConfig?.womenPromoSubtitle ?: "for Every You",
                        fontFamily = PlayfairDisplayFontFamily,
                        fontStyle = FontStyle.Italic,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium,
                        color = SalonGoldPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Relax • Rejuvenate • Reimagine",
                        fontFamily = PlusJakartaSansFontFamily,
                        fontSize = 11.sp,
                        color = Color(0xFFC0C0C0)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = SalonGoldPill,
                        modifier = Modifier.clickable { onBookClick() }
                    ) {
                        Text(
                            text = "Book Now",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color(0xFF141414),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

// Backwards compatibility alias for existing code
@Composable
fun UnisexPromoBannerRow(
    salonConfig: SalonConfigEntity?,
    onCategoryClick: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    PromotionalLuxuryCardsRow(
        salonConfig = salonConfig,
        onExploreClick = { onCategoryClick?.invoke("Beard Grooming") },
        onBookClick = { onCategoryClick?.invoke("Facial") },
        modifier = modifier
    )
}

@Composable
fun AdminDiscreetCornerButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
            .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f), CircleShape)
            .clickable(onClick = onClick)
            .testTag("admin_corner_circle_button")
            .testTag("switch_to_admin_app_button"),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Admin Access",
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f),
            modifier = Modifier.size(15.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSheet(
    notifications: List<NotificationEntity>,
    onDismiss: () -> Unit,
    onMarkAllRead: () -> Unit,
    onNotificationClick: (NotificationEntity) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.testTag("notification_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Real-Time Notifications",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (notifications.any { !it.isRead }) {
                    TextButton(onClick = onMarkAllRead) {
                        Text("Mark all read", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (notifications.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No notifications yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(notifications) { notif ->
                        ElevatedCard(
                            onClick = { onNotificationClick(notif) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = if (notif.isRead)
                                    MaterialTheme.colorScheme.surface
                                else
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (notif.isRead) MaterialTheme.colorScheme.surfaceVariant
                                            else MaterialTheme.colorScheme.primary
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = when (notif.type) {
                                            "NEW_BOOKING" -> Icons.Default.Schedule
                                            "PAYMENT_COLLECTED" -> Icons.Default.AttachMoney
                                            "STATUS_CHANGE" -> Icons.Default.CheckCircle
                                            else -> Icons.Default.Notifications
                                        },
                                        contentDescription = null,
                                        tint = if (notif.isRead) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = notif.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = if (notif.isRead) FontWeight.SemiBold else FontWeight.Bold
                                        )
                                        val timeStr = remember(notif.timestamp) {
                                            val sdf = SimpleDateFormat("h:mm a", Locale.US)
                                            sdf.format(Date(notif.timestamp))
                                        }
                                        Text(
                                            text = timeStr,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = notif.body,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AdminPasswordDialog(
    onDismiss: () -> Unit,
    onSubmitPassword: (String) -> Boolean
) {
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var hasError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("admin_password_dialog"),
        icon = {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(26.dp)
                )
            }
        },
        title = {
            Text(
                text = "Admin Mode Access",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Please enter the admin security password to access salon operations, live bookings, staff rosters, and financial cash ledgers.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        hasError = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_password_input_field"),
                    label = { Text("Admin Password") },
                    placeholder = { Text("Enter password...") },
                    singleLine = true,
                    isError = hasError,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordVisible = !passwordVisible },
                            modifier = Modifier.testTag("toggle_password_visibility_button")
                        ) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    }
                )

                if (hasError) {
                    Text(
                        text = "Incorrect password. Access denied.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Authorized salon administrative personnel only. Credentials are protected.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val success = onSubmitPassword(password)
                    if (!success) {
                        hasError = true
                    }
                },
                modifier = Modifier.testTag("submit_admin_password_button")
            ) {
                Text("Unlock Admin")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_admin_dialog_button")
            ) {
                Text("Cancel")
            }
        }
    )
}

