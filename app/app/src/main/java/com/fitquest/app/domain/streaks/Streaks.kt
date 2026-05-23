package com.fitquest.app.domain.streaks

/**
 * Local mirror of backend streak rules — used for optimistic UI hints.
 */
enum class StreakTransition { First, Increment, Same, Reset }

fun classifyTransition(daysAgo: Int?): StreakTransition = when {
    daysAgo == null -> StreakTransition.First
    daysAgo == 0 -> StreakTransition.Same
    daysAgo == 1 -> StreakTransition.Increment
    else -> StreakTransition.Reset
}

fun nextStreakDays(currentStreak: Int, transition: StreakTransition): Int = when (transition) {
    StreakTransition.First, StreakTransition.Reset -> 1
    StreakTransition.Increment -> currentStreak + 1
    StreakTransition.Same -> currentStreak
}
