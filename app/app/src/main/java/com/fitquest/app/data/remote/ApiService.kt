package com.fitquest.app.data.remote

import com.fitquest.app.data.remote.dto.AchievementResponse
import com.fitquest.app.data.remote.dto.AuthResponse
import com.fitquest.app.data.remote.dto.CharacterResponse
import com.fitquest.app.data.remote.dto.ClaimResponse
import com.fitquest.app.data.remote.dto.DailyVolumeResponse
import com.fitquest.app.data.remote.dto.FcmTokenRequest
import com.fitquest.app.data.remote.dto.FoodLogResponse
import com.fitquest.app.data.remote.dto.LogFoodRequest
import com.fitquest.app.data.remote.dto.LogWorkoutRequest
import com.fitquest.app.data.remote.dto.LoginRequest
import com.fitquest.app.data.remote.dto.QuestResponse
import com.fitquest.app.data.remote.dto.RegisterRequest
import com.fitquest.app.data.remote.dto.TodayFoodResponse
import com.fitquest.app.data.remote.dto.UpdateCharacterRequest
import com.fitquest.app.data.remote.dto.UserResponse
import com.fitquest.app.data.remote.dto.WorkoutListResponse
import com.fitquest.app.data.remote.dto.WorkoutSummaryResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): AuthResponse

    @GET("auth/me")
    suspend fun getMe(): UserResponse

    @PATCH("auth/fcm-token")
    suspend fun updateFcmToken(@Body body: FcmTokenRequest)

    @GET("character")
    suspend fun getCharacter(): CharacterResponse

    @PATCH("character")
    suspend fun updateCharacter(@Body body: UpdateCharacterRequest): CharacterResponse

    @POST("workouts")
    suspend fun logWorkout(@Body body: LogWorkoutRequest): WorkoutSummaryResponse

    @GET("workouts")
    suspend fun getWorkouts(@Query("page") page: Int = 1): WorkoutListResponse

    @GET("workouts/stats/volume")
    suspend fun getVolume(@Query("days") days: Int = 30): List<DailyVolumeResponse>

    @POST("food")
    suspend fun logFood(@Body body: LogFoodRequest): FoodLogResponse

    @GET("food/today")
    suspend fun getTodayFood(): TodayFoodResponse

    @GET("quests")
    suspend fun getQuests(): List<QuestResponse>

    @POST("quests/{id}/claim")
    suspend fun claimQuest(@Path("id") id: String): ClaimResponse

    @GET("achievements")
    suspend fun getAchievements(): List<AchievementResponse>
}
