package com.example.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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
import com.example.data.repository.SalonRepository
import com.example.util.AppFeedbackHelper
import com.example.util.EmailNotificationService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class AppMode {
    CUSTOMER,
    ADMIN
}

data class BookingWizardState(
    val isOpen: Boolean = false,
    val step: Int = 1, // 1: Service, 2: Date, 3: Staff, 4: Time Slot, 5: Review, 6: Confirmed
    val service: ServiceEntity? = null,
    val date: String = "",
    val staff: StaffEntity? = null,
    val availableSlots: List<String> = emptyList(),
    val isComputingSlots: Boolean = false,
    val selectedSlot: String? = null,
    val customerNotes: String = "",
    val errorMessage: String? = null,
    val confirmedBooking: BookingEntity? = null
)

class SalonViewModel(private val repository: SalonRepository) : ViewModel() {

    // Active App Mode & Current Authenticated User
    private val _appMode = MutableStateFlow(AppMode.CUSTOMER)
    val appMode: StateFlow<AppMode> = _appMode.asStateFlow()

    private var _lastCustomerUser: UserEntity? = null

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    data class PendingSignup(
        val name: String,
        val email: String,
        val phone: String,
        val passwordRaw: String
    )

    private val _isOtpSheetOpen = MutableStateFlow(false)
    val isOtpSheetOpen: StateFlow<Boolean> = _isOtpSheetOpen.asStateFlow()

    private val _otpTargetGmail = MutableStateFlow("")
    val otpTargetGmail: StateFlow<String> = _otpTargetGmail.asStateFlow()

    private val _otpTargetPhone = MutableStateFlow("")
    val otpTargetPhone: StateFlow<String> = _otpTargetPhone.asStateFlow()

    private val _activeOtpCode = MutableStateFlow("")
    val activeOtpCode: StateFlow<String> = _activeOtpCode.asStateFlow()

    private val _pendingSignup = MutableStateFlow<PendingSignup?>(null)
    val pendingSignup: StateFlow<PendingSignup?> = _pendingSignup.asStateFlow()

    private val _isResetPasswordFlow = MutableStateFlow(false)
    val isResetPasswordFlow: StateFlow<Boolean> = _isResetPasswordFlow.asStateFlow()

    private val _resetIdentifier = MutableStateFlow("")
    val resetIdentifier: StateFlow<String> = _resetIdentifier.asStateFlow()

    private val _currentUser = MutableStateFlow(
        UserEntity(
            id = "guest_client",
            name = "Guest Client",
            phone = "",
            email = "",
            role = UserRole.CUSTOMER,
            avatarInitial = "G"
        )
    )
    val currentUser: StateFlow<UserEntity> = _currentUser.asStateFlow()

