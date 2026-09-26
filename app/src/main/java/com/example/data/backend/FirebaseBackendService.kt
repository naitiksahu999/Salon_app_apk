package com.example.data.backend

import android.content.Context
import android.util.Log
import com.example.data.model.BookingEntity
import com.example.data.model.UserEntity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest

/**
 * Firebase Backend Service for Unisex Salon.
 * Provides cloud synchronization with Firebase Cloud Firestore and Firebase Authentication.
 * Operates cooperatively with the local secure Room database.
 */
class FirebaseBackendService(private val context: Context) {

    private val tag = "FirebaseBackend"

    val isFirebaseInitialized: Boolean
        get() = try {
            FirebaseApp.getApps(context).isNotEmpty()
        } catch (e: Exception) {
            Log.w(tag, "FirebaseApp check notice: ${e.localizedMessage}")
            false
        }

    private val firestore: FirebaseFirestore?
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

    /**
     * Synchronize a registered user to Firebase Firestore.
     */
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

    /**
     * Synchronize a booking to Firebase Firestore.
     */
    suspend fun syncBookingToCloud(booking: BookingEntity): Boolean {
        val db = firestore ?: return false
        return try {
            val bookingMap = hashMapOf(
                "id" to booking.id,
                "referenceNumber" to booking.referenceNumber,
                "customerId" to booking.customerId,
                "customerName" to booking.customerName,
                "customerPhone" to booking.customerPhone,
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
                "notes" to booking.notes,
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

    /**
     * Updates booking status on Firebase Cloud Firestore.
     */
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

    /**
     * Authenticate or register with Firebase Auth if available.
     */
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
        /**
         * Securely hash password with SHA-256 for local persistence
         */
        fun hashPassword(password: String): String {
            return try {
                val md = MessageDigest.getInstance("SHA-256")
                val digest = md.digest(password.toByteArray(Charsets.UTF_8))
                digest.fold("") { str, it -> str + "%02x".format(it) }
            } catch (e: Exception) {
                // Fallback deterministic hash
                password.hashCode().toString()
            }
        }
    }
}
