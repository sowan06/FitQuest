package com.fitquest.app.domain.xp

import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Local mirror of backend `services/xp.js`. Lets the UI preview the XP a
 * pending action would award before it hits the network.
 */

data class AvatarStage(val stage: Int, val label: String, val minLevel: Int)

val avatarStages = listOf(
    AvatarStage(0, "Rookie", 1),
    AvatarStage(1, "Fighter", 5),
    AvatarStage(2, "Warrior", 15),
    AvatarStage(3, "Legend", 30),
)

fun xpToNextLevel(level: Int): Int =
    (100.0 * level.coerceAtLeast(1).toDouble().pow(1.5)).roundToInt()

fun avatarStageForLevel(level: Int): Int {
    var stage = 0
    for (s in avatarStages) {
        if (level >= s.minLevel) stage = s.stage
    }
    return stage
}

fun avatarLabelForLevel(level: Int): String =
    avatarStages.firstOrNull { it.stage == avatarStageForLevel(level) }?.label ?: "Rookie"

fun workoutXp(totalVolumeKg: Double): Int {
    val safe = if (totalVolumeKg.isFinite()) totalVolumeKg.coerceAtLeast(0.0) else 0.0
    return 20 + floor(safe / 1000.0).toInt()
}

fun foodXp(): Int = 5

data class XpResult(
    val level: Int,
    val xp: Int,
    val xpToNext: Int,
    val leveledUp: Boolean,
    val levelsGained: Int,
    val avatarStage: Int,
)

fun applyXp(level: Int, xp: Int, xpToNext: Int, gained: Int): XpResult {
    var l = level
    var current = xp + gained
    var threshold = xpToNext
    var leveledUp = false
    var gainedLevels = 0
    while (current >= threshold) {
        current -= threshold
        l += 1
        gainedLevels += 1
        threshold = xpToNextLevel(l)
        leveledUp = true
    }
    return XpResult(
        level = l,
        xp = current,
        xpToNext = threshold,
        leveledUp = leveledUp,
        levelsGained = gainedLevels,
        avatarStage = avatarStageForLevel(l),
    )
}