    // Salon Information & Reactive Sources of Truth
    val salonConfig: StateFlow<SalonConfigEntity?> = repository.salonConfig.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    val allAmenities: StateFlow<List<AmenityEntity>> = repository.allAmenities.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val activeAmenities: StateFlow<List<AmenityEntity>> = repository.activeAmenities.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            repository.ensureSalonConfig()
            repository.ensureInitialAmenities()
            repository.ensureWorkingDays()
            // Start bidirectional real-time cloud sync across all devices
            repository.startRealtimeCloudSync(viewModelScope)
            // Check if this device is remembered as Admin mobile
            if (repository.isAdminDeviceRemembered()) {
                _isAdminAuthenticated.value = true
                _appMode.value = AppMode.ADMIN
            }
            // Check if there is an existing customer in the database
            val existing = repository.getFirstCustomerUser()
            if (existing != null) {
                _lastCustomerUser = existing
                _currentUser.value = existing
                _isUserLoggedIn.value = true
            }
        }
    }

    val workingDays: StateFlow<List<SalonWorkingDayEntity>> = repository.workingDays.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val breaks: StateFlow<List<SalonBreakEntity>> = repository.breaks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val holidays: StateFlow<List<SalonHolidayEntity>> = repository.holidays.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allServices: StateFlow<List<ServiceEntity>> = repository.allServices.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val activeServices: StateFlow<List<ServiceEntity>> = repository.activeServices.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allStaff: StateFlow<List<StaffEntity>> = repository.allStaff.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val activeStaff: StateFlow<List<StaffEntity>> = repository.activeStaff.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allBookings: StateFlow<List<BookingEntity>> = repository.allBookings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val customerBookings: StateFlow<List<BookingEntity>> = _currentUser
        .flatMapLatest { user -> repository.getCustomerBookings(user.id) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allUsers: StateFlow<List<UserEntity>> = repository.allUsers.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Notifications reactively filtered based on active role/user
    @OptIn(ExperimentalCoroutinesApi::class)
    val notifications: StateFlow<List<NotificationEntity>> = combine(_appMode, _currentUser) { mode, user ->
        Pair(mode, user)
    }.flatMapLatest { (mode, user) ->
        val role = if (mode == AppMode.ADMIN) UserRole.ADMIN else UserRole.CUSTOMER
        val userId = if (mode == AppMode.CUSTOMER) user.id else null
        repository.getNotificationsForRoleOrUser(role, userId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val unreadNotificationCount: StateFlow<Int> = notifications.map { list ->
        list.count { !it.isRead }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    // Support Messages
    @OptIn(ExperimentalCoroutinesApi::class)
    val customerSupportMessages: StateFlow<List<SupportMessageEntity>> = _currentUser
        .flatMapLatest { user -> repository.getSupportMessages(user.id) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allSupportMessages: StateFlow<List<SupportMessageEntity>> = repository.allSupportMessages.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // 6-Step Booking Wizard State
    private val _wizardState = MutableStateFlow(BookingWizardState())
    val wizardState: StateFlow<BookingWizardState> = _wizardState.asStateFlow()

    // Global UI Snack / Alert
    private val _uiMessage = MutableStateFlow<String?>(null)
    val uiMessage: StateFlow<String?> = _uiMessage.asStateFlow()

    // Admin Authentication State
    private val _isAdminAuthenticated = MutableStateFlow(false)
    val isAdminAuthenticated: StateFlow<Boolean> = _isAdminAuthenticated.asStateFlow()

    fun authenticateAdmin(passwordInput: String): Boolean {
        val currentPassword = salonConfig.value?.adminPassword ?: "UnisexSalon#Admin98"
        if (passwordInput.trim() == currentPassword) {
            _isAdminAuthenticated.value = true
            repository.setAdminDeviceRemembered(true)
            setAppMode(AppMode.ADMIN)
            _uiMessage.value = "Admin authentication verified. This device is remembered!"
            return true
        }
        return false
    }

    fun isAdminDeviceRemembered(): Boolean {
        return repository.isAdminDeviceRemembered()
    }

    fun forgetAdminDevice() {
        repository.setAdminDeviceRemembered(false)
        _isAdminAuthenticated.value = false
        setAppMode(AppMode.CUSTOMER)
        _uiMessage.value = "Admin login cleared from this device."
    }

    fun setAdminDeviceRemembered(remembered: Boolean) {
        repository.setAdminDeviceRemembered(remembered)
    }

    fun updatePhotoFit(photoType: String, fitKey: String) {
        val current = salonConfig.value ?: return
        val updated = when (photoType.lowercase(Locale.US)) {
            "storefront" -> current.copy(storefrontImageFit = fitKey)
            "hero" -> current.copy(heroImageFit = fitKey)
            "men", "menpromo" -> current.copy(menPromoImageFit = fitKey)
            "women", "womenpromo" -> current.copy(womenPromoImageFit = fitKey)
            else -> current
        }
        viewModelScope.launch {
            repository.updateSalonConfig(updated)
            _uiMessage.value = "Photo alignment updated to $fitKey."
        }
    }

    fun lockAdmin() {
        _isAdminAuthenticated.value = false
        setAppMode(AppMode.CUSTOMER)
        _uiMessage.value = "Admin mode locked."
    }

    fun updateAdminPassword(newPassword: String) {
        val trimmed = newPassword.trim()
        if (trimmed.length < 4) {
            _uiMessage.value = "Password must be at least 4 characters."
            return
        }
        viewModelScope.launch {
            val current = salonConfig.value ?: repository.ensureSalonConfig()
            repository.updateSalonConfig(
                current.copy(
                    adminPassword = trimmed,
                    updatedAt = System.currentTimeMillis()
                )
            )
            _uiMessage.value = "Admin password updated successfully!"
        }
    }

    fun updateCurrencySymbol(newSymbol: String) {
        val trimmed = newSymbol.trim()
        if (trimmed.isEmpty()) {
            _uiMessage.value = "Currency symbol cannot be empty."
            return
        }
        viewModelScope.launch {
            val current = salonConfig.value ?: repository.ensureSalonConfig()
            repository.updateSalonConfig(
                current.copy(
                    currencySymbol = trimmed,
                    updatedAt = System.currentTimeMillis()
                )
            )
            _uiMessage.value = "Currency updated to $trimmed!"
        }
    }

    fun updateSalonInfo(
        salonName: String,
        tagline: String,
        address: String,
        phone: String,
        email: String,
        about: String,
        cashNotice: String,
        adminGmail: String = "",
        adminPhone: String = "",
        storefrontImageUrl: String = ""
    ) {
        viewModelScope.launch {
            val current = salonConfig.value ?: repository.ensureSalonConfig()
            repository.updateSalonConfig(
                current.copy(
                    salonName = salonName.trim().ifEmpty { current.salonName },
                    tagline = tagline.trim().ifEmpty { current.tagline },
                    address = address.trim().ifEmpty { current.address },
                    phone = phone.trim().ifEmpty { current.phone },
                    email = email.trim().ifEmpty { current.email },
                    about = about.trim().ifEmpty { current.about },
                    cashNotice = cashNotice.trim().ifEmpty { current.cashNotice },
                    adminGmail = if (adminGmail.isNotBlank()) adminGmail.trim() else current.adminGmail,
                    adminPhone = if (adminPhone.isNotBlank()) adminPhone.trim() else current.adminPhone,
                    storefrontImageUrl = if (storefrontImageUrl.isNotBlank()) storefrontImageUrl.trim() else current.storefrontImageUrl,
                    updatedAt = System.currentTimeMillis()
                )
            )
            _uiMessage.value = "Salon details updated successfully across all customer dashboards!"
        }
    }

    fun updateSalonLocation(
        streetAddress: String,
        area: String,
        city: String,
        state: String,
        country: String,
        pincode: String,
        latitude: Double,
        longitude: Double
    ) {
        val fullAddress = listOf(streetAddress.trim(), area.trim(), city.trim(), "$state $pincode".trim(), country.trim())
            .filter { it.isNotBlank() }
            .joinToString(", ")

        viewModelScope.launch {
            val current = salonConfig.value ?: repository.ensureSalonConfig()
            repository.updateSalonConfig(
                current.copy(
                    address = if (fullAddress.isNotBlank()) fullAddress else current.address,
                    streetAddress = streetAddress.trim().ifEmpty { current.streetAddress },
                    area = area.trim().ifEmpty { current.area },
                    city = city.trim().ifEmpty { current.city },
                    state = state.trim().ifEmpty { current.state },
                    country = country.trim().ifEmpty { current.country },
                    pincode = pincode.trim().ifEmpty { current.pincode },
                    latitude = if (latitude != 0.0) latitude else current.latitude,
                    longitude = if (longitude != 0.0) longitude else current.longitude,
                    updatedAt = System.currentTimeMillis()
                )
            )
            _uiMessage.value = "Salon location updated successfully across all customer dashboards!"
        }
    }

    fun updateAdminContactDetails(
        adminGmail: String,
        adminPhone: String
    ) {
        viewModelScope.launch {
            val current = salonConfig.value ?: repository.ensureSalonConfig()
            repository.updateSalonConfig(
                current.copy(
                    adminGmail = adminGmail.trim().ifEmpty { current.adminGmail },
                    adminPhone = adminPhone.trim().ifEmpty { current.adminPhone },
                    updatedAt = System.currentTimeMillis()
                )
            )
            _uiMessage.value = "Admin contact details updated!"
        }
    }

    fun updateSalonVisualsAndBanners(
        heroImageUrl: String,
        heroTitleLine1: String,
        heroTitleLine2: String,
        heroAccentText: String,
        heroTags: String,
        heroCtaText: String,
        menPromoTitle: String,
        menPromoSubtitle: String,
        menPromoImageUrl: String,
        womenPromoTitle: String,
        womenPromoSubtitle: String,
        womenPromoImageUrl: String,
        storefrontImageUrl: String = ""
    ) {
        viewModelScope.launch {
            val current = salonConfig.value ?: repository.ensureSalonConfig()
            repository.updateSalonConfig(
                current.copy(
                    heroImageUrl = if (heroImageUrl.isNotBlank()) heroImageUrl.trim() else current.heroImageUrl,
                    heroTitleLine1 = if (heroTitleLine1.isNotBlank()) heroTitleLine1.trim() else current.heroTitleLine1,
                    heroTitleLine2 = if (heroTitleLine2.isNotBlank()) heroTitleLine2.trim() else current.heroTitleLine2,
                    heroAccentText = if (heroAccentText.isNotBlank()) heroAccentText.trim() else current.heroAccentText,
                    heroTags = if (heroTags.isNotBlank()) heroTags.trim() else current.heroTags,
                    heroCtaText = if (heroCtaText.isNotBlank()) heroCtaText.trim() else current.heroCtaText,
                    menPromoTitle = if (menPromoTitle.isNotBlank()) menPromoTitle.trim() else current.menPromoTitle,
                    menPromoSubtitle = if (menPromoSubtitle.isNotBlank()) menPromoSubtitle.trim() else current.menPromoSubtitle,
                    menPromoImageUrl = if (menPromoImageUrl.isNotBlank()) menPromoImageUrl.trim() else current.menPromoImageUrl,
                    womenPromoTitle = if (womenPromoTitle.isNotBlank()) womenPromoTitle.trim() else current.womenPromoTitle,
                    womenPromoSubtitle = if (womenPromoSubtitle.isNotBlank()) womenPromoSubtitle.trim() else current.womenPromoSubtitle,
                    womenPromoImageUrl = if (womenPromoImageUrl.isNotBlank()) womenPromoImageUrl.trim() else current.womenPromoImageUrl,
                    storefrontImageUrl = if (storefrontImageUrl.isNotBlank()) storefrontImageUrl.trim() else current.storefrontImageUrl,
                    updatedAt = System.currentTimeMillis()
                )
            )
            _uiMessage.value = "App visuals & imagery updated in real-time across customer dashboard!"
        }
    }

    fun dismissUiMessage() {
        _uiMessage.value = null
    }

    // Role & App Switcher
    fun setAppMode(mode: AppMode) {
        if (mode == AppMode.CUSTOMER) {
            _isAdminAuthenticated.value = false
        }
        _appMode.value = mode
        if (mode == AppMode.ADMIN) {
            _currentUser.value = UserEntity(
                id = "admin_1",
                name = "Salon Admin",
                phone = "+91 98765 43210",
                email = "admin@unisexsalon.com",
                role = UserRole.ADMIN,
                avatarInitial = "A"
            )
        } else {
            // Restore active logged-in customer or guest
            val restored = _lastCustomerUser ?: UserEntity(
                id = "guest_client",
                name = "Guest Client",
                phone = "",
                email = "",
                role = UserRole.CUSTOMER,
                avatarInitial = "G"
            )
            _currentUser.value = restored
        }
    }

    // Customer Authentication & Real Account Management
    private fun generateRandomOtp(): String {
        return (100000 + (Math.random() * 900000).toInt()).toString()
    }

    fun sendSignupVerificationOtp(
        name: String,
        email: String,
        phone: String,
        passwordRaw: String,
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val trimmedName = name.trim()
        val trimmedEmail = email.trim().lowercase(Locale.US)
        val trimmedPhone = phone.trim()

        if (trimmedName.length < 2) {
            onError("Please enter a valid full name.")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches() || !trimmedEmail.contains(".")) {
            onError("Please enter a valid Gmail / Email address.")
            return
        }
        val cleanPhone = trimmedPhone.filter { it.isDigit() || it == '+' }
        if (cleanPhone.length < 10) {
            onError("Please enter a valid 10-digit mobile number.")
            return
        }
        if (passwordRaw.length < 6) {
            onError("Password must be at least 6 characters.")
            return
        }

        val otp = generateRandomOtp()
        _activeOtpCode.value = otp
        _otpTargetGmail.value = trimmedEmail
        _otpTargetPhone.value = trimmedPhone
        _pendingSignup.value = PendingSignup(trimmedName, trimmedEmail, trimmedPhone, passwordRaw)
        _isResetPasswordFlow.value = false
        _isOtpSheetOpen.value = true

        EmailNotificationService.sendOtpVerificationEmail(
            context = context,
            recipientEmail = trimmedEmail,
            recipientPhone = trimmedPhone,
            otpCode = otp,
            purpose = "Account Verification",
            firebaseBackend = repository.firebaseBackend,
            scope = viewModelScope
        )
        AppFeedbackHelper.triggerNotification(context)

        _uiMessage.value = "Verification code dispatched to your Gmail: $trimmedEmail"
        onSuccess()
    }

    fun verifyOtpAndCompleteRegistration(
        enteredOtp: String,
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (enteredOtp.trim() != _activeOtpCode.value) {
            AppFeedbackHelper.triggerError(context)
            onError("Invalid verification code. Please check your Gmail inbox.")
            return
        }

        val pending = _pendingSignup.value
        if (pending == null) {
            onError("Signup session timed out. Please register again.")
            return
        }

        viewModelScope.launch {
            val res = repository.registerUser(
                name = pending.name,
                email = pending.email,
                phone = pending.phone,
                passwordRaw = pending.passwordRaw
            )
            res.fold(
                onSuccess = { user ->
                    _lastCustomerUser = user
                    _currentUser.value = user
                    _isUserLoggedIn.value = true
                    _isOtpSheetOpen.value = false
                    _pendingSignup.value = null
                    _authError.value = null
                    AppFeedbackHelper.triggerSuccess(context)
                    _uiMessage.value = "Account verified! Welcome to ${salonConfig.value?.salonName ?: "Unisex Salon"}, ${user.name}!"
                    onSuccess()
                },
                onFailure = { err ->
                    AppFeedbackHelper.triggerError(context)
                    onError(err.localizedMessage ?: "Registration failed.")
                }
            )
        }
    }

    fun sendPasswordResetOtp(
        identifier: String,
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val clean = identifier.trim()
        if (clean.isEmpty()) {
            onError("Please enter your registered Gmail or mobile number.")
            return
        }
        val otp = generateRandomOtp()
        _activeOtpCode.value = otp
        _resetIdentifier.value = clean
        _otpTargetGmail.value = if (clean.contains("@")) clean else ""
        _otpTargetPhone.value = if (!clean.contains("@")) clean else ""
        _isResetPasswordFlow.value = true
        _isOtpSheetOpen.value = true

        EmailNotificationService.sendOtpVerificationEmail(
            context = context,
            recipientEmail = clean,
            recipientPhone = clean,
            otpCode = otp,
            purpose = "Password Reset",
            firebaseBackend = repository.firebaseBackend,
            scope = viewModelScope
        )
        AppFeedbackHelper.triggerNotification(context)

        _uiMessage.value = "Password reset code dispatched to your Gmail: $clean"
        onSuccess()
    }

    fun verifyOtpAndResetPassword(
        enteredOtp: String,
        newPasswordRaw: String,
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (enteredOtp.trim() != _activeOtpCode.value) {
            AppFeedbackHelper.triggerError(context)
            onError("Invalid verification code. Please check your Gmail inbox.")
            return
        }
        if (newPasswordRaw.length < 6) {
            onError("New password must be at least 6 characters.")
            return
        }

        viewModelScope.launch {
            val res = repository.resetUserPassword(_resetIdentifier.value, newPasswordRaw)
            res.fold(
                onSuccess = { user ->
                    _currentUser.value = user
                    _isUserLoggedIn.value = true
                    _isOtpSheetOpen.value = false
                    AppFeedbackHelper.triggerSuccess(context)
                    _uiMessage.value = "Password reset successfully! Welcome back, ${user.name}."
                    onSuccess()
                },
                onFailure = { err ->
                    AppFeedbackHelper.triggerError(context)
                    onError(err.localizedMessage ?: "Failed to reset password.")
                }
            )
        }
    }

    fun closeOtpSheet() {
        _isOtpSheetOpen.value = false
    }

    fun resendCurrentOtp(context: Context) {
        val otp = generateRandomOtp()
        _activeOtpCode.value = otp
        val target = if (_isResetPasswordFlow.value) _resetIdentifier.value else _otpTargetGmail.value
        EmailNotificationService.sendOtpVerificationEmail(
            context = context,
            recipientEmail = _otpTargetGmail.value,
            recipientPhone = _otpTargetPhone.value,
            otpCode = otp,
            purpose = if (_isResetPasswordFlow.value) "Password Reset" else "Account Verification",
            firebaseBackend = repository.firebaseBackend,
            scope = viewModelScope
        )
        AppFeedbackHelper.triggerNotification(context)
        _uiMessage.value = "New verification code sent to your Gmail!"
    }

    fun registerCustomer(
        name: String,
        email: String,
        phone: String,
        passwordRaw: String,
        onSuccess: () -> Unit = {}
    ) {
        _authError.value = null
        viewModelScope.launch {
            val res = repository.registerUser(name, email, phone, passwordRaw)
            res.fold(
                onSuccess = { user ->
                    _lastCustomerUser = user
                    _currentUser.value = user
                    _isUserLoggedIn.value = true
                    _authError.value = null
                    _uiMessage.value = "Welcome to Unisex Salon, ${user.name}! Account created."
                    onSuccess()
                },
                onFailure = { err ->
                    _authError.value = err.localizedMessage ?: "Registration failed."
                }
            )
        }
    }

    fun loginCustomer(
        identifier: String,
        passwordRaw: String,
        onSuccess: () -> Unit = {}
    ) {
        _authError.value = null
        viewModelScope.launch {
            val res = repository.loginUser(identifier, passwordRaw)
            res.fold(
                onSuccess = { user ->
                    _lastCustomerUser = user
                    _currentUser.value = user
                    _isUserLoggedIn.value = true
                    _authError.value = null
                    _uiMessage.value = "Welcome back, ${user.name}!"
                    onSuccess()
                },
                onFailure = { err ->
                    _authError.value = err.localizedMessage ?: "Sign-in failed."
                }
            )
        }
    }

    fun logoutCustomer() {
        _isUserLoggedIn.value = false
        val guest = UserEntity(
            id = "guest_client",
            name = "Guest Client",
            phone = "",
            email = "",
            role = UserRole.CUSTOMER,
            avatarInitial = "G"
        )
        _currentUser.value = guest
        _uiMessage.value = "You have logged out."
    }

    fun clearAuthError() {
        _authError.value = null
    }

    fun signInWithGoogle(
        name: String,
        email: String,
        context: Context,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            val res = repository.signInWithGoogle(name, email)
            res.fold(
                onSuccess = { user ->
                    _lastCustomerUser = user
                    _currentUser.value = user
                    _isUserLoggedIn.value = true
                    _authError.value = null
                    AppFeedbackHelper.triggerSuccess(context)
                    _uiMessage.value = "Signed in with Google as ${user.email}!"
                    onSuccess()
                },
                onFailure = { err ->
                    AppFeedbackHelper.triggerError(context)
                    val msg = err.localizedMessage ?: "Google sign-in failed."
                    _authError.value = msg
                    onError(msg)
                }
            )
        }
    }

    fun updateCurrentUserProfile(
        name: String,
        phone: String,
        email: String,
        preferredServices: String? = null,
        gender: String? = null,
        birthday: String? = null,
        notes: String? = null
    ) {
        viewModelScope.launch {
            val user = _currentUser.value
            val res = repository.updateUserProfile(
                userId = user.id,
                name = name,
                phone = phone,
                email = email,
                preferredServices = preferredServices,
                gender = gender,
                birthday = birthday,
                notes = notes
            )
            res.fold(
                onSuccess = { updated ->
                    _currentUser.value = updated
                    _lastCustomerUser = updated
                    _uiMessage.value = "Profile & preferences saved and synced to Firebase!"
                },
                onFailure = { err ->
                    _uiMessage.value = err.localizedMessage ?: "Failed to update profile."
                }
            )
        }
    }

    // Firebase Backend Synchronization
    val isFirebaseAvailable: Boolean
        get() = repository.firebaseBackend?.isFirebaseInitialized == true

    fun triggerFirebaseCloudSync() {
        viewModelScope.launch {
            val count = repository.syncAllBookingsToCloud()
            val statusNotice = if (isFirebaseAvailable) {
                "Firebase Cloud sync complete ($count records verified)."
            } else {
                "Local Room Database active (Firebase standby/ready)."
            }
            _uiMessage.value = statusNotice
        }
    }

    // 6-Step Booking Flow Methods
    fun startBookingFlow(initialService: ServiceEntity? = null) {
        val defaultDate = getDefaultBookingDate()
        _wizardState.value = BookingWizardState(
            isOpen = true,
            step = if (initialService != null) 2 else 1,
            service = initialService,
            date = defaultDate,
            staff = null,
            customerNotes = ""
        )
        if (initialService != null) {
            recalculateAvailableSlots()
        }
    }

    private fun getDefaultBookingDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(Date())
    }

    fun closeBookingFlow() {
        _wizardState.value = BookingWizardState()
    }

    fun setWizardStep(step: Int) {
        _wizardState.value = _wizardState.value.copy(step = step, errorMessage = null)
    }

    fun selectBookingService(service: ServiceEntity) {
        _wizardState.value = _wizardState.value.copy(
            service = service,
            step = 2,
            errorMessage = null
        )
        recalculateAvailableSlots()
    }

    fun selectBookingDate(dateStr: String) {
        _wizardState.value = _wizardState.value.copy(
            date = dateStr,
            selectedSlot = null,
            errorMessage = null
        )
        recalculateAvailableSlots()
    }

    fun selectBookingStaff(staff: StaffEntity?) {
        _wizardState.value = _wizardState.value.copy(
            staff = staff,
            selectedSlot = null,
            errorMessage = null
        )
        recalculateAvailableSlots()
    }

    fun selectBookingSlot(slot: String) {
        _wizardState.value = _wizardState.value.copy(
            selectedSlot = slot,
            errorMessage = null
        )
    }

    fun updateCustomerNotes(notes: String) {
        _wizardState.value = _wizardState.value.copy(customerNotes = notes)
    }

    private fun recalculateAvailableSlots() {
        val current = _wizardState.value
        val service = current.service ?: return
        val date = if (current.date.isNotEmpty()) current.date else getDefaultBookingDate()

        viewModelScope.launch {
            _wizardState.value = _wizardState.value.copy(isComputingSlots = true)

            val staffId = current.staff?.id
            val slots = repository.getAvailableTimeSlots(date, staffId, service.durationMinutes)

            _wizardState.value = _wizardState.value.copy(
                availableSlots = slots,
                isComputingSlots = false
            )
        }
    }

    fun submitBookingConfirmation() {
        val current = _wizardState.value
        val service = current.service
        val staff = current.staff ?: activeStaff.value.firstOrNull() ?: StaffEntity(
            id = 1,
            name = "Elena Vance",
            roleTitle = "Master Stylist",
            specialty = "All Services",
            rating = 4.98f,
            avatarColorHex = 0xFF8E4D3E,
            isActive = true
        )
        val date = if (current.date.isNotEmpty()) current.date else getDefaultBookingDate()
        val slot = current.selectedSlot
        val user = _currentUser.value

        if (service == null) {
            _wizardState.value = current.copy(errorMessage = "Please select a service before confirming booking.")
            return
        }
        if (slot.isNullOrEmpty()) {
            _wizardState.value = current.copy(errorMessage = "Please select an appointment time slot before confirming.")
            return
        }

        viewModelScope.launch {
            val result = repository.validateAndCreateBooking(
                customerId = user.id.ifBlank { "cust_guest" },
                customerName = user.name.ifBlank { "Guest Client" },
                customerPhone = user.phone.ifBlank { "+91 98765 00000" },
                customerEmail = user.email.ifBlank { "client@unisexsalon.com" },
                service = service,
                staff = staff,
                bookingDate = date,
                startTime = slot,
                notes = current.customerNotes
            )

            result.fold(
                onSuccess = { createdBooking ->
                    _wizardState.value = current.copy(
                        step = 6, // Confirmation screen!
                        confirmedBooking = createdBooking,
                        errorMessage = null
                    )
                },
                onFailure = { error ->
                    _wizardState.value = current.copy(
                        errorMessage = error.message ?: "Booking could not be created. Please select another slot."
                    )
                }
            )
        }
    }

    // Customer Actions
    fun cancelBooking(bookingId: Long) {
        viewModelScope.launch {
            repository.cancelBookingByCustomer(bookingId)
            _uiMessage.value = "Booking cancelled."
        }
    }

    fun sendCustomerSupportMessage(text: String) {
        if (text.isBlank()) return
        val user = _currentUser.value
        viewModelScope.launch {
            repository.sendSupportMessage(
                ticketCustomerId = user.id,
                customerName = user.name,
                senderId = user.id,
                senderName = user.name,
                senderRole = UserRole.CUSTOMER,
                messageText = text.trim()
            )
        }
    }

    // Admin Actions
    fun acceptBooking(bookingId: Long) {
        viewModelScope.launch {
            val res = repository.acceptBooking(bookingId)
            _uiMessage.value = if (res.isSuccess) "Booking accepted & customer notified!" else "Failed to accept booking"
        }
    }

    fun rejectBooking(bookingId: Long, reason: String) {
        viewModelScope.launch {
            val res = repository.rejectBooking(bookingId, reason)
            _uiMessage.value = if (res.isSuccess) "Booking rejected with reason sent to customer." else "Failed to reject booking"
        }
    }

    fun rescheduleBooking(bookingId: Long, newDate: String, newStartTime: String) {
        viewModelScope.launch {
            val res = repository.rescheduleBooking(bookingId, newDate, newStartTime)
            if (res.isSuccess) {
                _uiMessage.value = "Appointment successfully rescheduled!"
            } else {
                _uiMessage.value = res.exceptionOrNull()?.message ?: "Reschedule conflict."
            }
        }
    }

    fun postponeBooking(
        bookingId: Long,
        newDate: String,
        newStartTime: String,
        reason: String
    ) {
        viewModelScope.launch {
            val res = repository.postponeBooking(bookingId, newDate, newStartTime, reason)
            if (res.isSuccess) {
                _uiMessage.value = "Appointment successfully postponed! Customer notified via email & SMS."
            } else {
                _uiMessage.value = res.exceptionOrNull()?.message ?: "Unable to postpone appointment."
            }
        }
    }

    fun checkCustomTimeSlotAvailability(
        date: String,
        time: String,
        staffId: Long?,
        durationMins: Int,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            val (isAvail, reason) = repository.isCustomSlotAvailable(date, time, staffId, durationMins)
            onResult(isAvail, reason)
        }
    }

    fun markProgress(bookingId: Long, newStatus: String) {
        viewModelScope.launch {
            val res = repository.markProgress(bookingId, newStatus)
            _uiMessage.value = if (res.isSuccess) "Status updated to $newStatus" else "Failed to update status"
        }
    }

    fun collectCashPayment(bookingId: Long) {
        viewModelScope.launch {
            val res = repository.collectCashPayment(bookingId)
            _uiMessage.value = if (res.isSuccess) "Cash payment recorded and receipt issued!" else "Failed to record cash payment"
        }
    }

    fun sendAdminSupportReply(ticketCustomerId: String, customerName: String, text: String) {
        if (text.isBlank()) return
        val user = _currentUser.value
        viewModelScope.launch {
            repository.sendSupportMessage(
                ticketCustomerId = ticketCustomerId,
                customerName = customerName,
                senderId = user.id,
                senderName = user.name,
                senderRole = UserRole.ADMIN,
                messageText = text.trim()
            )
        }
    }


    fun saveService(service: ServiceEntity) {
        viewModelScope.launch {
            if (service.id == 0L) {
                repository.addService(service)
                _uiMessage.value = "New service '${service.name}' added!"
            } else {
                repository.updateService(service)
                _uiMessage.value = "Service '${service.name}' updated!"
            }
        }
    }

    fun deleteService(service: ServiceEntity) {
        viewModelScope.launch {
            repository.deleteService(service)
            _uiMessage.value = "Service removed."
        }
    }

    fun saveStaff(staff: StaffEntity) {
        viewModelScope.launch {
            if (staff.id == 0L) {
                repository.addStaff(staff)
                _uiMessage.value = "Staff member '${staff.name}' added!"
            } else {
                repository.updateStaff(staff)
                _uiMessage.value = "Staff '${staff.name}' updated!"
            }
        }
    }

    fun deleteStaff(staff: StaffEntity) {
        viewModelScope.launch {
            repository.deleteStaff(staff)
            _uiMessage.value = "Staff member removed."
        }
    }

    fun updateWorkingDayHours(dayOfWeek: Int, isOpen: Boolean, openTime: String, closeTime: String) {
        viewModelScope.launch {
            val existing = workingDays.value.find { it.dayOfWeek == dayOfWeek } ?: return@launch
            val updated = existing.copy(
                isOpen = isOpen,
                openTime = openTime,
                closeTime = closeTime
            )
            repository.updateWorkingDay(updated)
            _uiMessage.value = "Updated ${existing.dayName} operating hours!"
        }
    }

    fun applyHoursToMultipleDays(days: List<Int>, isOpen: Boolean, openTime: String, closeTime: String, label: String) {
        viewModelScope.launch {
            days.forEach { dayOfWeek ->
                val existing = workingDays.value.find { it.dayOfWeek == dayOfWeek }
                if (existing != null) {
                    val updated = existing.copy(
                        isOpen = isOpen,
                        openTime = openTime,
                        closeTime = closeTime
                    )
                    repository.updateWorkingDay(updated)
                }
            }
            _uiMessage.value = "Operating hours applied to $label!"
        }
    }

    fun addSalonBreak(title: String, startTime: String, endTime: String) {
        viewModelScope.launch {
            repository.addBreak(
                SalonBreakEntity(
                    title = title,
                    startTime = startTime,
                    endTime = endTime,
                    appliesToAllStaff = true
                )
            )
            _uiMessage.value = "Salon break added: $startTime - $endTime"
        }
    }

    fun deleteSalonBreak(id: Long) {
        viewModelScope.launch {
            repository.deleteBreak(id)
            _uiMessage.value = "Salon break removed."
        }
    }

    fun addSalonHoliday(dateStr: String, name: String, reason: String) {
        viewModelScope.launch {
            repository.addHoliday(
                SalonHolidayEntity(
                    dateString = dateStr,
                    name = name,
                    reason = reason
                )
            )
            _uiMessage.value = "Salon holiday added for $dateStr"
        }
    }

    fun deleteSalonHoliday(id: Long) {
        viewModelScope.launch {
            repository.deleteHoliday(id)
            _uiMessage.value = "Holiday removed."
        }
    }

    fun markNotificationRead(id: Long) {
        viewModelScope.launch {
            repository.markNotificationRead(id)
        }
    }

    fun markAllNotificationsRead() {
        val role = if (_appMode.value == AppMode.ADMIN) UserRole.ADMIN else UserRole.CUSTOMER
        val userId = if (_appMode.value == AppMode.CUSTOMER) _currentUser.value.id else null
        viewModelScope.launch {
            repository.markAllNotificationsRead(role, userId)
        }
    }

    // Admin Notification Settings (WhatsApp & SMS)
    fun updateNotificationSettings(
        adminPhone: String,
        enableWhatsApp: Boolean,
        enableSms: Boolean
    ) {
        val current = salonConfig.value ?: return
        viewModelScope.launch {
            repository.updateSalonConfig(
                current.copy(
                    adminNotificationPhone = adminPhone.trim(),
                    enableWhatsAppAlerts = enableWhatsApp,
                    enableSmsAlerts = enableSms,
                    updatedAt = System.currentTimeMillis()
                )
            )
            _uiMessage.value = "Notification channels updated: Alerts route to registered admin phone."
        }
    }

    // Facilities / Amenities Management (Adjustable by Admin)
    fun addAmenity(name: String, description: String, iconKey: String = "AC") {
        if (name.isBlank()) {
            _uiMessage.value = "Facility name cannot be empty."
            return
        }
        val id = name.trim().lowercase(Locale.US).replace(" ", "_") + "_" + System.currentTimeMillis() % 10000
        val sortOrder = (allAmenities.value.maxOfOrNull { it.sortOrder } ?: 0) + 1
        viewModelScope.launch {
            repository.insertOrUpdateAmenity(
                AmenityEntity(
                    id = id,
                    name = name.trim(),
                    description = description.trim(),
                    iconKey = iconKey.uppercase(Locale.US).trim(),
                    isEnabled = true,
                    sortOrder = sortOrder
                )
            )
            _uiMessage.value = "Facility \"${name.trim()}\" added."
        }
    }

    fun updateAmenity(amenity: AmenityEntity) {
        viewModelScope.launch {
            repository.updateAmenity(amenity)
            _uiMessage.value = "Facility \"${amenity.name}\" updated."
        }
    }

    fun toggleAmenityEnabled(amenity: AmenityEntity) {
        viewModelScope.launch {
            val updated = amenity.copy(isEnabled = !amenity.isEnabled)
            repository.updateAmenity(updated)
            _uiMessage.value = "${amenity.name} ${if (updated.isEnabled) "enabled" else "disabled"}."
        }
    }

    fun deleteAmenity(id: String) {
        viewModelScope.launch {
            repository.deleteAmenity(id)
            _uiMessage.value = "Facility removed."
        }
    }

    // Time Format Helper (12-hour AM/PM)
    fun formatTo12Hour(timeStr: String): String {
        return repository.formatTo12Hour(timeStr)
    }

    fun applyHoursToAllDays(openTime: String, closeTime: String) {
        viewModelScope.launch {
            workingDays.value.forEach { day ->
                val updated = day.copy(
                    isOpen = true,
                    openTime = openTime,
                    closeTime = closeTime
                )
                repository.updateWorkingDay(updated)
            }
            _uiMessage.value = "Operating hours ($openTime - $closeTime) applied to all days!"
        }
    }
}

class SalonViewModelFactory(private val repository: SalonRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SalonViewModel::class.java)) {
            return SalonViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
