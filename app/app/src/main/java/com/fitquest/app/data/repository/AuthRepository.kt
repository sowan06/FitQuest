package com.fitquest.app.data.repository

import com.fitquest.app.data.auth.TokenStore
import com.fitquest.app.data.remote.ApiService
import com.fitquest.app.data.remote.dto.FcmTokenRequest
import com.fitquest.app.data.remote.dto.LoginRequest
import com.fitquest.app.data.remote.dto.RegisterRequest
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: ApiService,
    private val tokenStore: TokenStore,
) {
    val authState: StateFlow<String?> = tokenStore.authState

    fun isLoggedIn(): Boolean = tokenStore.token() != null

    suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        val res = api.login(LoginRequest(email.trim(), password))
        tokenStore.setToken(res.token)
    }

    suspend fun register(username: String, email: String, password: String): Result<Unit> =
        runCatching {
            val res = api.register(RegisterRequest(username.trim(), email.trim(), password))
            tokenStore.setToken(res.token)
        }

    fun logout() {
        tokenStore.clear()
    }

    suspend fun pushFcmTokenIfNeeded(fcmToken: String): Result<Unit> = runCatching {
        if (tokenStore.token() == null) return@runCatching
        api.updateFcmToken(FcmTokenRequest(fcmToken))
        tokenStore.setFcmToken(fcmToken)
    }
}
