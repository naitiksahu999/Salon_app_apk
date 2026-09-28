package com.example.data.backend

import android.content.Context
import android.util.Log
import com.example.data.db.SalonDao
import com.example.data.model.BookingEntity
import com.example.data.model.SalonBreakEntity
import com.example.data.model.SalonConfigEntity
import com.example.data.model.SalonHolidayEntity
import com.example.data.model.SalonWorkingDayEntity
import com.example.data.model.ServiceEntity
import com.example.data.model.StaffEntity
import com.example.data.model.UserEntity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.Locale

/**
 * Firebase Backend Service for Unisex Salon.
 * Provides bidirectional real-time cloud synchronization with Firebase Cloud Firestore
 * and Firebase Authentication so that updates made by Admin are instantaneously visible
 * to all logged-in customers across multiple devices.
 */
class FirebaseBackendService(private val context: Context) {

    private val tag = "FirebaseBackend"
    private val activeListeners = mutableListOf<ListenerRegistration>()

    val isFirebaseInitialized: Boolean
        get() = try {
            FirebaseApp.getApps(context).isNotEmpty()
        } catch (e: Exception) {
            Log.w(tag, "FirebaseApp check notice: ${e.localizedMessage}")
            false
        }

    val firestore: FirebaseFirestore?
        get() = if (isFirebaseInitialized) {
            try {
                FirebaseFirestore.getInstance()
            } catch (e: Exception) {
                Log.w(tag, "Firestore instance not available: ${e.localizedMessage}")
                null
            }
        } else null

    private val auth: FirebaseAuth?
        get() = if (isFirebaseInitialized) {
            try {
                FirebaseAuth.getInstance()
            } catch (e: Exception) {
                Log.w(tag, "FirebaseAuth instance not available: ${e.localizedMessage}")
                null
            }
        } else null

    // -------------------------------------------------------------
    // USERS SYNC
    // -------------------------------------------------------------
    suspend fun syncUserToCloud(user: UserEntity): Boolean {
        val db = firestore ?: return false
        return try {
            val userMap = hashMapOf(
                "id" to user.id,
                "name" to user.name,
                "phone" to user.phone,
                "email" to user.email,
                "role" to user.role,
                "avatarInitial" to user.avatarInitial,
                "preferredServices" to user.preferredServices,
                "gender" to user.gender,
                "birthday" to user.birthday,
                "notes" to user.notes,
                "createdAt" to user.createdAt,
                "lastSyncedAt" to System.currentTimeMillis()
            )
            db.collection("users").document(user.id)
                .set(userMap, SetOptions.merge())
                .await()
            Log.d(tag, "User ${user.id} synced to Firebase Firestore.")
            true
        } catch (e: Exception) {
            Log.e(tag, "Error syncing user to Firestore: ${e.localizedMessage}")
            false
        }
    }

    // -------------------------------------------------------------
    // SERVICES SYNC
    // -------------------------------------------------------------
    suspend fun syncServiceToCloud(service: ServiceEntity): Boolean {
        val db = firestore ?: return false
        return try {
            val map = hashMapOf(
                "id" to service.id,
                "name" to service.name,
                "category" to service.category,
                "description" to service.description,
                "durationMinutes" to service.durationMinutes,
                "price" to service.price,
                "iconKey" to service.iconKey,
                "imageUrl" to service.imageUrl,
                "isActive" to service.isActive,
                "updatedAt" to System.currentTimeMillis()
            )
            val docId = if (service.id > 0) service.id.toString() else service.name.hashCode().toString()
            db.collection("services").document(docId)
                .set(map, SetOptions.merge())
                .await()
            Log.d(tag, "Service ${service.name} synced to Firebase Firestore.")
            true
        } catch (e: Exception) {
            Log.e(tag, "Error syncing service to Firestore: ${e.localizedMessage}")
            false
        }
    }

    suspend fun deleteServiceFromCloud(serviceId: Long): Boolean {
        val db = firestore ?: return false
        return try {
            db.collection("services").document(serviceId.toString()).delete().await()
            true
        } catch (e: Exception) {
            Log.e(tag, "Error deleting service from Firestore: ${e.localizedMessage}")
            false
        }
    }

