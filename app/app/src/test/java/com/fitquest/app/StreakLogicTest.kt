package com.fitquest.app

import com.fitquest.app.domain.streaks.StreakTransition
import com.fitquest.app.domain.streaks.classifyTransition
import com.fitquest.app.domain.streaks.nextStreakDays
import org.junit.Assert.assertEquals
import org.junit.Test

class StreakLogicTest {
    @Test fun `first activity sets streak to 1`() {
        assertEquals(1, nextStreakDays(0, StreakTransition.First))
    }

    @Test fun `increment grows`() {
        assertEquals(7, nextStreakDays(6, StreakTransition.Increment))
    }

    @Test fun `same day no change`() {
        assertEquals(10, nextStreakDays(10, StreakTransition.Same))
    }

    @Test fun `reset returns to 1`() {
        assertEquals(1, nextStreakDays(99, StreakTransition.Reset))
    }

    @Test fun `classify same day`() {
        assertEquals(
            StreakTransition.Same,
            classifyTransition(daysAgo = 0),
        )
    }

    @Test fun `classify yesterday`() {
        assertEquals(
            StreakTransition.Increment,
            classifyTransition(daysAgo = 1),
        )
    }

    @Test fun `classify gap`() {
        assertEquals(
            StreakTransition.Reset,
            classifyTransition(daysAgo = 3),
        )
    }
}
