package com.example.util

import android.content.Context
import android.content.SharedPreferences

/**
 * Manages persistent user session and "Remember Me" credentials
 * so a registered or logged-in user stays logged in across app launches.
 */
class UserPreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("unisex_salon_user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_SAVED_USER_ID = "saved_user_id"
        private const val KEY_SAVED_USER_EMAIL = "saved_user_email"
        private const val KEY_SAVED_USER_NAME = "saved_user_name"
        private const val KEY_SAVED_USER_PHONE = "saved_user_phone"
        private const val KEY_REMEMBER_ME = "remember_me"
    }

    fun saveUserSession(userId: String, name: String, email: String, phone: String, rememberMe: Boolean = true) {
        prefs.edit()
            .putString(KEY_SAVED_USER_ID, userId)
            .putString(KEY_SAVED_USER_NAME, name)
            .putString(KEY_SAVED_USER_EMAIL, email)
            .putString(KEY_SAVED_USER_PHONE, phone)
            .putBoolean(KEY_REMEMBER_ME, rememberMe)
            .apply()
    }

    fun clearSession() {
        prefs.edit()
            .remove(KEY_SAVED_USER_ID)
            .remove(KEY_SAVED_USER_NAME)
            .remove(KEY_SAVED_USER_EMAIL)
            .remove(KEY_SAVED_USER_PHONE)
            .putBoolean(KEY_REMEMBER_ME, false)
            .apply()
    }

    fun getSavedUserId(): String? {
        return if (prefs.getBoolean(KEY_REMEMBER_ME, false)) {
            prefs.getString(KEY_SAVED_USER_ID, null)
        } else null
    }

    fun isRememberMeEnabled(): Boolean = prefs.getBoolean(KEY_REMEMBER_ME, true)
}