    // -------------------------------------------------------------
    // STAFF SYNC
    // -------------------------------------------------------------
    suspend fun syncStaffToCloud(staff: StaffEntity): Boolean {
        val db = firestore ?: return false
        return try {
            val map = hashMapOf(
                "id" to staff.id,
                "name" to staff.name,
                "roleTitle" to staff.roleTitle,
                "specialty" to staff.specialty,
                "rating" to staff.rating.toDouble(),
                "avatarColorHex" to staff.avatarColorHex,
                "isActive" to staff.isActive,
                "phone" to staff.phone,
                "email" to staff.email,
                "updatedAt" to System.currentTimeMillis()
            )
            val docId = if (staff.id > 0) staff.id.toString() else staff.name.hashCode().toString()
            db.collection("staff").document(docId)
                .set(map, SetOptions.merge())
                .await()
            true
        } catch (e: Exception) {
            Log.e(tag, "Error syncing staff to Firestore: ${e.localizedMessage}")
            false
        }
    }

    suspend fun deleteStaffFromCloud(staffId: Long): Boolean {
        val db = firestore ?: return false
        return try {
            db.collection("staff").document(staffId.toString()).delete().await()
            true
        } catch (e: Exception) {
            Log.e(tag, "Error deleting staff from Firestore: ${e.localizedMessage}")
            false
        }
    }

    // -------------------------------------------------------------
    // SALON CONFIG SYNC
    // -------------------------------------------------------------
    suspend fun syncSalonConfigToCloud(config: SalonConfigEntity): Boolean {
        val db = firestore ?: return false
        return try {
            val map = hashMapOf(
                "id" to config.id,
                "salonName" to config.salonName,
                "tagline" to config.tagline,
                "phone" to config.phone,
                "email" to config.email,
                "adminGmail" to config.adminGmail,
                "adminPhone" to config.adminPhone,
                "address" to config.address,
                "currencySymbol" to config.currencySymbol,
                "taxRate" to config.taxRate,
                "cancelGraceHours" to config.cancelGraceHours,
                "updatedAt" to System.currentTimeMillis()
            )
            db.collection("salon_config").document("1")
                .set(map, SetOptions.merge())
                .await()
            true
        } catch (e: Exception) {
            Log.e(tag, "Error syncing salon config to Firestore: ${e.localizedMessage}")
            false
        }
    }

    // -------------------------------------------------------------
    // WORKING DAYS & BREAKS SYNC
    // -------------------------------------------------------------
    suspend fun syncWorkingDayToCloud(day: SalonWorkingDayEntity): Boolean {
        val db = firestore ?: return false
        return try {
            val map = hashMapOf(
                "id" to day.id,
                "dayOfWeek" to day.dayOfWeek,
                "dayName" to day.dayName,
                "isOpen" to day.isOpen,
                "openTime" to day.openTime,
                "closeTime" to day.closeTime,
                "updatedAt" to System.currentTimeMillis()
            )
            db.collection("working_days").document(day.dayOfWeek.toString())
                .set(map, SetOptions.merge())
                .await()
            true
        } catch (e: Exception) {
            Log.e(tag, "Error syncing working day to Firestore: ${e.localizedMessage}")
            false
        }
    }

    suspend fun syncBreakToCloud(salonBreak: SalonBreakEntity): Boolean {
        val db = firestore ?: return false
        return try {
            val map = hashMapOf(
                "id" to salonBreak.id,
                "title" to salonBreak.title,
                "startTime" to salonBreak.startTime,
                "endTime" to salonBreak.endTime,
                "appliesToAllStaff" to salonBreak.appliesToAllStaff,
                "staffId" to salonBreak.staffId,
                "updatedAt" to System.currentTimeMillis()
            )
            db.collection("salon_breaks").document(salonBreak.id.toString())
                .set(map, SetOptions.merge())
                .await()
            true
        } catch (e: Exception) {
            Log.e(tag, "Error syncing break to Firestore: ${e.localizedMessage}")
            false
        }
    }

