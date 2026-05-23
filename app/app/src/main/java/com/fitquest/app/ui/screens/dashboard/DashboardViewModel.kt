package com.fitquest.app.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitquest.app.data.local.dao.DailyVolume
import com.fitquest.app.data.local.entity.CharacterEntity
import com.fitquest.app.data.local.entity.FoodEntity
import com.fitquest.app.data.repository.CharacterRepository
import com.fitquest.app.data.repository.FoodRepository
import com.fitquest.app.data.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val character: CharacterEntity? = null,
    val todayFood: List<FoodEntity> = emptyList(),
    val volume: List<DailyVolume> = emptyList(),
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val foodRepository: FoodRepository,
    private val workoutRepository: WorkoutRepository,
) : ViewModel() {

    val state: StateFlow<DashboardUiState> = combine(
        characterRepository.observe(),
        foodRepository.observeToday(),
        workoutRepository.observeVolume(7),
    ) { character, food, volume ->
        DashboardUiState(character, food, volume)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardUiState(),
    )

    init {
        viewModelScope.launch {
            characterRepository.refresh()
        }
    }

    fun refresh() {
        viewModelScope.launch { characterRepository.refresh() }
    }
}
