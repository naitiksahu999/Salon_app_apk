package com.example.data.repository

import com.example.data.db.SalonDao
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
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import android.content.Context
import com.example.util.AppFeedbackHelper
import com.example.util.EmailNotificationService
import com.example.data.backend.FirebaseBackendService
import java.util.Date
import java.util.Locale
import java.util.UUID

class SalonRepository(
    private val dao: SalonDao,
    val firebaseBackend: FirebaseBackendService? = null,
    val context: Context? = null
) {

    // Config
    val salonConfig: Flow<SalonConfigEntity?> = dao.getSalonConfig()
    suspend fun updateSalonConfig(config: SalonConfigEntity) = dao.insertOrUpdateConfig(config)
    suspend fun ensureSalonConfig(): SalonConfigEntity {
        val existing = dao.getSalonConfigSync()
        if (existing != null) return existing
        val initial = SalonConfigEntity(
            id = 1,
            salonName = "UNISEX SALON",
            tagline = "PREMIUM CARE FOR EVERYONE",
            address = "123 Wellness Street, Green Park, Delhi",
            streetAddress = "123 Wellness Street",
            area = "Green Park",
            city = "Delhi",
            state = "Delhi",
            country = "India",
            pincode = "110016",
            latitude = 28.5583,
            longitude = 77.2028,
            phone = "+91 98765 43210",
            adminNotificationPhone = "+91 98765 43210",
            adminGmail = "admin.unisexsalon@gmail.com",
            adminPhone = "+91 98765 43210",
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
            storefrontImageUrl = "",
            storefrontImageFit = "CROP_CENTER",
            heroImageFit = "CROP_CENTER",
            menPromoImageFit = "CROP_CENTER",
            womenPromoImageFit = "CROP_CENTER",
            adminPassword = "UnisexSalon#Admin98",
            currencySymbol = "₹"
        )
        dao.insertOrUpdateConfig(initial)
        return initial
    }

    fun isAdminDeviceRemembered(): Boolean {
        if (context == null) return false
        val prefs = context.getSharedPreferences("admin_device_prefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("is_admin_device_remembered", false)
    }

    fun setAdminDeviceRemembered(remembered: Boolean) {
        if (context == null) return
        val prefs = context.getSharedPreferences("admin_device_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("is_admin_device_remembered", remembered).apply()
    }

    // Amenities / Facilities (Adjustable by Admin)
    val allAmenities: Flow<List<AmenityEntity>> = dao.getAllAmenities()
    val activeAmenities: Flow<List<AmenityEntity>> = dao.getActiveAmenities()
    suspend fun getAllAmenitiesSync(): List<AmenityEntity> = dao.getAllAmenitiesSync()
    suspend fun insertOrUpdateAmenity(amenity: AmenityEntity) = dao.insertOrUpdateAmenity(amenity)
    suspend fun updateAmenity(amenity: AmenityEntity) = dao.updateAmenity(amenity)
    suspend fun deleteAmenity(id: String) = dao.deleteAmenity(id)

    suspend fun ensureInitialAmenities() {
        if (dao.getAllAmenitiesSync().isEmpty()) {
            val initial = listOf(
                AmenityEntity("ac", "AC", "Full climate control for a relaxing sanctuary.", "AC", true, 1),
                AmenityEntity("wifi", "Wi-Fi", "Complimentary high-speed gigabit Wi-Fi.", "WIFI", true, 2),
                AmenityEntity("parking", "Parking", "Dedicated VIP valet parking at entrance.", "PARKING", true, 3),
                AmenityEntity("hygiene", "Hygiene", "Hospital-grade autoclave sterilization for all tools.", "HYGIENE", true, 4),
                AmenityEntity("lounge", "Lounge", "Luxury refreshment lounge with espresso and infused water.", "LOUNGE", true, 5)
            )
            dao.insertAmenities(initial)
        }
    }

    suspend fun ensureWorkingDays() {
        if (dao.getAllWorkingDaysSync().isEmpty()) {
            val defaultDays = listOf(
                SalonWorkingDayEntity(dayOfWeek = 1, dayName = "Monday", isOpen = true, openTime = "09:00", closeTime = "19:00"),
                SalonWorkingDayEntity(dayOfWeek = 2, dayName = "Tuesday", isOpen = true, openTime = "09:00", closeTime = "19:00"),
                SalonWorkingDayEntity(dayOfWeek = 3, dayName = "Wednesday", isOpen = true, openTime = "09:00", closeTime = "19:00"),
                SalonWorkingDayEntity(dayOfWeek = 4, dayName = "Thursday", isOpen = true, openTime = "09:00", closeTime = "20:00"),
                SalonWorkingDayEntity(dayOfWeek = 5, dayName = "Friday", isOpen = true, openTime = "09:00", closeTime = "20:00"),
                SalonWorkingDayEntity(dayOfWeek = 6, dayName = "Saturday", isOpen = true, openTime = "09:00", closeTime = "18:00"),
                SalonWorkingDayEntity(dayOfWeek = 7, dayName = "Sunday", isOpen = true, openTime = "10:00", closeTime = "17:00")
            )
            dao.insertWorkingDays(defaultDays)
        }
    }

    // Working Hours & Breaks & Holidays
    val workingDays: Flow<List<SalonWorkingDayEntity>> = dao.getAllWorkingDays()
    suspend fun updateWorkingDay(day: SalonWorkingDayEntity) = dao.updateWorkingDay(day)

    val breaks: Flow<List<SalonBreakEntity>> = dao.getAllBreaks()
    suspend fun addBreak(salonBreak: SalonBreakEntity) = dao.insertBreak(salonBreak)
    suspend fun deleteBreak(id: Long) = dao.deleteBreak(id)

    val holidays: Flow<List<SalonHolidayEntity>> = dao.getAllHolidays()
    suspend fun addHoliday(holiday: SalonHolidayEntity) = dao.insertHoliday(holiday)
    suspend fun deleteHoliday(id: Long) = dao.deleteHoliday(id)

    // Services
    val allServices: Flow<List<ServiceEntity>> = dao.getAllServices()
    val activeServices: Flow<List<ServiceEntity>> = dao.getActiveServices()
    suspend fun addService(service: ServiceEntity) = dao.insertService(service)
    suspend fun updateService(service: ServiceEntity) = dao.updateService(service)
    suspend fun deleteService(service: ServiceEntity) = dao.deleteService(service)

    // Staff
    val allStaff: Flow<List<StaffEntity>> = dao.getAllStaff()
    val activeStaff: Flow<List<StaffEntity>> = dao.getActiveStaff()
    suspend fun addStaff(staff: StaffEntity) = dao.insertStaff(staff)
    suspend fun updateStaff(staff: StaffEntity) = dao.updateStaff(staff)
    suspend fun deleteStaff(staff: StaffEntity) = dao.deleteStaff(staff)

    // Users & Authentication
    val allUsers: Flow<List<UserEntity>> = dao.getAllUsers()
    suspend fun getUser(id: String) = dao.getUserById(id)
    suspend fun saveUser(user: UserEntity) = dao.insertUser(user)

    suspend fun registerUser(
        name: String,
        email: String,
        phone: String,
        passwordRaw: String
    ): Result<UserEntity> {
        val trimmedName = name.trim()
        val trimmedEmail = email.trim().lowercase(Locale.US)
        val trimmedPhone = phone.trim()

        if (trimmedName.length < 2) {
            return Result.failure(IllegalArgumentException("Please enter a valid full name."))
        }
        if (!trimmedEmail.contains("@") || !trimmedEmail.contains(".")) {
            return Result.failure(IllegalArgumentException("Please enter a valid Gmail / Email address."))
        }
        val cleanPhone = trimmedPhone.filter { it.isDigit() || it == '+' }
        if (cleanPhone.length < 8) {
            return Result.failure(IllegalArgumentException("Please enter a valid 10-digit mobile number."))
        }
        if (passwordRaw.length < 6) {
            return Result.failure(IllegalArgumentException("Password must be at least 6 characters."))
        }

        // Check uniqueness in local secure DB
        val existingEmail = dao.getUserByEmail(trimmedEmail)
        if (existingEmail != null) {
            return Result.failure(IllegalStateException("An account with email $trimmedEmail is already registered. Please sign in."))
        }
        val existingPhone = dao.getUserByPhone(trimmedPhone)
        if (existingPhone != null) {
            return Result.failure(IllegalStateException("An account with mobile $trimmedPhone is already registered. Please sign in."))
        }

        val passwordHash = FirebaseBackendService.hashPassword(passwordRaw)
        val newId = "cust_" + UUID.randomUUID().toString().take(8)
        val initial = trimmedName.take(1).uppercase(Locale.US)

        val newUser = UserEntity(
            id = newId,
            name = trimmedName,
            phone = trimmedPhone,
            email = trimmedEmail,
            role = UserRole.CUSTOMER,
            avatarInitial = initial,
            passwordHash = passwordHash,
            createdAt = System.currentTimeMillis()
        )

        dao.insertUser(newUser)

        // Try registering / syncing to Firebase Auth & Firestore
        try {
            firebaseBackend?.registerUserWithFirebaseAuth(trimmedEmail, passwordRaw)
            firebaseBackend?.syncUserToCloud(newUser)
        } catch (_: Exception) { }

        return Result.success(newUser)
    }

    suspend fun loginUser(identifier: String, passwordRaw: String): Result<UserEntity> {
        val cleanIdentifier = identifier.trim()
        if (cleanIdentifier.isEmpty()) {
            return Result.failure(IllegalArgumentException("Please enter your Gmail / Email or mobile number."))
        }
        if (passwordRaw.isEmpty()) {
            return Result.failure(IllegalArgumentException("Please enter your password."))
        }

        val user = dao.getUserByIdentifier(cleanIdentifier)
            ?: return Result.failure(IllegalArgumentException("No account found for '$cleanIdentifier'. Please create an account."))

        val hash = FirebaseBackendService.hashPassword(passwordRaw)
        if (user.passwordHash.isNotEmpty() && user.passwordHash != hash) {
            return Result.failure(IllegalArgumentException("Incorrect password. Please verify your credentials."))
        }

        // Background sync to Firebase
        try {
            firebaseBackend?.syncUserToCloud(user)
        } catch (_: Exception) { }

        return Result.success(user)
    }

    suspend fun signInWithGoogle(name: String, email: String): Result<UserEntity> {
        val cleanEmail = email.trim().lowercase(Locale.US)
        val cleanName = name.trim().ifEmpty { "Google User" }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches() || !cleanEmail.contains(".")) {
            return Result.failure(IllegalArgumentException("Invalid Google account email format."))
        }

        val existing = dao.getUserByIdentifier(cleanEmail)
        val user = if (existing != null) {
            val updated = existing.copy(
                name = cleanName,
                avatarInitial = cleanName.take(1).uppercase(Locale.US)
            )
            dao.updateUser(updated)
            updated
        } else {
            val newId = "google_" + UUID.randomUUID().toString().take(8)
            val newUser = UserEntity(
                id = newId,
                name = cleanName,
                phone = "",
                email = cleanEmail,
                role = UserRole.CUSTOMER,
                avatarInitial = cleanName.take(1).uppercase(Locale.US),
                createdAt = System.currentTimeMillis()
            )
            dao.insertUser(newUser)
            newUser
        }

        try {
            firebaseBackend?.syncUserToCloud(user)
        } catch (_: Exception) { }

        return Result.success(user)
    }

    suspend fun updateUserProfile(
        userId: String,
        name: String,
        phone: String,
        email: String,
        preferredServices: String? = null,
        gender: String? = null,
        birthday: String? = null,
        notes: String? = null
    ): Result<UserEntity> {
        val existing = dao.getUserById(userId)
            ?: return Result.failure(IllegalArgumentException("User account not found."))

        val cleanName = name.trim().ifEmpty { existing.name }
        val cleanEmail = email.trim().lowercase(Locale.US)
        val cleanPhone = phone.trim()

        if (cleanEmail.isNotEmpty() && (!android.util.Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches() || !cleanEmail.contains("."))) {
            return Result.failure(IllegalArgumentException("Invalid email format. Please enter a valid email address (e.g. name@gmail.com)."))
        }

        val digitsOnly = cleanPhone.filter { it.isDigit() }
        if (cleanPhone.isNotEmpty() && digitsOnly.length < 10) {
            return Result.failure(IllegalArgumentException("Invalid phone number. Please enter a valid 10-digit mobile number."))
        }

        val updated = existing.copy(
            name = cleanName,
            phone = cleanPhone.ifEmpty { existing.phone },
            email = cleanEmail.ifEmpty { existing.email },
            avatarInitial = (cleanName.take(1).uppercase(Locale.US)).ifEmpty { existing.avatarInitial },
            preferredServices = preferredServices ?: existing.preferredServices,
            gender = gender ?: existing.gender,
            birthday = birthday ?: existing.birthday,
            notes = notes ?: existing.notes
        )
        dao.updateUser(updated)
        try {
            firebaseBackend?.syncUserToCloud(updated)
        } catch (_: Exception) { }

        return Result.success(updated)
    }

    suspend fun resetUserPassword(identifier: String, newPasswordRaw: String): Result<UserEntity> {
        val cleanIdentifier = identifier.trim()
        val user = dao.getUserByIdentifier(cleanIdentifier)
            ?: return Result.failure(IllegalArgumentException("No account registered for '$cleanIdentifier'."))
        if (newPasswordRaw.length < 6) {
            return Result.failure(IllegalArgumentException("New password must be at least 6 characters."))
        }
        val newHash = FirebaseBackendService.hashPassword(newPasswordRaw)
        val updated = user.copy(passwordHash = newHash)
        dao.updateUser(updated)
        try {
            firebaseBackend?.syncUserToCloud(updated)
        } catch (_: Exception) { }
        return Result.success(updated)
    }

    suspend fun updateAdminContact(adminGmail: String, adminPhone: String): Result<Unit> {
        val current = dao.getSalonConfigSync() ?: return Result.failure(IllegalStateException("Salon configuration not found."))
        val updated = current.copy(
            adminGmail = adminGmail.trim(),
            adminPhone = adminPhone.trim(),
            updatedAt = System.currentTimeMillis()
        )
        dao.insertOrUpdateConfig(updated)
        return Result.success(Unit)
    }

    suspend fun getFirstCustomerUser(): UserEntity? {
        val all = dao.getAllUsersSync()
        return all.firstOrNull { it.role == UserRole.CUSTOMER }
    }

    suspend fun syncAllBookingsToCloud(): Int {
        val fb = firebaseBackend ?: return 0
        val bookings = dao.getAllBookingsSync()
        var count = 0
        for (b in bookings) {
            if (fb.syncBookingToCloud(b)) {
                count++
            }
        }
        return count
    }

    // Bookings
    val allBookings: Flow<List<BookingEntity>> = dao.getAllBookings()
    fun getCustomerBookings(customerId: String): Flow<List<BookingEntity>> =
        dao.getBookingsForCustomer(customerId)

    // Notifications
    val allNotifications: Flow<List<NotificationEntity>> = dao.getAllNotifications()
    fun getNotificationsForRoleOrUser(role: String, userId: String?): Flow<List<NotificationEntity>> =
        dao.getNotificationsForRoleOrUser(role, userId)

    suspend fun markNotificationRead(id: Long) = dao.markNotificationAsRead(id)
    suspend fun markAllNotificationsRead(role: String, userId: String?) =
        dao.markAllNotificationsAsRead(role, userId)

    // Support
    fun getSupportMessages(customerId: String): Flow<List<SupportMessageEntity>> =
        dao.getMessagesForCustomer(customerId)
    val allSupportMessages: Flow<List<SupportMessageEntity>> = dao.getAllMessages()

    suspend fun sendSupportMessage(
        ticketCustomerId: String,
        customerName: String,
        senderId: String,
        senderName: String,
        senderRole: String,
        messageText: String
    ) {
        val msg = SupportMessageEntity(
            ticketCustomerId = ticketCustomerId,
            customerName = customerName,
            senderId = senderId,
            senderName = senderName,
            senderRole = senderRole,
            message = messageText,
            timestamp = System.currentTimeMillis(),
            isRead = false
        )
        dao.insertSupportMessage(msg)

        // Generate in-app notification for counterparty
        if (senderRole == UserRole.CUSTOMER) {
            dao.insertNotification(
                NotificationEntity(
                    recipientRole = UserRole.ADMIN,
                    title = "Support Inquiry from $customerName",
                    body = messageText.take(80),
                    type = "SUPPORT",
                    referenceId = ticketCustomerId,
                    timestamp = System.currentTimeMillis()
                )
            )
        } else {
            dao.insertNotification(
                NotificationEntity(
                    recipientRole = UserRole.CUSTOMER,
                    recipientUserId = ticketCustomerId,
                    title = "Salon Support Reply",
                    body = messageText.take(80),
                    type = "SUPPORT",
                    referenceId = ticketCustomerId,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    // Helper: Convert "HH:mm" or "h:mm a" to minutes since midnight
    fun timeToMinutes(timeStr: String): Int {
        val clean = timeStr.trim()
        val isPm = clean.endsWith("PM", ignoreCase = true)
        val isAm = clean.endsWith("AM", ignoreCase = true)
        val withoutAmPm = clean.replace("(?i)(AM|PM)".toRegex(), "").trim()
        val parts = withoutAmPm.split(":")
        var hours = parts.getOrNull(0)?.toIntOrNull() ?: 0
        val minutes = parts.getOrNull(1)?.toIntOrNull() ?: 0
        if (isPm && hours < 12) hours += 12
        if (isAm && hours == 12) hours = 0
        return hours * 60 + minutes
    }

    // Helper: Convert minutes since midnight to 12-hour AM/PM format (e.g. "9:00 AM", "1:30 PM")
    fun minutesTo12HourTime(totalMinutes: Int): String {
        val totalHours = ((totalMinutes / 60) % 24 + 24) % 24
        val mins = ((totalMinutes % 60) + 60) % 60
        val amPm = if (totalHours < 12) "AM" else "PM"
        val displayHour = when (val h = totalHours % 12) {
            0 -> 12
            else -> h
        }
        return String.format(Locale.US, "%d:%02d %s", displayHour, mins, amPm)
    }

    // Helper: Universal 12-hour AM/PM formatter
    fun formatTo12Hour(timeStr: String): String {
        if (timeStr.isEmpty()) return ""
        if (timeStr.contains("AM", ignoreCase = true) || timeStr.contains("PM", ignoreCase = true)) {
            return timeStr
        }
        return minutesTo12HourTime(timeToMinutes(timeStr))
    }

    // Helper: Get Day of Week (1=Monday ... 7=Sunday) for "YYYY-MM-DD"
    private fun getDayOfWeekForDate(dateString: String): Int {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = format.parse(dateString) ?: return 1
        val cal = Calendar.getInstance()
        cal.time = date
        return when (cal.get(Calendar.DAY_OF_WEEK)) {
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

    /**
     * Booking Engine: Dynamic Time Slot Computation
     * Checks salon operating hours, breaks, holidays, active bookings,
     * AND strictly excludes time slots that have already passed for TODAY.
     */
    suspend fun getAvailableTimeSlots(
        dateString: String,
        staffId: Long? = null,
        durationMinutes: Int
    ): List<String> {
        // 1. Check if date is a holiday
        val holidays = dao.getAllHolidaysSync()
        if (holidays.any { it.dateString == dateString }) {
            return emptyList()
        }

        // 2. Check salon working day
        val dayOfWeek = getDayOfWeekForDate(dateString)
        val workingDay = dao.getWorkingDaySync(dayOfWeek)
        if (workingDay == null || !workingDay.isOpen) {
            return emptyList()
        }

        val salonOpenMin = timeToMinutes(workingDay.openTime)
        val salonCloseMin = timeToMinutes(workingDay.closeTime)

        val allStaff = dao.getAllActiveStaffSync()
        val targetStaffList = if (staffId != null && staffId > 0) {
            allStaff.filter { it.id == staffId }
        } else {
            allStaff
        }
        if (targetStaffList.isEmpty()) return emptyList()

        val allBreaks = dao.getAllBreaksSync()
        val allBookings = dao.getActiveBookingsForDateSync(dateString)

        // Check if date is today to filter out passed time slots
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val isToday = dateString == todayStr
        val nowCal = Calendar.getInstance()
        val currentTotalMinutes = nowCal.get(Calendar.HOUR_OF_DAY) * 60 + nowCal.get(Calendar.MINUTE)

        val availableSlots = mutableListOf<String>()
        var slotStart = salonOpenMin

        while (slotStart + durationMinutes <= salonCloseMin) {
            val slotEnd = slotStart + durationMinutes

            // If selected date is today, omit past time slots (with 10-min buffer)
            if (isToday && slotStart <= currentTotalMinutes - 10) {
                slotStart += 30
                continue
            }

            // Check if ANY staff in targetStaffList is free for this slot
            val hasAvailableStaff = targetStaffList.any { st ->
                val breaks = allBreaks.filter {
                    it.appliesToAllStaff || it.staffId == null || it.staffId == st.id
                }
                val hasBreakConflict = breaks.any { brk ->
                    val bStart = timeToMinutes(brk.startTime)
                    val bEnd = timeToMinutes(brk.endTime)
                    maxOf(slotStart, bStart) < minOf(slotEnd, bEnd)
                }
                if (hasBreakConflict) return@any false

                val bookings = allBookings.filter { it.staffId == st.id }
                val hasBookingConflict = bookings.any { bk ->
                    val bkStart = timeToMinutes(bk.startTime)
                    val bkEnd = timeToMinutes(bk.endTime)
                    maxOf(slotStart, bkStart) < minOf(slotEnd, bkEnd)
                }
                !hasBookingConflict
            }

            if (hasAvailableStaff) {
                availableSlots.add(minutesTo12HourTime(slotStart))
            }

            slotStart += 30 // 30-minute interval increments
        }

        return availableSlots
    }

    /**
     * Booking Engine: Strict Backend Validation & Transactional Insertion
     * Validates that the requested booking does not violate salon hours, holidays,
     * breaks, or double-book the staff member.
     */
    suspend fun validateAndCreateBooking(
        customerId: String,
        customerName: String,
        customerPhone: String,
        customerEmail: String = "",
        service: ServiceEntity,
        staff: StaffEntity,
        bookingDate: String,
        startTime: String,
        notes: String = ""
    ): Result<BookingEntity> {
        val duration = service.durationMinutes
        val startMin = timeToMinutes(startTime)
        val endMin = startMin + duration
        val formattedStartTime = minutesTo12HourTime(startMin)
        val formattedEndTime = minutesTo12HourTime(endMin)

        // 1. Validate if booking date is today and time has passed (with 40-min grace period)
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        if (bookingDate == todayStr) {
            val nowCal = Calendar.getInstance()
            val currentTotalMinutes = nowCal.get(Calendar.HOUR_OF_DAY) * 60 + nowCal.get(Calendar.MINUTE)
            if (startMin < currentTotalMinutes - 40) {
                return Result.failure(IllegalStateException("Selected appointment time ($formattedStartTime) has already passed."))
            }
        }

        // 2. Validate Holiday
        val holidays = dao.getAllHolidaysSync()
        val holiday = holidays.find { it.dateString == bookingDate }
        if (holiday != null) {
            return Result.failure(
                IllegalStateException("The salon is closed on this date for: ${holiday.name} (${holiday.reason})")
            )
        }

        // 3. Validate Salon Working Hours
        val dayOfWeek = getDayOfWeekForDate(bookingDate)
        val workingDay = dao.getWorkingDaySync(dayOfWeek)
            ?: return Result.failure(IllegalStateException("Salon operating schedule not found."))

        if (!workingDay.isOpen) {
            return Result.failure(IllegalStateException("The salon is closed on ${workingDay.dayName}s."))
        }

        val openMin = timeToMinutes(workingDay.openTime)
        val closeMin = timeToMinutes(workingDay.closeTime)

        if (startMin < openMin || endMin > closeMin) {
            return Result.failure(
                IllegalStateException(
                    "Selected time $formattedStartTime - $formattedEndTime is outside salon hours (${formatTo12Hour(workingDay.openTime)} - ${formatTo12Hour(workingDay.closeTime)})."
                )
            )
        }

        // 4. Validate Breaks & Staff Availability
        var finalStaff = staff
        val allActiveStaff = dao.getAllActiveStaffSync()
        val allBreaks = dao.getAllBreaksSync()
        val dateBookings = dao.getActiveBookingsForDateSync(bookingDate)

        fun isStaffFree(st: StaffEntity): Boolean {
            val stBreaks = allBreaks.filter {
                it.appliesToAllStaff || it.staffId == null || it.staffId == st.id
            }
            val breakConflict = stBreaks.any { brk ->
                maxOf(startMin, timeToMinutes(brk.startTime)) < minOf(endMin, timeToMinutes(brk.endTime))
            }
            if (breakConflict) return false

            val stBookings = dateBookings.filter { it.staffId == st.id }
            val bookConflict = stBookings.any { ex ->
                maxOf(startMin, timeToMinutes(ex.startTime)) < minOf(endMin, timeToMinutes(ex.endTime))
            }
            return !bookConflict
        }

        if (!isStaffFree(finalStaff)) {
            // Check if another active specialist is free
            val alternate = allActiveStaff.firstOrNull { isStaffFree(it) }
            if (alternate != null) {
                finalStaff = alternate
            } else {
                return Result.failure(
                    IllegalStateException(
                        "All specialists are booked for $formattedStartTime on this date. Please select another time slot or date."
                    )
                )
            }
        }

        // 6. Generate Reference ID & Save (Formatted in 12-hour AM/PM)
        val refNumber = "BK-" + UUID.randomUUID().toString().take(6).uppercase(Locale.US)
        val resolvedCustName = customerName.trim().ifEmpty { "Guest Client" }
        val resolvedCustPhone = customerPhone.trim().ifEmpty { "+91 98765 00000" }
        val resolvedCustEmail = customerEmail.trim().ifEmpty { "client@unisexsalon.com" }

        val newBooking = BookingEntity(
            referenceNumber = refNumber,
            customerId = customerId,
            customerName = resolvedCustName,
            customerPhone = resolvedCustPhone,
            customerEmail = resolvedCustEmail,
            serviceId = service.id,
            serviceName = service.name,
            servicePrice = service.price,
            serviceDurationMinutes = duration,
            staffId = finalStaff.id,
            staffName = finalStaff.name,
            bookingDate = bookingDate,
            startTime = formattedStartTime,
            endTime = formattedEndTime,
            status = BookingStatus.PENDING,
            notes = notes,
            paymentMethod = "CASH",
            isCashCollected = false,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        val insertedId = dao.insertBooking(newBooking)
        val created = newBooking.copy(id = insertedId)

        // Notification for Customer
        dao.insertNotification(
            NotificationEntity(
                recipientRole = UserRole.CUSTOMER,
                recipientUserId = customerId,
                title = "Booking Confirmed ($refNumber)",
                body = "Your appointment for ${service.name} with ${staff.name} on $bookingDate at $formattedStartTime has been reserved. Email notification dispatched to your registered Gmail and salon admin.",
                type = "NEW_BOOKING",
                referenceId = refNumber,
                timestamp = System.currentTimeMillis()
            )
        )

        // Email notification dispatch & feedback
        context?.let { ctx ->
            val config = dao.getSalonConfigSync()
            EmailNotificationService.sendBookingEmailNotification(
                context = ctx,
                booking = created,
                salonConfig = config,
                isPostponed = false
            )
            AppFeedbackHelper.triggerSuccess(ctx)
        }

        // Cloud sync to Firebase
        try {
            firebaseBackend?.syncBookingToCloud(created)
        } catch (_: Exception) { }

        return Result.success(created)
    }

    // Admin Operations
    suspend fun acceptBooking(bookingId: Long): Result<Unit> {
        val booking = dao.getBookingById(bookingId)
            ?: return Result.failure(IllegalArgumentException("Booking not found"))

        val updated = booking.copy(
            status = BookingStatus.CONFIRMED,
            updatedAt = System.currentTimeMillis()
        )
        dao.updateBooking(updated)

        dao.insertNotification(
            NotificationEntity(
                recipientRole = UserRole.CUSTOMER,
                recipientUserId = booking.customerId,
                title = "Appointment Confirmed!",
                body = "Great news! Your booking (${booking.referenceNumber}) with ${booking.staffName} on ${booking.bookingDate} at ${booking.startTime} has been confirmed.",
                type = "STATUS_CHANGE",
                referenceId = booking.referenceNumber,
                timestamp = System.currentTimeMillis()
            )
        )
        context?.let { ctx ->
            val config = dao.getSalonConfigSync()
            EmailNotificationService.sendBookingEmailNotification(
                context = ctx,
                booking = updated,
                salonConfig = config,
                isPostponed = false
            )
            AppFeedbackHelper.triggerSuccess(ctx)
        }
        return Result.success(Unit)
    }

    suspend fun rejectBooking(bookingId: Long, reason: String): Result<Unit> {
        val booking = dao.getBookingById(bookingId)
            ?: return Result.failure(IllegalArgumentException("Booking not found"))

        val updated = booking.copy(
            status = BookingStatus.REJECTED,
            rejectReason = reason,
            updatedAt = System.currentTimeMillis()
        )
        dao.updateBooking(updated)

        dao.insertNotification(
            NotificationEntity(
                recipientRole = UserRole.CUSTOMER,
                recipientUserId = booking.customerId,
                title = "Booking Update (${booking.referenceNumber})",
                body = "Your booking request could not be accommodated: $reason",
                type = "STATUS_CHANGE",
                referenceId = booking.referenceNumber,
                timestamp = System.currentTimeMillis()
            )
        )
        context?.let { ctx ->
            val config = dao.getSalonConfigSync()
            EmailNotificationService.sendBookingEmailNotification(
                context = ctx,
                booking = updated,
                salonConfig = config,
                isPostponed = false,
                postponeReason = "Declined: $reason"
            )
            AppFeedbackHelper.triggerError(ctx)
        }
        return Result.success(Unit)
    }

    suspend fun rescheduleBooking(
        bookingId: Long,
        newDate: String,
        newStartTime: String
    ): Result<Unit> {
        val booking = dao.getBookingById(bookingId)
            ?: return Result.failure(IllegalArgumentException("Booking not found"))

        val duration = booking.serviceDurationMinutes
        val startMin = timeToMinutes(newStartTime)
        val endMin = startMin + duration
        val formattedStartTime = minutesTo12HourTime(startMin)
        val formattedEndTime = minutesTo12HourTime(endMin)

        // Conflict check against other bookings (excluding self)
        val existingBookings = dao.getActiveBookingsForDateAndStaffSync(newDate, booking.staffId)
        for (existing in existingBookings) {
            if (existing.id == booking.id) continue
            val exStart = timeToMinutes(existing.startTime)
            val exEnd = timeToMinutes(existing.endTime)
            if (maxOf(startMin, exStart) < minOf(endMin, exEnd)) {
                return Result.failure(
                    IllegalStateException("Time slot conflicts with an existing appointment.")
                )
            }
        }

        val updated = booking.copy(
            bookingDate = newDate,
            startTime = formattedStartTime,
            endTime = formattedEndTime,
            status = BookingStatus.CONFIRMED,
            updatedAt = System.currentTimeMillis()
        )
        dao.updateBooking(updated)

        dao.insertNotification(
            NotificationEntity(
                recipientRole = UserRole.CUSTOMER,
                recipientUserId = booking.customerId,
                title = "Appointment Rescheduled",
                body = "Your booking (${booking.referenceNumber}) has been moved to $newDate at $formattedStartTime.",
                type = "STATUS_CHANGE",
                referenceId = booking.referenceNumber,
                timestamp = System.currentTimeMillis()
            )
        )

        context?.let { ctx ->
            val config = dao.getSalonConfigSync()
            EmailNotificationService.sendBookingEmailNotification(
                context = ctx,
                booking = updated,
                salonConfig = config,
                isPostponed = false
            )
            AppFeedbackHelper.triggerSuccess(ctx)
        }

        return Result.success(Unit)
    }

    suspend fun postponeBooking(
        bookingId: Long,
        newDate: String,
        newStartTime: String,
        reason: String
    ): Result<Unit> {
        val booking = dao.getBookingById(bookingId)
            ?: return Result.failure(IllegalArgumentException("Booking not found"))

        val duration = booking.serviceDurationMinutes
        val startMin = timeToMinutes(newStartTime)
        val endMin = startMin + duration
        val formattedStartTime = minutesTo12HourTime(startMin)
        val formattedEndTime = minutesTo12HourTime(endMin)

        // Conflict check against other bookings (excluding self)
        val existingBookings = dao.getActiveBookingsForDateAndStaffSync(newDate, booking.staffId)
        for (existing in existingBookings) {
            if (existing.id == booking.id) continue
            val exStart = timeToMinutes(existing.startTime)
            val exEnd = timeToMinutes(existing.endTime)
            if (maxOf(startMin, exStart) < minOf(endMin, exEnd)) {
                return Result.failure(
                    IllegalStateException("Time slot conflicts with an existing appointment for ${booking.staffName}.")
                )
            }
        }

        val updated = booking.copy(
            bookingDate = newDate,
            startTime = formattedStartTime,
            endTime = formattedEndTime,
            status = BookingStatus.POSTPONED,
            isPostponed = true,
            postponedNewDate = newDate,
            postponedNewTime = formattedStartTime,
            postponeReason = reason,
            updatedAt = System.currentTimeMillis()
        )
        dao.updateBooking(updated)

        dao.insertNotification(
            NotificationEntity(
                recipientRole = UserRole.CUSTOMER,
                recipientUserId = booking.customerId,
                title = "⚠️ Booking Postponed (${booking.referenceNumber})",
                body = "Your booking for ${booking.serviceName} has been postponed to $newDate at $formattedStartTime. Reason: $reason. Email notification dispatched to your registered Gmail.",
                type = "STATUS_CHANGE",
                referenceId = booking.referenceNumber,
                timestamp = System.currentTimeMillis()
            )
        )

        context?.let { ctx ->
            val config = dao.getSalonConfigSync()
            EmailNotificationService.sendBookingEmailNotification(
                context = ctx,
                booking = updated,
                salonConfig = config,
                isPostponed = true,
                postponeReason = reason
            )
            AppFeedbackHelper.triggerNotification(ctx)
        }

        return Result.success(Unit)
    }

    suspend fun isCustomSlotAvailable(
        bookingDate: String,
        startTime: String,
        staffId: Long?,
        durationMins: Int
    ): Pair<Boolean, String> {
        val startMin = timeToMinutes(startTime)
        val endMin = startMin + durationMins
        val formattedStartTime = minutesTo12HourTime(startMin)
        val formattedEndTime = minutesTo12HourTime(endMin)

        // 1. Check if date/time already passed today
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        if (bookingDate == todayStr) {
            val nowCal = Calendar.getInstance()
            val currentTotalMinutes = nowCal.get(Calendar.HOUR_OF_DAY) * 60 + nowCal.get(Calendar.MINUTE)
            if (startMin <= currentTotalMinutes) {
                return Pair(false, "Selected time ($formattedStartTime) has already passed for today.")
            }
        }

        // 2. Check salon holiday
        val holidays = dao.getAllHolidaysSync()
        val holiday = holidays.find { it.dateString == bookingDate }
        if (holiday != null) {
            return Pair(false, "Salon is closed on this date for ${holiday.name}.")
        }

        // 3. Operating hours
        val dayOfWeek = getDayOfWeekForDate(bookingDate)
        val workingDay = dao.getWorkingDaySync(dayOfWeek)
            ?: return Pair(false, "Salon schedule not configured for this day.")

        if (!workingDay.isOpen) {
            return Pair(false, "The salon is closed on ${workingDay.dayName}s.")
        }

        val openMin = timeToMinutes(workingDay.openTime)
        val closeMin = timeToMinutes(workingDay.closeTime)

        if (startMin < openMin || endMin > closeMin) {
            return Pair(
                false,
                "Time ($formattedStartTime - $formattedEndTime) falls outside salon hours (${formatTo12Hour(workingDay.openTime)} - ${formatTo12Hour(workingDay.closeTime)})."
            )
        }

        // 4. Breaks
        val breaks = dao.getAllBreaksSync().filter {
            it.appliesToAllStaff || it.staffId == null || (staffId != null && it.staffId == staffId)
        }
        for (brk in breaks) {
            val bStart = timeToMinutes(brk.startTime)
            val bEnd = timeToMinutes(brk.endTime)
            if (maxOf(startMin, bStart) < minOf(endMin, bEnd)) {
                return Pair(false, "Collides with salon break: ${brk.title} (${formatTo12Hour(brk.startTime)} - ${formatTo12Hour(brk.endTime)}).")
            }
        }

        // 5. Existing active bookings
        val effectiveStaffId = staffId ?: -1L
        val existingBookings = dao.getActiveBookingsForDateAndStaffSync(bookingDate, effectiveStaffId)
        for (existing in existingBookings) {
            val exStart = timeToMinutes(existing.startTime)
            val exEnd = timeToMinutes(existing.endTime)
            if (maxOf(startMin, exStart) < minOf(endMin, exEnd)) {
                return Pair(false, "Time slot is unavailable: already booked (${formatTo12Hour(existing.startTime)} - ${formatTo12Hour(existing.endTime)}).")
            }
        }

        return Pair(true, "Slot is available ($formattedStartTime - $formattedEndTime)")
    }

    suspend fun markProgress(bookingId: Long, newStatus: String): Result<Unit> {
        val booking = dao.getBookingById(bookingId)
            ?: return Result.failure(IllegalArgumentException("Booking not found"))

        val updated = booking.copy(
            status = newStatus,
            updatedAt = System.currentTimeMillis()
        )
        dao.updateBooking(updated)

        if (newStatus == BookingStatus.IN_PROGRESS) {
            dao.insertNotification(
                NotificationEntity(
                    recipientRole = UserRole.CUSTOMER,
                    recipientUserId = booking.customerId,
                    title = "Service Started",
                    body = "${booking.staffName} has commenced your ${booking.serviceName}.",
                    type = "STATUS_CHANGE",
                    referenceId = booking.referenceNumber,
                    timestamp = System.currentTimeMillis()
                )
            )
        } else if (newStatus == BookingStatus.COMPLETED) {
            dao.insertNotification(
                NotificationEntity(
                    recipientRole = UserRole.CUSTOMER,
                    recipientUserId = booking.customerId,
                    title = "Service Completed",
                    body = "Thank you for visiting Aura Lounge! We hope you loved your ${booking.serviceName}.",
                    type = "STATUS_CHANGE",
                    referenceId = booking.referenceNumber,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
        return Result.success(Unit)
    }

    suspend fun collectCashPayment(bookingId: Long): Result<Unit> {
        val booking = dao.getBookingById(bookingId)
            ?: return Result.failure(IllegalArgumentException("Booking not found"))

        val updated = booking.copy(
            isCashCollected = true,
            cashCollectedAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        dao.updateBooking(updated)

        // Customer receipt notification
        dao.insertNotification(
            NotificationEntity(
                recipientRole = UserRole.CUSTOMER,
                recipientUserId = booking.customerId,
                title = "Cash Payment Received",
                body = "Cash receipt: \$${String.format(Locale.US, "%.2f", booking.servicePrice)} collected for ${booking.referenceNumber}. Thank you!",
                type = "PAYMENT_COLLECTED",
                referenceId = booking.referenceNumber,
                timestamp = System.currentTimeMillis()
            )
        )

        // Admin audit notification
        dao.insertNotification(
            NotificationEntity(
                recipientRole = UserRole.ADMIN,
                title = "Cash Registered",
                body = "Registered \$${String.format(Locale.US, "%.2f", booking.servicePrice)} cash received for ${booking.referenceNumber}.",
                type = "PAYMENT_COLLECTED",
                referenceId = booking.referenceNumber,
                timestamp = System.currentTimeMillis()
            )
        )
        return Result.success(Unit)
    }

    suspend fun cancelBookingByCustomer(bookingId: Long): Result<Unit> {
        val booking = dao.getBookingById(bookingId)
            ?: return Result.failure(IllegalArgumentException("Booking not found"))

        val updated = booking.copy(
            status = BookingStatus.CANCELLED,
            updatedAt = System.currentTimeMillis()
        )
        dao.updateBooking(updated)

        dao.insertNotification(
            NotificationEntity(
                recipientRole = UserRole.ADMIN,
                title = "Booking Cancelled",
                body = "${booking.customerName} cancelled appointment ${booking.referenceNumber} (${booking.serviceName}).",
                type = "STATUS_CHANGE",
                referenceId = booking.referenceNumber,
                timestamp = System.currentTimeMillis()
            )
        )
        return Result.success(Unit)
    }
}
