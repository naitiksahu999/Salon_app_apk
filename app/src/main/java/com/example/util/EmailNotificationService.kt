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
import com.example.data.backend.FirebaseBackendService
import com.example.data.model.BookingEntity
import com.example.data.model.SalonConfigEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Handles formatting, dispatching, and notifying users and salon admins
 * of OTP email verifications and booking updates via real Gmail dispatch.
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
     * Dispatches real-time booking update email to the logged-in customer's Gmail and Admin Gmail.
     * Writes to cloud Firestore 'mail' collection and issues status notification with Gmail intent.
     */
    fun sendBookingEmailNotification(
        context: Context,
        booking: BookingEntity,
        salonConfig: SalonConfigEntity?,
        isPostponed: Boolean = false,
        postponeReason: String = "",
        firebaseBackend: FirebaseBackendService? = null,
        scope: CoroutineScope? = null
    ) {
        try {
            ensureNotificationChannel(context)

            val salonName = salonConfig?.salonName ?: "UNISEX SALON"
            val adminEmail = salonConfig?.adminGmail?.takeIf { it.isNotBlank() }
                ?: salonConfig?.email?.takeIf { it.isNotBlank() }
                ?: "admin.unisexsalon@gmail.com"
            val customerEmail = booking.customerEmail.ifBlank { "customer@gmail.com" }

            val subject = if (isPostponed) {
                "⚠️ APPOINTMENT POSTPONED - $salonName (${booking.referenceNumber})"
            } else {
                "✨ Booking Confirmed - $salonName (${booking.referenceNumber})"
            }

            val plainBody = if (isPostponed) {
                """
                Dear ${booking.customerName},

                Your appointment for ${booking.serviceName} has been POSTPONED by $salonName.
                • Reference: ${booking.referenceNumber}
                • New Date: ${if (booking.postponedNewDate.isNotBlank()) booking.postponedNewDate else booking.bookingDate}
                • New Time: ${if (booking.postponedNewTime.isNotBlank()) booking.postponedNewTime else booking.startTime}
                • Specialist: ${booking.staffName}
                • Reason: ${postponeReason.ifBlank { "Schedule adjustment" }}
                • Location: ${salonConfig?.address ?: "123 Wellness Street, Green Park, Delhi"}

                Dispatched to Customer Gmail: $customerEmail
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
                """.trimIndent()
            }

            val htmlBody = """
                <div style="font-family:'Segoe UI',Roboto,Helvetica,Arial,sans-serif; max-width:540px; margin:0 auto; padding:28px 24px; border:1px solid #ECECEC; border-radius:16px; background-color:#FFFFFF;">
                  <div style="text-align:center; padding-bottom:16px; border-bottom:1px solid #F0F0F0;">
                    <h1 style="color:#C69255; font-size:24px; margin:0; letter-spacing:2px; font-weight:800;">$salonName</h1>
                    <p style="color:#888888; font-size:12px; margin:4px 0 0 0; text-transform:uppercase; letter-spacing:1px;">Booking Confirmation Receipt</p>
                  </div>
                  <div style="padding:20px 0;">
                    <h2 style="color:#222222; font-size:18px; margin:0 0 6px 0;">${if (isPostponed) "⚠️ Appointment Postponed" else "✨ Appointment Confirmed"}</h2>
                    <p style="color:#666666; font-size:14px; margin:0 0 16px 0;">Hello ${booking.customerName},</p>
                    <table style="width:100%; border-collapse:collapse; font-size:14px; color:#444444;">
                      <tr style="border-bottom:1px solid #F4F4F4;"><td style="padding:8px 0; color:#888;">Reference</td><td style="padding:8px 0; font-weight:bold; text-align:right;">${booking.referenceNumber}</td></tr>
                      <tr style="border-bottom:1px solid #F4F4F4;"><td style="padding:8px 0; color:#888;">Service</td><td style="padding:8px 0; font-weight:bold; text-align:right;">${booking.serviceName} (${booking.serviceDurationMinutes}m)</td></tr>
                      <tr style="border-bottom:1px solid #F4F4F4;"><td style="padding:8px 0; color:#888;">Specialist</td><td style="padding:8px 0; font-weight:bold; text-align:right;">${booking.staffName}</td></tr>
                      <tr style="border-bottom:1px solid #F4F4F4;"><td style="padding:8px 0; color:#888;">Date</td><td style="padding:8px 0; font-weight:bold; text-align:right;">${if (isPostponed && booking.postponedNewDate.isNotBlank()) booking.postponedNewDate else booking.bookingDate}</td></tr>
                      <tr style="border-bottom:1px solid #F4F4F4;"><td style="padding:8px 0; color:#888;">Time</td><td style="padding:8px 0; font-weight:bold; text-align:right;">${if (isPostponed && booking.postponedNewTime.isNotBlank()) booking.postponedNewTime else booking.startTime}</td></tr>
                      <tr style="border-bottom:1px solid #F4F4F4;"><td style="padding:8px 0; color:#888;">Price</td><td style="padding:8px 0; font-weight:bold; text-align:right; color:#C69255;">${salonConfig?.currencySymbol ?: "₹"}${booking.servicePrice.toInt()} (Cash in Salon)</td></tr>
                      ${if (isPostponed && postponeReason.isNotBlank()) "<tr style='border-bottom:1px solid #F4F4F4;'><td style='padding:8px 0; color:#E53935;'>Reason</td><td style='padding:8px 0; text-align:right; color:#E53935;'>$postponeReason</td></tr>" else ""}
                    </table>
                    <div style="background:#FBF9F6; border-radius:10px; padding:12px; margin-top:16px; font-size:13px; color:#666;">
                      📍 <strong>Location:</strong> ${salonConfig?.address ?: "123 Wellness Street, Green Park, Delhi"}<br/>
                      📞 <strong>Phone:</strong> ${salonConfig?.phone ?: "+91 98765 43210"}
                    </div>
                  </div>
                  <div style="border-top:1px solid #F0F0F0; padding-top:14px; text-align:center;">
                    <p style="color:#AAAAAA; font-size:11px; margin:0;">Sent to your registered Gmail: $customerEmail</p>
                  </div>
                </div>
            """.trimIndent()

            // Dispatch to Cloud Firestore 'mail' collection
            if (firebaseBackend != null) {
                val sc = scope ?: CoroutineScope(Dispatchers.IO)
                sc.launch {
                    firebaseBackend.dispatchMailToCloud(
                        toEmail = customerEmail,
                        subject = subject,
                        textBody = plainBody,
                        htmlBody = htmlBody
                    )
                    if (adminEmail.isNotBlank() && !adminEmail.equals(customerEmail, ignoreCase = true)) {
                        firebaseBackend.dispatchMailToCloud(
                            toEmail = adminEmail,
                            subject = "[Admin Copy] $subject",
                            textBody = plainBody,
                            htmlBody = htmlBody
                        )
                    }
                }
            }

            // Launch Gmail app when user taps notification
            val gmailIntent = context.packageManager.getLaunchIntentForPackage("com.google.android.gm")
                ?: Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_APP_EMAIL)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

            val pendingIntent = PendingIntent.getActivity(
                context,
                booking.id.toInt(),
                gmailIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle(if (isPostponed) "⚠️ Appointment Postponed" else "✨ Booking Confirmed (#${booking.referenceNumber})")
                .setContentText("Booking details dispatched to your Gmail ($customerEmail). Tap to view.")
                .setStyle(NotificationCompat.BigTextStyle().bigText("Details sent to $customerEmail:\n$plainBody"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            notificationManager.notify((booking.id + (if (isPostponed) 9000 else 1000)).toInt(), notification)

            AppFeedbackHelper.triggerNotification(context)
        } catch (e: Exception) {
            Log.e(TAG, "Error sending booking email notification", e)
        }
    }

    /**
     * Dispatches OTP verification code to the user's Gmail.
     * Stores in cloud Firestore 'mail' & 'otp_verifications' collections.
     * CRITICAL: Does NOT display the OTP digits inside the Salon app!
     */
    fun sendOtpVerificationEmail(
        context: Context,
        recipientEmail: String,
        recipientPhone: String,
        otpCode: String,
        purpose: String = "Verification",
        firebaseBackend: FirebaseBackendService? = null,
        scope: CoroutineScope? = null
    ) {
        try {
            ensureNotificationChannel(context)

            val subject = "UNISEX SALON - Your $purpose Code"
            val plainBody = """
                Hi,

                Your Unisex Salon $purpose 6-digit code is: $otpCode

                This code was requested for $recipientEmail.
                It is valid for 10 minutes. Do not share this code with anyone.

                Warm regards,
                Unisex Salon Team
            """.trimIndent()

            val htmlBody = """
                <div style="font-family:'Segoe UI',Roboto,Helvetica,Arial,sans-serif; max-width:540px; margin:0 auto; padding:28px 24px; border:1px solid #ECECEC; border-radius:16px; background-color:#FFFFFF;">
                  <div style="text-align:center; padding-bottom:16px; border-bottom:1px solid #F0F0F0;">
                    <h1 style="color:#C69255; font-size:24px; margin:0; letter-spacing:2px; font-weight:800;">UNISEX SALON</h1>
                    <p style="color:#888888; font-size:12px; margin:4px 0 0 0; text-transform:uppercase; letter-spacing:1px;">Premium Luxury Care For Everyone</p>
                  </div>
                  <div style="padding:24px 0; text-align:center;">
                    <h2 style="color:#222222; font-size:20px; margin:0 0 8px 0;">Verify Your Email Address</h2>
                    <p style="color:#666666; font-size:14px; margin:0 0 20px 0;">Please use the following 6-digit code to complete your $purpose:</p>
                    <div style="display:inline-block; background:#F8F4EF; border:2px dashed #C69255; border-radius:12px; padding:14px 28px; font-size:32px; font-weight:800; letter-spacing:8px; color:#222222;">
                      $otpCode
                    </div>
                    <p style="color:#999999; font-size:12px; margin:20px 0 0 0;">This code is valid for 10 minutes. Please enter it in the app to complete your verification.</p>
                  </div>
                  <div style="border-top:1px solid #F0F0F0; padding-top:16px; text-align:center;">
                    <p style="color:#AAAAAA; font-size:11px; margin:0;">© Unisex Salon • 123 Wellness Street, Green Park, Delhi</p>
                  </div>
                </div>
            """.trimIndent()

            // Dispatch to Cloud Firestore 'mail' and 'otp_verifications' collection
            if (firebaseBackend != null) {
                val sc = scope ?: CoroutineScope(Dispatchers.IO)
                sc.launch {
                    firebaseBackend.dispatchMailToCloud(
                        toEmail = recipientEmail,
                        subject = subject,
                        textBody = plainBody,
                        htmlBody = htmlBody,
                        otpCode = otpCode
                    )
                }
            }

            // Launch Gmail app when user taps notification
            val gmailIntent = context.packageManager.getLaunchIntentForPackage("com.google.android.gm")
                ?: Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_APP_EMAIL)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

            val pendingIntent = PendingIntent.getActivity(
                context,
                System.currentTimeMillis().toInt(),
                gmailIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("📧 Verification Code Dispatched")
                .setContentText("We sent a 6-digit verification code to $recipientEmail. Tap to check Gmail.")
                .setStyle(NotificationCompat.BigTextStyle().bigText("A 6-digit verification code has been dispatched to $recipientEmail.\nPlease open your Gmail inbox (or Spam/Promotions folder) to retrieve your code."))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
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
