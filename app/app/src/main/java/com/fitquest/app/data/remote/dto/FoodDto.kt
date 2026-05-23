package com.fitquest.app.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LogFoodRequest(
    @Json(name = "food_name") val foodName: String,
    val calories: Double,
    @Json(name = "protein_g") val proteinG: Double,
    @Json(name = "carbs_g") val carbsG: Double,
    @Json(name = "fat_g") val fatG: Double,
    @Json(name = "serving_g") val servingG: Double?,
    @Json(name = "meal_type") val mealType: String,
)

@JsonClass(generateAdapter = true)
data class FoodEntryResponse(
    val id: String,
    @Json(name = "food_name") val foodName: String,
    val calories: Double,
    @Json(name = "protein_g") val proteinG: Double,
    @Json(name = "carbs_g") val carbsG: Double,
    @Json(name = "fat_g") val fatG: Double,
    @Json(name = "serving_g") val servingG: Double?,
    @Json(name = "meal_type") val mealType: String,
    @Json(name = "logged_at") val loggedAt: String,
    @Json(name = "xp_earned") val xpEarned: Int,
)

@JsonClass(generateAdapter = true)
data class FoodLogResponse(
    val entry: FoodEntryResponse,
    @Json(name = "xp_earned") val xpEarned: Int,
    @Json(name = "stat_gains") val statGains: List<StatGain>,
    @Json(name = "leveled_up") val leveledUp: Boolean,
    @Json(name = "new_level") val newLevel: Int?,
    @Json(name = "streak_days") val streakDays: Int,
)

@JsonClass(generateAdapter = true)
data class FoodTotals(
    val calories: Double,
    @Json(name = "protein_g") val proteinG: Double,
    @Json(name = "carbs_g") val carbsG: Double,
    @Json(name = "fat_g") val fatG: Double,
)

@JsonClass(generateAdapter = true)
data class TodayFoodResponse(
    val totals: FoodTotals,
    val entries: List<FoodEntryResponse>,
)
