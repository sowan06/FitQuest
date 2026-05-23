package com.fitquest.app.data.auth

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * JWT and FCM token storage. Uses EncryptedSharedPreferences so secrets never
 * land in plain shared prefs. Exposes a StateFlow so navigation can react to
 * logout (e.g. on a 401 the AuthInterceptor calls clear()).
 */
@Singleton
class TokenStore @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val prefs = run {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            "fitquest_secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    private val _authState = MutableStateFlow(prefs.getString(KEY_TOKEN, null))
    val authState: StateFlow<String?> = _authState.asStateFlow()

    fun token(): String? = _authState.value

    fun setToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
        _authState.value = token
    }

    fun clear() {
        prefs.edit().remove(KEY_TOKEN).apply()
        _authState.value = null
    }

    fun fcmToken(): String? = prefs.getString(KEY_FCM, null)

    fun setFcmToken(token: String) {
        prefs.edit().putString(KEY_FCM, token).apply()
    }

    private companion object {
        const val KEY_TOKEN = "jwt"
        const val KEY_FCM = "fcm_token"
    }
}
