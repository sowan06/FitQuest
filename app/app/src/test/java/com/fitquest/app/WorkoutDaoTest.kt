package com.fitquest.app

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fitquest.app.data.local.FitQuestDatabase
import com.fitquest.app.data.local.entity.WorkoutSessionEntity
import com.fitquest.app.data.local.entity.WorkoutSetEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE, sdk = [33])
class WorkoutDaoTest {

    private lateinit var db: FitQuestDatabase

    @Before fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FitQuestDatabase::class.java,
        ).allowMainThreadQueries().build()
    }

    @After fun tearDown() {
        db.close()
    }

    @Test fun `insert and query unsynced sessions`() = runBlocking {
        val dao = db.workoutDao()
        val sessionId = dao.insertSession(
            WorkoutSessionEntity(
                totalVolumeKg = 800.0,
                loggedAt = System.currentTimeMillis(),
                isSynced = false,
            ),
        )
        dao.insertSets(
            listOf(
                WorkoutSetEntity(
                    localSessionId = sessionId,
                    exerciseName = "Bench",
                    setNumber = 1,
                    reps = 10,
                    weightKg = 80.0,
                ),
            ),
        )

        val unsynced = dao.getUnsyncedSessions()
        assertEquals(1, unsynced.size)
        assertEquals(800.0, unsynced[0].totalVolumeKg, 0.001)

        dao.markSynced(sessionId, "server-uuid", xpEarned = 25)
        val unsyncedAfter = dao.getUnsyncedSessions()
        assertTrue(unsyncedAfter.isEmpty())
    }
}
