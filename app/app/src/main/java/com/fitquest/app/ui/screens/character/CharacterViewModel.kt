package com.fitquest.app.ui.screens.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitquest.app.data.local.entity.CharacterEntity
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

data class CharacterScreenState(
    val character: CharacterEntity? = null,
    val achievements: List<AchievementResponse> = emptyList(),
)

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val questRepository: QuestRepository,
) : ViewModel() {

    private val achievements = MutableStateFlow<List<AchievementResponse>>(emptyList())

    val state: StateFlow<CharacterScreenState> = combine(
        characterRepository.observe(),
        achievements,
    ) { c, a -> CharacterScreenState(c, a) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CharacterScreenState())

    init {
        viewModelScope.launch {
            characterRepository.refresh()
            questRepository.listAchievements().onSuccess { achievements.value = it }
        }
    }
}
