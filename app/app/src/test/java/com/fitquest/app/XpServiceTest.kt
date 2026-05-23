package com.fitquest.app

import com.fitquest.app.domain.xp.applyXp
import com.fitquest.app.domain.xp.avatarLabelForLevel
import com.fitquest.app.domain.xp.avatarStageForLevel
import com.fitquest.app.domain.xp.workoutXp
import com.fitquest.app.domain.xp.xpToNextLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Mirrors the backend XP rules so the client can render optimistic previews.
 */
class XpServiceTest {

    @Test fun `workout xp base`() {
        assertEquals(20, workoutXp(0.0))
    }

    @Test fun `workout xp volume bonus`() {
        assertEquals(25, workoutXp(5_000.0))
    }

    @Test fun `negative volume safe`() {
        assertEquals(20, workoutXp(-100.0))
    }

    @Test fun `xpToNextLevel level 1 is 100`() {
        assertEquals(100, xpToNextLevel(1))
    }

    @Test fun `single level up applies cleanly`() {
        val res = applyXp(level = 1, xp = 0, xpToNext = 100, gained = 100)
        assertEquals(2, res.level)
        assertEquals(0, res.xp)
        assertTrue(res.leveledUp)
    }

    @Test fun `multi level up chain`() {
        val res = applyXp(level = 1, xp = 0, xpToNext = 100, gained = 400)
        assertTrue(res.level >= 3)
    }

    @Test fun `avatar stages by level`() {
        assertEquals(0, avatarStageForLevel(1))
        assertEquals(1, avatarStageForLevel(5))
        assertEquals(2, avatarStageForLevel(15))
        assertEquals(3, avatarStageForLevel(30))
    }

    @Test fun `avatar labels`() {
        assertEquals("Rookie", avatarLabelForLevel(1))
        assertEquals("Legend", avatarLabelForLevel(99))
    }
}
