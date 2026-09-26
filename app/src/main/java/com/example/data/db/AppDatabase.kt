package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.AmenityEntity
import com.example.data.model.BookingEntity
import com.example.data.model.BookingStatus
import com.example.data.model.NotificationEntity
import com.example.data.model.SalonBreakEntity
import com.example.data.model.SalonConfigEntity
import com.example.data.model.SalonHolidayEntity
import com.example.data.model.SalonWorkingDayEntity
import com.example.data.model.ServiceEntity
import com.example.data.model.StaffEntity
import com.example.data.model.SupportMessageEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Database(
    entities = [
        SalonConfigEntity::class,
        AmenityEntity::class,
        SalonWorkingDayEntity::class,
        SalonBreakEntity::class,
        SalonHolidayEntity::class,
        ServiceEntity::class,
        StaffEntity::class,
        UserEntity::class,
        BookingEntity::class,
        SupportMessageEntity::class,
        NotificationEntity::class
    ],
    version = 9,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun salonDao(): SalonDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "salonsync_database"
                )
                    .addCallback(DatabaseCallback())
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        seedInitialData(database.salonDao())
                    }
                }
            }
        }

        suspend fun seedInitialData(dao: SalonDao) {
            // Salon Config
            dao.insertOrUpdateConfig(
                SalonConfigEntity(
                    id = 1,
                    salonName = "UNISEX SALON",
                    tagline = "PREMIUM CARE FOR EVERYONE",
                    address = "123 Wellness Street, Green Park, Delhi",
                    phone = "+91 98765 43210",
                    adminNotificationPhone = "+91 98765 43210",
                    enableWhatsAppAlerts = true,
                    enableSmsAlerts = true,
                    email = "concierge@unisexsalon.com",
                    about = "Premier unisex luxury lounge offering bespoke haircuts, precision styling, restorative facials, beard grooming, and luxury care. We take pride in personalized attention and an unhurried, serene atmosphere.",
                    cashNotice = "In-salon cash payment in Rupee (₹) accepted upon appointment completion. Please check in with the concierge 10 minutes prior to your service.",
                    heroImageUrl = "",
                    heroTitleLine1 = "Premium Care",
                    heroTitleLine2 = "for Everyone",
                    heroAccentText = "Everyone",
                    heroTags = "Hair | Skin | Grooming | Beauty",
                    heroCtaText = "Book Your Appointment",
                    menPromoTitle = "Be Your Best Self",
                    menPromoSubtitle = "Expert care. Premium products.",
                    menPromoImageUrl = "",
                    womenPromoTitle = "Luxury Care",
                    womenPromoSubtitle = "for Every You",
                    womenPromoImageUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=800&q=80",
                    adminPassword = "UnisexSalon#Admin98",
                    currencySymbol = "₹"
                )
            )

            // Amenities / Facilities (Adjustable by Admin)
            val initialAmenities = listOf(
                AmenityEntity("ac", "AC", "Full climate control for a relaxing sanctuary.", "AC", true, 1),
                AmenityEntity("wifi", "Wi-Fi", "Complimentary high-speed gigabit Wi-Fi.", "WIFI", true, 2),
                AmenityEntity("parking", "Parking", "Dedicated VIP valet parking at entrance.", "PARKING", true, 3),
                AmenityEntity("hygiene", "Hygiene", "Hospital-grade autoclave sterilization for all tools.", "HYGIENE", true, 4),
                AmenityEntity("lounge", "Lounge", "Luxury refreshment lounge with espresso and infused water.", "LOUNGE", true, 5)
            )
            dao.insertAmenities(initialAmenities)

            // Working Days (1 = Mon ... 7 = Sun)
            val workingDays = listOf(
                SalonWorkingDayEntity(dayOfWeek = 1, dayName = "Monday", isOpen = true, openTime = "09:00", closeTime = "19:00"),
                SalonWorkingDayEntity(dayOfWeek = 2, dayName = "Tuesday", isOpen = true, openTime = "09:00", closeTime = "19:00"),
                SalonWorkingDayEntity(dayOfWeek = 3, dayName = "Wednesday", isOpen = true, openTime = "09:00", closeTime = "19:00"),
                SalonWorkingDayEntity(dayOfWeek = 4, dayName = "Thursday", isOpen = true, openTime = "09:00", closeTime = "20:00"),
                SalonWorkingDayEntity(dayOfWeek = 5, dayName = "Friday", isOpen = true, openTime = "09:00", closeTime = "20:00"),
                SalonWorkingDayEntity(dayOfWeek = 6, dayName = "Saturday", isOpen = true, openTime = "09:00", closeTime = "18:00"),
                SalonWorkingDayEntity(dayOfWeek = 7, dayName = "Sunday", isOpen = true, openTime = "10:00", closeTime = "17:00")
            )
            dao.insertWorkingDays(workingDays)

            // Midday Break
            dao.insertBreak(
                SalonBreakEntity(
                    id = 1,
                    title = "Midday Team Luncheon & Sanitation",
                    startTime = "13:00",
                    endTime = "14:00",
                    appliesToAllStaff = true
                )
            )

            // Upcoming Salon Holiday / Closed Maintenance Day
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, 10)
            val holidayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            dao.insertHoliday(
                SalonHolidayEntity(
                    id = 1,
                    dateString = holidayFormat.format(cal.time),
                    name = "Autumn Aesthetic Summit",
                    reason = "Salon closed for team masterclasses & equipment calibration."
                )
            )

            // Services matching reference categories
            val services = listOf(
                ServiceEntity(
                    id = 1,
                    name = "Haircut",
                    category = "Hair Cut",
                    description = "Customized consultation, precision scissor cut, organic botanical wash, and volume blow-dry.",
                    durationMinutes = 30,
                    price = 299.00,
                    iconKey = "scissors",
                    imageUrl = "https://images.unsplash.com/photo-1622286342621-4bd786c2447c?auto=format&fit=crop&w=600&q=80",
                    isActive = true
                ),
                ServiceEntity(
                    id = 2,
                    name = "Hair Styling",
                    category = "Hair Styling",
                    description = "Formaldehyde-free smoothing treatment and bespoke red-carpet styling for effortless volume.",
                    durationMinutes = 60,
                    price = 499.00,
                    iconKey = "auto_awesome",
                    imageUrl = "https://images.unsplash.com/photo-1560869713-7d0a29430803?auto=format&fit=crop&w=600&q=80",
                    isActive = true
                ),
                ServiceEntity(
                    id = 3,
                    name = "Facial",
                    category = "Facial",
                    description = "Hyperbaric oxygen infusion with hyaluronic acid, peptides, and ultrasonic deep pore cleansing.",
                    durationMinutes = 60,
                    price = 799.00,
                    iconKey = "face",
                    imageUrl = "https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?auto=format&fit=crop&w=600&q=80",
                    isActive = true
                ),
                ServiceEntity(
                    id = 4,
                    name = "Beard Grooming",
                    category = "Beard Grooming",
                    description = "Razor contouring, beard sculpting, organic essential oil steam, and botanical balm conditioning.",
                    durationMinutes = 20,
                    price = 199.00,
                    iconKey = "content_cut",
                    imageUrl = "https://images.unsplash.com/photo-1621605815971-fbc98d665033?auto=format&fit=crop&w=600&q=80",
                    isActive = true
                ),
                ServiceEntity(
                    id = 5,
                    name = "Hair Spa",
                    category = "Hair Spa",
                    description = "Deep nourishing keratin mask, restorative warm herbal towel wrap, and scalp massage.",
                    durationMinutes = 45,
                    price = 699.00,
                    iconKey = "spa",
                    imageUrl = "https://images.unsplash.com/photo-1519699047748-de8e457a634e?auto=format&fit=crop&w=600&q=80",
                    isActive = true
                ),
                ServiceEntity(
                    id = 6,
                    name = "Colour & Highlights",
                    category = "Colour & Highlights",
                    description = "Hand-painted dimensional highlights paired with a nourishing tone glossing bath.",
                    durationMinutes = 90,
                    price = 1499.00,
                    iconKey = "palette",
                    imageUrl = "https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?auto=format&fit=crop&w=600&q=80",
                    isActive = true
                ),
                ServiceEntity(
                    id = 7,
                    name = "Luxury Makeup",
                    category = "Makeup",
                    description = "Full couture glam makeup application with luxury setting, contouring, and lash enhancements.",
                    durationMinutes = 60,
                    price = 1299.00,
                    iconKey = "brush",
                    imageUrl = "https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?auto=format&fit=crop&w=600&q=80",
                    isActive = true
                ),
                ServiceEntity(
                    id = 8,
                    name = "Deep Hydration Skin Care",
                    category = "Skin Care",
                    description = "Exfoliating botanical scrub, warm herbal steam, and shiatsu face, neck and shoulder massage.",
                    durationMinutes = 45,
                    price = 899.00,
                    iconKey = "spa",
                    imageUrl = "https://images.unsplash.com/photo-1512290900672-1f55b9e07895?auto=format&fit=crop&w=600&q=80",
                    isActive = true
                )
            )
            dao.insertServices(services)

            // Staff
            val staffList = listOf(
                StaffEntity(
                    id = 1,
                    name = "Elena Vance",
                    roleTitle = "Master Stylist & Creative Director",
                    specialty = "Couture Cuts, Parisian Layers & Bridal",
                    rating = 4.98f,
                    avatarColorHex = 0xFF8E4D3E,
                    isActive = true,
                    phone = "+1 (310) 555-0101",
                    email = "elena@auraluxurylounge.com"
                ),
                StaffEntity(
                    id = 2,
                    name = "Marcus Thorne",
                    roleTitle = "Colour Specialist & Balayage Master",
                    specialty = "Dimensional Blonding & Colour Correction",
                    rating = 4.95f,
                    avatarColorHex = 0xFF6D4C41,
                    isActive = true,
                    phone = "+1 (310) 555-0102",
                    email = "marcus@auraluxurylounge.com"
                ),
                StaffEntity(
                    id = 3,
                    name = "Chloe Moreau",
                    roleTitle = "Holistic Esthetician",
                    specialty = "Oxygen Facials, Dermal Therapy & Gua Sha",
                    rating = 4.96f,
                    avatarColorHex = 0xFF5D4037,
                    isActive = true,
                    phone = "+1 (310) 555-0103",
                    email = "chloe@auraluxurylounge.com"
                ),
                StaffEntity(
                    id = 4,
                    name = "David Sterling",
                    roleTitle = "Senior Barber & Texture Artisan",
                    specialty = "Fades, Razor Detailing & Scalp Health",
                    rating = 4.92f,
                    avatarColorHex = 0xFF4E342E,
                    isActive = true,
                    phone = "+1 (310) 555-0104",
                    email = "david@auraluxurylounge.com"
                )
            )
            dao.insertStaffList(staffList)

            // Users (Admin only seeded; customers create real accounts)
            val users = listOf(
                UserEntity(
                    id = "admin_1",
                    name = "Salon Admin",
                    phone = "+91 98765 43210",
                    email = "admin@unisexsalon.com",
                    role = UserRole.ADMIN,
                    avatarInitial = "A",
                    passwordHash = ""
                )
            )
            dao.insertUsers(users)

            // Today's date string
            val todayStr = holidayFormat.format(Date())
            val tomorrowCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
            val tomorrowStr = holidayFormat.format(tomorrowCal.time)

            // Sample Initial Bookings
            val sampleBookings = listOf(
                BookingEntity(
                    id = 1,
                    referenceNumber = "BK-70192",
                    customerId = "cust_1",
                    customerName = "Sophia Miller",
                    customerPhone = "+1 (310) 555-4821",
                    serviceId = 1,
                    serviceName = "Signature Haircut & Blowout",
                    servicePrice = 799.00,
                    serviceDurationMinutes = 60,
                    staffId = 1,
                    staffName = "Elena Vance",
                    bookingDate = todayStr,
                    startTime = "10:00",
                    endTime = "11:00",
                    status = BookingStatus.CONFIRMED,
                    notes = "Prefers organic shampoo if available.",
                    paymentMethod = "CASH",
                    isCashCollected = false
                ),
                BookingEntity(
                    id = 2,
                    referenceNumber = "BK-70193",
                    customerId = "cust_2",
                    customerName = "Lucas Rivera",
                    customerPhone = "+1 (310) 555-9832",
                    serviceId = 7,
                    serviceName = "Gentleman's Executive Cut & Shave",
                    servicePrice = 749.00,
                    serviceDurationMinutes = 45,
                    staffId = 4,
                    staffName = "David Sterling",
                    bookingDate = todayStr,
                    startTime = "11:30",
                    endTime = "12:15",
                    status = BookingStatus.IN_PROGRESS,
                    notes = "First time guest.",
                    paymentMethod = "CASH",
                    isCashCollected = false
                ),
                BookingEntity(
                    id = 3,
                    referenceNumber = "BK-70194",
                    customerId = "cust_1",
                    customerName = "Sophia Miller",
                    customerPhone = "+1 (310) 555-4821",
                    serviceId = 4,
                    serviceName = "Radiance Oxygen Facial",
                    servicePrice = 1499.00,
                    serviceDurationMinutes = 60,
                    staffId = 3,
                    staffName = "Chloe Moreau",
                    bookingDate = tomorrowStr,
                    startTime = "14:30",
                    endTime = "15:30",
                    status = BookingStatus.PENDING,
                    notes = "Sensitive skin consultation requested.",
                    paymentMethod = "CASH",
                    isCashCollected = false
                )
            )
            dao.insertBookings(sampleBookings)

            // Initial Support Messages
            val sampleMessages = listOf(
                SupportMessageEntity(
                    id = 1,
                    ticketCustomerId = "cust_1",
                    customerName = "Sophia Miller",
                    senderId = "cust_1",
                    senderName = "Sophia Miller",
                    senderRole = UserRole.CUSTOMER,
                    message = "Hello! I booked an Oxygen Facial for tomorrow afternoon. Are there any special skincare steps I should do beforehand?",
                    timestamp = System.currentTimeMillis() - 3600000 * 2,
                    isRead = true
                ),
                SupportMessageEntity(
                    id = 2,
                    ticketCustomerId = "cust_1",
                    customerName = "Sophia Miller",
                    senderId = "admin_1",
                    senderName = "Elena Vance (Salon Host)",
                    senderRole = UserRole.ADMIN,
                    message = "Hi Sophia! Just arrive with clean skin and pause active retinoids 24 hours prior. We will provide a hydrating collagen booster during your treatment!",
                    timestamp = System.currentTimeMillis() - 3600000,
                    isRead = true
                )
            )
            dao.insertSupportMessages(sampleMessages)

            // Initial Notifications
            val sampleNotifications = listOf(
                NotificationEntity(
                    id = 1,
                    recipientRole = UserRole.ADMIN,
                    title = "New Booking Request",
                    body = "Sophia Miller requested Radiance Oxygen Facial for tomorrow at 14:30 (BK-70194).",
                    type = "NEW_BOOKING",
                    referenceId = "BK-70194",
                    timestamp = System.currentTimeMillis() - 1800000,
                    isRead = false
                ),
                NotificationEntity(
                    id = 2,
                    recipientRole = UserRole.CUSTOMER,
                    recipientUserId = "cust_1",
                    title = "Appointment Confirmed",
                    body = "Your Signature Haircut with Elena Vance today at 10:00 AM is confirmed! Cash on arrival.",
                    type = "STATUS_CHANGE",
                    referenceId = "BK-70192",
                    timestamp = System.currentTimeMillis() - 7200000,
                    isRead = false
                )
            )
            dao.insertNotifications(sampleNotifications)
        }
    }
}
