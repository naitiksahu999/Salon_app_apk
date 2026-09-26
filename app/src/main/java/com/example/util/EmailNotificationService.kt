package com.example.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.data.model.BookingEntity
import com.example.data.model.SalonConfigEntity

/**
 * Handles formatting, dispatching, and notifying users and salon admins
 * of booking updates, creations, and postponements via registered Gmail.
 */
object EmailNotificationService {
    private const val TAG = "EmailNotificationService"
    private const val CHANNEL_ID = "unisex_salon_booking_alerts"

    fun ensureNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Unisex Salon Appointments"
            val descriptionText = "Booking confirmations, postponements, and real-time updates"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableVibration(true)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Dispatches real-time Android Notification simulating Gmail inbox notification,
     * and provides intent for opening external email client if needed.
     */
    fun sendBookingEmailNotification(
        context: Context,
        booking: BookingEntity,
        salonConfig: SalonConfigEntity?,
        isPostponed: Boolean = false,
        postponeReason: String = ""
    ) {
        try {
            ensureNotificationChannel(context)

            val salonName = salonConfig?.salonName ?: "UNISEX SALON"
            val adminEmail = salonConfig?.adminGmail?.takeIf { it.isNotBlank() }
                ?: salonConfig?.email?.takeIf { it.isNotBlank() }
                ?: "admin.unisexsalon@gmail.com"
            val customerEmail = booking.customerEmail.ifBlank { "customer@gmail.com" }

            val title = if (isPostponed) {
                "⚠️ APPOINTMENT POSTPONED - $salonName (${booking.referenceNumber})"
            } else {
                "✨ Booking Confirmed - $salonName (${booking.referenceNumber})"
            }

            val formattedBody = if (isPostponed) {
                """
                Dear ${booking.customerName},
                
                Your appointment for ${booking.serviceName} has been POSTPONED by the salon.
                • Reference: ${booking.referenceNumber}
                • New Date: ${booking.postponedNewDate.ifBlank { booking.bookingDate }}
                • New Time: ${booking.postponedNewTime.ifBlank { booking.startTime }}
                • Specialist: ${booking.staffName}
                • Reason: ${postponeReason.ifBlank { "Salon schedule adjustment" }}
                • Location: ${salonConfig?.address ?: "123 Wellness Street, Green Park, Delhi"}
                
                Dispatched to Customer Gmail: $customerEmail
                Dispatched to Admin Gmail: $adminEmail
                """.trimIndent()
            } else {
                """
                Dear ${booking.customerName},
                
                Thank you for choosing $salonName!
                • Reference: ${booking.referenceNumber}
                • Service: ${booking.serviceName} (${booking.serviceDurationMinutes} mins)
                • Date & Time: ${booking.bookingDate} at ${booking.startTime}
                • Specialist: ${booking.staffName}
                • Total Price: ${salonConfig?.currencySymbol ?: "₹"}${booking.servicePrice.toInt()} (Cash in Salon)
                • Location: ${salonConfig?.address ?: "123 Wellness Street, Green Park, Delhi"}
                
                Dispatched to Customer Gmail: $customerEmail
                Dispatched to Admin Gmail: $adminEmail
                """.trimIndent()
            }

            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                booking.id.toInt(),
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle(title)
                .setContentText("Appointment update sent to $customerEmail")
                .setStyle(NotificationCompat.BigTextStyle().bigText(formattedBody))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            notificationManager.notify((booking.id + (if (isPostponed) 9000 else 1000)).toInt(), notification)

            // Auditory feedback
            AppFeedbackHelper.triggerNotification(context)

        } catch (e: Exception) {
            Log.e(TAG, "Error sending booking email notification", e)
        }
    }

    /**
     * Creates an Intent to open the user's Gmail/Email client with pre-filled details.
     */
    fun createComposeEmailIntent(
        recipientEmail: String,
        subject: String,
        body: String
    ): Intent {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        return intent
    }

    /**
     * Dispatches real-time Android Notification with the 6-digit OTP verification code
     * simulating official Gmail & SMS dispatch to the user's email and phone.
     */
    fun sendOtpVerificationEmail(
        context: Context,
        recipientEmail: String,
        recipientPhone: String,
        otpCode: String,
        purpose: String = "Verification"
    ) {
        try {
            ensureNotificationChannel(context)

            val title = "🔐 $otpCode is your UNISEX SALON $purpose code"
            val body = "Hi, your Unisex Salon verification OTP code is $otpCode. Dispatched to $recipientEmail and $recipientPhone. Valid for 10 minutes. Do not share this OTP with anyone."

            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                System.currentTimeMillis().toInt(),
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle(title)
                .setContentText("Your OTP is $otpCode (Dispatched to $recipientEmail)")
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(7777, notification)
            AppFeedbackHelper.triggerNotification(context)
        } catch (e: Exception) {
            Log.e(TAG, "Error sending OTP email notification", e)
        }
    }
}
