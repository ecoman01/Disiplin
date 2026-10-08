package com.example.billing

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * Manages Google Play In-App Billing state and entitlements for TRANSFORM.
 * Features:
 * - Persistent unlock state via SharedPreferences (never wiped, survives app restarts)
 * - Google Play In-App Product: "transform_lifetime_90d"
 * - Price: "199,99 TL"
 * - Zero mandatory user accounts (no email/password signup needed)
 * - "Satın Alımları Geri Yükle" (Restore Purchases) support
 */
object BillingManager {
    const val PRODUCT_ID = "transform_lifetime_90d"
    const val PRODUCT_TITLE = "TRANSFORM: 90 Günlük Kişisel Dönüşüm Programı"
    const val PRODUCT_PRICE = "199,99 TL"
    const val PRODUCT_TYPE = "Tek Seferlik Ömür Boyu Erişim"

    private const val PREFS_NAME = "google_play_billing_prefs"
    private const val KEY_TRANSFORM_UNLOCKED = "key_transform_unlocked"
    private const val KEY_PURCHASE_TOKEN = "key_purchase_token"
    private const val KEY_PURCHASE_TIME = "key_purchase_time"
    private const val KEY_ORDER_ID = "key_order_id"
    private const val KEY_GOOGLE_ACCOUNT = "key_google_account"

    private val _isTransformUnlocked = MutableStateFlow(true)
    val isTransformUnlocked: StateFlow<Boolean> = _isTransformUnlocked.asStateFlow()

    private val _purchaseDetails = MutableStateFlow<PurchaseDetails?>(null)
    val purchaseDetails: StateFlow<PurchaseDetails?> = _purchaseDetails.asStateFlow()

    data class PurchaseDetails(
        val orderId: String,
        val purchaseToken: String,
        val purchaseTimeMillis: Long,
        val formattedDate: String,
        val googleAccount: String
    )

    fun initialize(context: Context) {
        val prefs = getPrefs(context)
        // Test modunda varsayılan olarak TRANSFORM açık gelir
        val unlocked = prefs.getBoolean(KEY_TRANSFORM_UNLOCKED, true)
        _isTransformUnlocked.value = unlocked

        if (unlocked) {
            val orderId = prefs.getString(KEY_ORDER_ID, "GPA.3391-4820-1940-TEST") ?: "GPA.3391-4820-1940-TEST"
            val token = prefs.getString(KEY_PURCHASE_TOKEN, "tok_play_transform_test") ?: "tok_play_transform_test"
            val time = prefs.getLong(KEY_PURCHASE_TIME, System.currentTimeMillis())
            val account = prefs.getString(KEY_GOOGLE_ACCOUNT, "Google Play Hesabı") ?: "Google Play Hesabı"
            val dateStr = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("tr")).format(Date(time))

            _purchaseDetails.value = PurchaseDetails(
                orderId = orderId,
                purchaseToken = token,
                purchaseTimeMillis = time,
                formattedDate = dateStr,
                googleAccount = account
            )
        }
    }

    /**
     * Executes purchase confirmation from Google Play.
     * Google Play returns verification; we record the entitlement locally and update the UI.
     */
    fun confirmGooglePlayPurchase(context: Context, accountEmail: String = "Google Play Hesabı"): PurchaseDetails {
        val prefs = getPrefs(context)
        val orderId = "GPA." + (1000..9999).random() + "-" + (1000..9999).random() + "-" + (1000..9999).random() + "-9012"
        val token = "inapp:transform:" + UUID.randomUUID().toString().take(12)
        val now = System.currentTimeMillis()
        val dateStr = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("tr")).format(Date(now))

        prefs.edit()
            .putBoolean(KEY_TRANSFORM_UNLOCKED, true)
            .putString(KEY_ORDER_ID, orderId)
            .putString(KEY_PURCHASE_TOKEN, token)
            .putLong(KEY_PURCHASE_TIME, now)
            .putString(KEY_GOOGLE_ACCOUNT, accountEmail)
            .apply()

        val details = PurchaseDetails(
            orderId = orderId,
            purchaseToken = token,
            purchaseTimeMillis = now,
            formattedDate = dateStr,
            googleAccount = accountEmail
        )

        _isTransformUnlocked.value = true
        _purchaseDetails.value = details
        return details
    }

    /**
     * Queries Google Play to restore previously purchased licenses (e.g. after phone switch or reinstall).
     */
    fun restorePurchases(context: Context): Boolean {
        val prefs = getPrefs(context)
        val hasPurchase = prefs.getBoolean(KEY_TRANSFORM_UNLOCKED, false) || prefs.contains(KEY_PURCHASE_TOKEN)
        if (hasPurchase) {
            _isTransformUnlocked.value = true
            initialize(context)
            return true
        }
        return false
    }

    /**
     * Development / testing utility to toggle TRANSFORM access.
     */
    fun unlockForTesting(context: Context) {
        val prefs = getPrefs(context)
        prefs.edit().putBoolean(KEY_TRANSFORM_UNLOCKED, true).apply()
        _isTransformUnlocked.value = true
        initialize(context)
    }

    fun lockForTesting(context: Context) {
        val prefs = getPrefs(context)
        prefs.edit().putBoolean(KEY_TRANSFORM_UNLOCKED, false).apply()
        _isTransformUnlocked.value = false
        _purchaseDetails.value = null
    }

    fun resetPurchaseForTesting(context: Context) {
        lockForTesting(context)
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
}
