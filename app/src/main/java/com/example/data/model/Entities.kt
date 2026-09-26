package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

object BookingStatus {
    const val PENDING = "PENDING"
    const val CONFIRMED = "CONFIRMED"
    const val IN_PROGRESS = "IN_PROGRESS"
    const val COMPLETED = "COMPLETED"
    const val REJECTED = "REJECTED"
    const val CANCELLED = "CANCELLED"
    const val POSTPONED = "POSTPONED"
}

object UserRole {
    const val ADMIN = "ADMIN"
    const val CUSTOMER = "CUSTOMER"
}

@Entity(tableName = "salon_config")
data class SalonConfigEntity(
    @PrimaryKey val id: Int = 1,
    val salonName: String = "UNISEX SALON",
    val tagline: String = "PREMIUM CARE FOR EVERYONE",
    val address: String = "123 Wellness Street, Green Park, Delhi",
    val streetAddress: String = "123 Wellness Street",
    val area: String = "Green Park",
    val city: String = "Delhi",
    val state: String = "Delhi",
    val country: String = "India",
    val pincode: String = "110016",
    val latitude: Double = 28.5583,
    val longitude: Double = 77.2028,
    val phone: String = "+91 98765 43210",
    val adminNotificationPhone: String = "+91 98765 43210",
    val enableWhatsAppAlerts: Boolean = true,
    val enableSmsAlerts: Boolean = true,
    val email: String = "concierge@unisexsalon.com",
    val adminGmail: String = "admin.unisexsalon@gmail.com",
    val adminPhone: String = "+91 98765 43210",
    val about: String = "Premier unisex luxury lounge offering bespoke haircuts, precision styling, restorative facials, beard grooming, and luxury care. We take pride in personalized attention and an unhurried, serene atmosphere.",
    val cashNotice: String = "In-salon cash payment in Rupee (₹) accepted upon appointment completion. Please check in with the concierge 10 minutes prior to your service.",
    val heroImageUrl: String = "",
    val heroTitleLine1: String = "Premium Care",
    val heroTitleLine2: String = "for Everyone",
    val heroAccentText: String = "Everyone",
    val heroTags: String = "Hair | Skin | Grooming | Beauty",
    val heroCtaText: String = "Book Your Appointment",
    val menPromoTitle: String = "Be Your Best Self",
    val menPromoSubtitle: String = "Expert care. Premium products.",
    val menPromoImageUrl: String = "",
    val womenPromoTitle: String = "Luxury Care",
    val womenPromoSubtitle: String = "for Every You",
    val womenPromoImageUrl: String = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=800&q=80",
    val storefrontImageUrl: String = "",
    val storefrontImageFit: String = "CROP_CENTER",
    val heroImageFit: String = "CROP_CENTER",
    val menPromoImageFit: String = "CROP_CENTER",
    val womenPromoImageFit: String = "CROP_CENTER",
    val adminPassword: String = "UnisexSalon#Admin98",
    val currencySymbol: String = "₹",
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "salon_amenities")
data class AmenityEntity(
    @PrimaryKey val id: String, // e.g. "ac", "wifi", "parking", "hygiene", "lounge"
    val name: String,
    val description: String,
    val iconKey: String = "AC", // "AC", "WIFI", "PARKING", "HYGIENE", "LOUNGE", "SPA", "COFFEE", "MUSIC"
    val isEnabled: Boolean = true,
    val sortOrder: Int = 0
)

@Entity(tableName = "salon_working_days")
data class SalonWorkingDayEntity(
    @PrimaryKey val dayOfWeek: Int, // 1 = Monday, 7 = Sunday
    val dayName: String,
    val isOpen: Boolean,
    val openTime: String, // "09:00"
    val closeTime: String // "19:00"
)

@Entity(tableName = "salon_breaks")
data class SalonBreakEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val startTime: String, // "13:00"
    val endTime: String,   // "14:00"
    val appliesToAllStaff: Boolean = true,
    val staffId: Long? = null
)

@Entity(tableName = "salon_holidays")
data class SalonHolidayEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateString: String, // "YYYY-MM-DD"
    val name: String,
    val reason: String
)

@Entity(tableName = "services")
data class ServiceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: String, // "Hair Cut", "Hair Styling", "Colour & Highlights", "Beard Grooming", "Facial", "Skin Care", "Makeup"
    val description: String,
    val durationMinutes: Int,
    val price: Double,
    val iconKey: String = "scissors",
    val imageUrl: String = "",
    val isActive: Boolean = true
)

@Entity(tableName = "staff")
data class StaffEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val roleTitle: String,
    val specialty: String,
    val rating: Float = 4.9f,
    val avatarColorHex: Long = 0xFF8E4D3E,
    val isActive: Boolean = true,
    val phone: String = "",
    val email: String = ""
)

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val role: String, // "ADMIN" or "CUSTOMER"
    val avatarInitial: String,
    val passwordHash: String = "",
    val isSyncedWithFirebase: Boolean = false,
    val preferredServices: String = "", // Comma-separated service names or IDs
    val gender: String = "",           // e.g. "Female", "Male", "Other"
    val birthday: String = "",         // e.g. "YYYY-MM-DD"
    val notes: String = "",            // Hair/Skin styling preferences or allergy notes
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val referenceNumber: String, // e.g. "BK-82914"
    val customerId: String,
    val customerName: String,
    val customerPhone: String,
    val customerEmail: String = "",
    val serviceId: Long,
    val serviceName: String,
    val servicePrice: Double,
    val serviceDurationMinutes: Int,
    val staffId: Long,
    val staffName: String,
    val bookingDate: String, // "YYYY-MM-DD"
    val startTime: String,   // "HH:mm"
    val endTime: String,     // "HH:mm"
    val status: String,      // PENDING, CONFIRMED, IN_PROGRESS, COMPLETED, REJECTED, CANCELLED, POSTPONED
    val isPostponed: Boolean = false,
    val postponedNewDate: String = "",
    val postponedNewTime: String = "",
    val postponeReason: String = "",
    val notes: String = "",
    val paymentMethod: String = "CASH",
    val isCashCollected: Boolean = false,
    val cashCollectedAt: Long? = null,
    val rejectReason: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "support_messages")
data class SupportMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ticketCustomerId: String,
    val customerName: String,
    val senderId: String,
    val senderName: String,
    val senderRole: String, // "CUSTOMER" or "ADMIN"
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recipientRole: String, // "ADMIN" or "CUSTOMER"
    val recipientUserId: String? = null,
    val title: String,
    val body: String,
    val type: String, // "NEW_BOOKING", "STATUS_CHANGE", "PAYMENT_COLLECTED", "SUPPORT"
    val referenceId: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)
