package com.fitquest.app.ui.screens.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitquest.app.data.local.dao.WorkoutSessionWithSets
import com.fitquest.app.data.local.entity.CharacterEntity
import com.fitquest.app.data.local.entity.WorkoutSetEntity
import com.fitquest.app.data.repository.CharacterRepository
import com.fitquest.app.data.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExerciseRow(
    val id: Long = System.nanoTime(),
    val exerciseName: String = "",
    val tag: String = "CHEST",
    val sets: List<SetRow> = listOf(SetRow(setNumber = 1)),
)

data class SetRow(
    val id: Long = System.nanoTime(),
    val setNumber: Int,
    val weightKg: String = "",
    val reps: String = "",
    val done: Boolean = false,
)

data class WorkoutScreenState(
    val character: CharacterEntity? = null,
    val exercises: List<ExerciseRow> = listOf(ExerciseRow(exerciseName = "Bench Press")),
    val history: List<WorkoutSessionWithSets> = emptyList(),
    val submitting: Boolean = false,
    val lastResult: SubmitResult? = null,
)

sealed interface SubmitResult {
    data class Logged(val xp: Int, val leveledUp: Boolean) : SubmitResult
    data class Offline(val message: String) : SubmitResult
    data class Error(val message: String) : SubmitResult
}

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val characterRepository: CharacterRepository,
) : ViewModel() {

    private val draft = MutableStateFlow(
        listOf(ExerciseRow(exerciseName = "Bench Press")),
    )
    private val submitting = MutableStateFlow(false)
    private val result = MutableStateFlow<SubmitResult?>(null)

    val state: StateFlow<WorkoutScreenState> = combine(
        characterRepository.observe(),
        workoutRepository.observeSessions(),
        draft,
        submitting,
        result,
    ) { char, history, ex, sub, res ->
        WorkoutScreenState(
            character = char,
            exercises = ex,
            history = history,
            submitting = sub,
            lastResult = res,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), WorkoutScreenState())

    fun setExerciseName(rowId: Long, name: String) {
        draft.value = draft.value.map { if (it.id == rowId) it.copy(exerciseName = name) else it }
    }

    fun setTag(rowId: Long, tag: String) {
        draft.value = draft.value.map { if (it.id == rowId) it.copy(tag = tag) else it }
    }

    fun updateSet(rowId: Long, setId: Long, weight: String? = null, reps: String? = null, done: Boolean? = null) {
        draft.value = draft.value.map { ex ->
            if (ex.id != rowId) ex else ex.copy(
                sets = ex.sets.map { s ->
                    if (s.id != setId) s else s.copy(
                        weightKg = weight ?: s.weightKg,
                        reps = reps ?: s.reps,
                        done = done ?: s.done,
                    )
                },
            )
        }
    }

    fun addSet(rowId: Long) {
        draft.value = draft.value.map { ex ->
            if (ex.id != rowId) ex else ex.copy(
                sets = ex.sets + SetRow(setNumber = ex.sets.size + 1),
            )
        }
    }

    fun addExercise() {
        draft.value = draft.value + ExerciseRow()
    }

    fun removeExercise(rowId: Long) {
        if (draft.value.size <= 1) return
        draft.value = draft.value.filter { it.id != rowId }
    }

    fun complete() {
        val rows = draft.value
        val flatSets = rows.flatMap { ex ->
            ex.sets
                .filter { it.done || (it.weightKg.isNotBlank() && it.reps.isNotBlank()) }
                .mapNotNull { s ->
                    val w = s.weightKg.toDoubleOrNull() ?: return@mapNotNull null
                    val r = s.reps.toIntOrNull() ?: return@mapNotNull null
                    if (w <= 0 || r <= 0 || ex.exerciseName.isBlank()) null
                    else WorkoutSetEntity(
                        localSessionId = 0L,
                        exerciseName = ex.exerciseName,
                        setNumber = 0,
                        reps = r,
                        weightKg = w,
                    )
                }
        }
        if (flatSets.isEmpty()) {
            result.value = SubmitResult.Error("ADD AT LEAST ONE COMPLETED SET")
            return
        }
        submitting.value = true
        viewModelScope.launch {
            val res = workoutRepository.logWorkout(flatSets, notes = null)
            submitting.value = false
            res.onSuccess { summary ->
                if (summary != null) {
                    result.value = SubmitResult.Logged(summary.xpEarned, summary.leveledUp)
                } else {
                    result.value = SubmitResult.Offline("SAVED LOCALLY — WILL SYNC WHEN ONLINE")
                }
                draft.value = listOf(ExerciseRow(exerciseName = "Bench Press"))
            }.onFailure {
                result.value = SubmitResult.Error("LOG FAILED")
            }
        }
    }

    fun clearResult() {
        result.value = null
    }
}
