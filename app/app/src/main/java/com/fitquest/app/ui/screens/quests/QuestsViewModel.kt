package com.fitquest.app.ui.screens.quests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitquest.app.data.local.entity.CharacterEntity
import com.fitquest.app.data.local.entity.QuestEntity
import com.fitquest.app.data.remote.dto.AchievementResponse
import com.fitquest.app.data.repository.CharacterRepository
import com.fitquest.app.data.repository.QuestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuestsUiState(
    val character: CharacterEntity? = null,
    val quests: List<QuestEntity> = emptyList(),
    val achievements: List<AchievementResponse> = emptyList(),
    val message: String? = null,
)

@HiltViewModel
class QuestsViewModel @Inject constructor(
    private val questRepository: QuestRepository,
    private val characterRepository: CharacterRepository,
) : ViewModel() {

    private val achievements = MutableStateFlow<List<AchievementResponse>>(emptyList())
    private val message = MutableStateFlow<String?>(null)

    val state: StateFlow<QuestsUiState> = combine(
        characterRepository.observe(),
        questRepository.observe(),
        achievements,
        message,
    ) { c, q, a, m -> QuestsUiState(c, q, a, m) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), QuestsUiState())

    init {
        viewModelScope.launch {
            questRepository.refresh()
            questRepository.listAchievements().onSuccess { achievements.value = it }
        }
    }

    fun claim(questId: String) {
        viewModelScope.launch {
            questRepository.claim(questId).onSuccess { xp ->
                message.value = "+$xp XP CLAIMED"
                questRepository.listAchievements().onSuccess { achievements.value = it }
            }.onFailure {
                message.value = "CLAIM FAILED"
            }
        }
    }

    fun refresh() {
        viewModelScope.launch { questRepository.refresh() }
    }

    fun clearMessage() { message.value = null }
}
