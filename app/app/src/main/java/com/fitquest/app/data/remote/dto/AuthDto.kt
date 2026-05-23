package com.fitquest.app.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(val email: String, val password: String)

@JsonClass(generateAdapter = true)
data class RegisterRequest(val username: String, val email: String, val password: String)

@JsonClass(generateAdapter = true)
data class AuthResponse(val token: String)

@JsonClass(generateAdapter = true)
data class UserResponse(
    val id: String,
    val username: String,
    val email: String,
    @Json(name = "created_at") val createdAt: String,
)

@JsonClass(generateAdapter = true)
data class FcmTokenRequest(@Json(name = "fcm_token") val fcmToken: String)