    suspend fun deleteBreakFromCloud(breakId: Long): Boolean {
        val db = firestore ?: return false
        return try {
            db.collection("salon_breaks").document(breakId.toString()).delete().await()
            true
        } catch (e: Exception) {
            Log.e(tag, "Error deleting break from Firestore: ${e.localizedMessage}")
            false
        }
    }

    // -------------------------------------------------------------
    // BOOKINGS SYNC
    // -------------------------------------------------------------
    suspend fun syncBookingToCloud(booking: BookingEntity): Boolean {
        val db = firestore ?: return false
        return try {
            val bookingMap = hashMapOf(
                "id" to booking.id,
                "referenceNumber" to booking.referenceNumber,
                "customerId" to booking.customerId,
                "customerName" to booking.customerName,
                "customerPhone" to booking.customerPhone,
                "customerEmail" to booking.customerEmail,
                "serviceId" to booking.serviceId,
                "serviceName" to booking.serviceName,
                "servicePrice" to booking.servicePrice,
                "serviceDurationMinutes" to booking.serviceDurationMinutes,
                "staffId" to booking.staffId,
                "staffName" to booking.staffName,
                "bookingDate" to booking.bookingDate,
                "startTime" to booking.startTime,
                "endTime" to booking.endTime,
                "status" to booking.status,
                "isPostponed" to booking.isPostponed,
                "postponedNewDate" to booking.postponedNewDate,
                "postponedNewTime" to booking.postponedNewTime,
                "postponeReason" to booking.postponeReason,
                "rejectReason" to booking.rejectReason,
                "notes" to booking.notes,
                "paymentMethod" to booking.paymentMethod,
                "isCashCollected" to booking.isCashCollected,
                "createdAt" to booking.createdAt,
                "updatedAt" to booking.updatedAt
            )
            val docId = if (booking.id > 0) booking.id.toString() else booking.referenceNumber
            db.collection("bookings").document(docId)
                .set(bookingMap, SetOptions.merge())
                .await()
            Log.d(tag, "Booking ${booking.referenceNumber} synced to Firebase Firestore.")
            true
        } catch (e: Exception) {
            Log.e(tag, "Error syncing booking to Firestore: ${e.localizedMessage}")
            false
        }
    }

    suspend fun updateBookingStatusOnCloud(bookingId: Long, reference: String, status: String): Boolean {
        val db = firestore ?: return false
        return try {
            val docId = if (bookingId > 0) bookingId.toString() else reference
            db.collection("bookings").document(docId)
                .update(
                    mapOf(
                        "status" to status,
                        "updatedAt" to System.currentTimeMillis()
                    )
                )
                .await()
            true
        } catch (e: Exception) {
            Log.e(tag, "Error updating status on Firestore: ${e.localizedMessage}")
            false
        }
    }

    // -------------------------------------------------------------
    // GMAIL & EMAIL DISPATCH
    // -------------------------------------------------------------
    suspend fun dispatchMailToCloud(
        toEmail: String,
        subject: String,
        textBody: String,
        htmlBody: String,
        otpCode: String? = null
    ): Boolean {
        val db = firestore ?: return false
        return try {
            val mailMap = hashMapOf(
                "to" to listOf(toEmail),
                "message" to hashMapOf(
                    "subject" to subject,
                    "text" to textBody,
                    "html" to htmlBody
                ),
                "otpCode" to (otpCode ?: ""),
                "recipientEmail" to toEmail,
                "status" to "QUEUED",
                "createdAt" to System.currentTimeMillis()
            )
            db.collection("mail").add(mailMap).await()

            if (!otpCode.isNullOrBlank()) {
                val cleanEmail = toEmail.trim().lowercase(Locale.US)
                db.collection("otp_verifications").document(cleanEmail).set(
                    hashMapOf(
                        "email" to cleanEmail,
                        "otpCode" to otpCode,
                        "createdAt" to System.currentTimeMillis(),
                        "expiresAt" to System.currentTimeMillis() + (10 * 60 * 1000)
                    )
                ).await()
            }
            Log.d(tag, "Email for $toEmail dispatched to Firestore 'mail' collection.")
            true
        } catch (e: Exception) {
            Log.e(tag, "Error dispatching email to cloud: ${e.localizedMessage}")
            false
        }
    }

    // -------------------------------------------------------------
    // REAL-TIME MULTI-DEVICE SNAPSHOT LISTENERS
    // -------------------------------------------------------------
    /**
     * Starts listening to all real-time Firestore collections (services, bookings, staff,
     * salon_config, working_days, salon_breaks).
     * Any change made by Admin on one device is pushed instantaneously to all other logged-in
     * devices through Room DB and Jetpack Compose StateFlow!
     */
    fun startRealtimeCloudSync(dao: SalonDao, scope: CoroutineScope) {
        val db = firestore ?: run {
            Log.w(tag, "Firestore not available; real-time listeners skipped.")
            return
        }

        // Clear existing listeners if re-attaching
        stopRealtimeCloudSync()

        // Initial Seed Check: If Firestore collections are empty, seed from local Room DB
        scope.launch(Dispatchers.IO) {
            try {
                val servSnap = db.collection("services").limit(1).get().await()
                if (servSnap.isEmpty) {
                    val localServices = dao.getAllServicesSync()
                    for (s in localServices) {
                        syncServiceToCloud(s)
                    }
                }
                val staffSnap = db.collection("staff").limit(1).get().await()
                if (staffSnap.isEmpty) {
                    val localStaff = dao.getAllStaffSync()
                    for (st in localStaff) {
                        syncStaffToCloud(st)
                    }
                }
                val configSnap = db.collection("salon_config").document("1").get().await()
                if (!configSnap.exists()) {
                    dao.getSalonConfigSync()?.let { syncSalonConfigToCloud(it) }
                }
                val daysSnap = db.collection("working_days").limit(1).get().await()
                if (daysSnap.isEmpty) {
                    val localDays = dao.getAllWorkingDaysSync()
                    for (d in localDays) {
                        syncWorkingDayToCloud(d)
                    }
                }
            } catch (e: Exception) {
                Log.w(tag, "Initial cloud seed notice: ${e.localizedMessage}")
            }
        }

        // 1. SERVICES REAL-TIME LISTENER
        try {
            val servicesSub = db.collection("services").addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(tag, "Real-time services listener error: ${error.localizedMessage}")
                    return@addSnapshotListener
                }
                snapshot?.let { snap ->
                    scope.launch(Dispatchers.IO) {
                        for (dc in snap.documentChanges) {
                            val doc = dc.document
                            val id = doc.getLong("id") ?: doc.id.toLongOrNull() ?: 0L
                            when (dc.type) {
                                DocumentChange.Type.ADDED, DocumentChange.Type.MODIFIED -> {
                                    val entity = ServiceEntity(
                                        id = id,
                                        name = doc.getString("name") ?: "Salon Service",
                                        category = doc.getString("category") ?: "Hair Cut",
                                        description = doc.getString("description") ?: "",
                                        durationMinutes = doc.getLong("durationMinutes")?.toInt() ?: 60,
                                        price = doc.getDouble("price") ?: 50.0,
                                        iconKey = doc.getString("iconKey") ?: "scissors",
                                        imageUrl = doc.getString("imageUrl") ?: "",
                                        isActive = doc.getBoolean("isActive") ?: true
                                    )
                                    dao.insertService(entity)
                                }
                                DocumentChange.Type.REMOVED -> {
                                    if (id > 0) dao.deleteServiceById(id)
                                }
                            }
                        }
                    }
                }
            }
            activeListeners.add(servicesSub)
        } catch (e: Exception) {
            Log.w(tag, "Failed to attach services listener: ${e.localizedMessage}")
        }

        // 2. BOOKINGS REAL-TIME LISTENER
        try {
            val bookingsSub = db.collection("bookings").addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(tag, "Real-time bookings listener error: ${error.localizedMessage}")
                    return@addSnapshotListener
                }
                snapshot?.let { snap ->
                    scope.launch(Dispatchers.IO) {
                        for (dc in snap.documentChanges) {
                            val doc = dc.document
                            val id = doc.getLong("id") ?: doc.id.toLongOrNull() ?: 0L
                            when (dc.type) {
                                DocumentChange.Type.ADDED, DocumentChange.Type.MODIFIED -> {
                                    val entity = BookingEntity(
                                        id = id,
                                        referenceNumber = doc.getString("referenceNumber") ?: "BK-${doc.id.take(6).uppercase()}",
                                        customerId = doc.getString("customerId") ?: "",
                                        customerName = doc.getString("customerName") ?: "Client",
                                        customerPhone = doc.getString("customerPhone") ?: "",
                                        customerEmail = doc.getString("customerEmail") ?: "",
                                        serviceId = doc.getLong("serviceId") ?: 1L,
                                        serviceName = doc.getString("serviceName") ?: "Service",
                                        servicePrice = doc.getDouble("servicePrice") ?: 0.0,
                                        serviceDurationMinutes = doc.getLong("serviceDurationMinutes")?.toInt() ?: 60,
                                        staffId = doc.getLong("staffId") ?: 1L,
                                        staffName = doc.getString("staffName") ?: "Specialist",
                                        bookingDate = doc.getString("bookingDate") ?: "",
                                        startTime = doc.getString("startTime") ?: "10:00 AM",
                                        endTime = doc.getString("endTime") ?: "11:00 AM",
                                        status = doc.getString("status") ?: "PENDING",
                                        isPostponed = doc.getBoolean("isPostponed") ?: false,
                                        postponedNewDate = doc.getString("postponedNewDate") ?: "",
                                        postponedNewTime = doc.getString("postponedNewTime") ?: "",
                                        postponeReason = doc.getString("postponeReason") ?: "",
                                        rejectReason = doc.getString("rejectReason") ?: "",
                                        notes = doc.getString("notes") ?: "",
                                        paymentMethod = doc.getString("paymentMethod") ?: "CASH",
                                        isCashCollected = doc.getBoolean("isCashCollected") ?: false,
                                        createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis(),
                                        updatedAt = doc.getLong("updatedAt") ?: System.currentTimeMillis()
                                    )
                                    dao.insertBooking(entity)
                                }
                                DocumentChange.Type.REMOVED -> {
                                    if (id > 0) dao.deleteBookingById(id)
                                }
                            }
                        }
                    }
                }
            }
            activeListeners.add(bookingsSub)
        } catch (e: Exception) {
            Log.w(tag, "Failed to attach bookings listener: ${e.localizedMessage}")
        }

        // 3. STAFF REAL-TIME LISTENER
        try {
            val staffSub = db.collection("staff").addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                snapshot?.let { snap ->
                    scope.launch(Dispatchers.IO) {
                        for (dc in snap.documentChanges) {
                            val doc = dc.document
                            val id = doc.getLong("id") ?: doc.id.toLongOrNull() ?: 0L
                            when (dc.type) {
                                DocumentChange.Type.ADDED, DocumentChange.Type.MODIFIED -> {
                                    val staff = StaffEntity(
                                        id = id,
                                        name = doc.getString("name") ?: "Specialist",
                                        roleTitle = doc.getString("roleTitle") ?: "Stylist",
                                        specialty = doc.getString("specialty") ?: "Hair Care",
                                        rating = (doc.getDouble("rating") ?: 4.9).toFloat(),
                                        avatarColorHex = doc.getLong("avatarColorHex") ?: 0xFF8E4D3E,
                                        isActive = doc.getBoolean("isActive") ?: true,
                                        phone = doc.getString("phone") ?: "",
                                        email = doc.getString("email") ?: ""
                                    )
                                    dao.insertStaff(staff)
                                }
                                DocumentChange.Type.REMOVED -> {
                                    if (id > 0) dao.deleteStaffById(id)
                                }
                            }
                        }
                    }
                }
            }
            activeListeners.add(staffSub)
        } catch (e: Exception) {
            Log.w(tag, "Failed to attach staff listener: ${e.localizedMessage}")
        }

        // 4. SALON CONFIG REAL-TIME LISTENER
        try {
            val configSub = db.collection("salon_config").document("1").addSnapshotListener { doc, error ->
                if (error != null || doc == null || !doc.exists()) return@addSnapshotListener
                scope.launch(Dispatchers.IO) {
                    val config = SalonConfigEntity(
                        id = 1L,
                        salonName = doc.getString("salonName") ?: "Unisex Salon",
                        tagline = doc.getString("tagline") ?: "Luxury Hair & Beauty Lounge",
                        phone = doc.getString("phone") ?: "+91 98765 43210",
                        email = doc.getString("email") ?: "contact@unisexsalon.com",
                        adminGmail = doc.getString("adminGmail") ?: "",
                        adminPhone = doc.getString("adminPhone") ?: "",
                        address = doc.getString("address") ?: "123 Wellness Street, Green Park, Delhi",
                        currencySymbol = doc.getString("currencySymbol") ?: "₹",
                        taxRate = doc.getDouble("taxRate") ?: 0.05,
                        cancelGraceHours = doc.getLong("cancelGraceHours")?.toInt() ?: 2,
                        updatedAt = doc.getLong("updatedAt") ?: System.currentTimeMillis()
                    )
                    dao.insertOrUpdateConfig(config)
                }
            }
            activeListeners.add(configSub)
        } catch (e: Exception) {
            Log.w(tag, "Failed to attach config listener: ${e.localizedMessage}")
        }

        // 5. WORKING DAYS REAL-TIME LISTENER
        try {
            val workingDaysSub = db.collection("working_days").addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                snapshot?.let { snap ->
                    scope.launch(Dispatchers.IO) {
                        for (doc in snap.documents) {
                            val dayOfWeek = doc.getLong("dayOfWeek")?.toInt() ?: continue
                            val day = SalonWorkingDayEntity(
                                id = doc.getLong("id") ?: dayOfWeek.toLong(),
                                dayOfWeek = dayOfWeek,
                                dayName = doc.getString("dayName") ?: "Day",
                                isOpen = doc.getBoolean("isOpen") ?: true,
                                openTime = doc.getString("openTime") ?: "09:00",
                                closeTime = doc.getString("closeTime") ?: "19:00"
                            )
                            dao.updateWorkingDay(day)
                        }
                    }
                }
            }
            activeListeners.add(workingDaysSub)
        } catch (e: Exception) {
            Log.w(tag, "Failed to attach working days listener: ${e.localizedMessage}")
        }

        Log.d(tag, "Real-time Firestore listeners started successfully across ${activeListeners.size} collections.")
    }

    fun stopRealtimeCloudSync() {
        for (reg in activeListeners) {
            try {
                reg.remove()
            } catch (_: Exception) {}
        }
        activeListeners.clear()
    }

    // -------------------------------------------------------------
    // AUTHENTICATION
    // -------------------------------------------------------------
    suspend fun registerUserWithFirebaseAuth(email: String, passwordRaw: String): Result<String> {
        val fbAuth = auth ?: return Result.failure(IllegalStateException("Firebase Auth not initialized on device"))
        return try {
            val res = fbAuth.createUserWithEmailAndPassword(email, passwordRaw).await()
            val uid = res.user?.uid ?: ""
            Result.success(uid)
        } catch (e: Exception) {
            Log.w(tag, "Firebase Auth registration fallback: ${e.localizedMessage}")
            Result.failure(e)
        }
    }

    suspend fun signInWithFirebaseAuth(email: String, passwordRaw: String): Result<String> {
        val fbAuth = auth ?: return Result.failure(IllegalStateException("Firebase Auth not initialized on device"))
        return try {
            val res = fbAuth.signInWithEmailAndPassword(email, passwordRaw).await()
            val uid = res.user?.uid ?: ""
            Result.success(uid)
        } catch (e: Exception) {
            Log.w(tag, "Firebase Auth sign-in fallback: ${e.localizedMessage}")
            Result.failure(e)
        }
    }

    companion object {
        fun hashPassword(password: String): String {
            return try {
                val md = MessageDigest.getInstance("SHA-256")
                val digest = md.digest(password.toByteArray(Charsets.UTF_8))
                digest.fold("") { str, it -> str + "%02x".format(it) }
            } catch (e: Exception) {
                password.hashCode().toString()
            }
        }
    }
}
