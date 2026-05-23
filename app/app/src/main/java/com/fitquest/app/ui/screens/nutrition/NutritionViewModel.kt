package com.fitquest.app.ui.screens.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitquest.app.data.local.entity.CharacterEntity
import com.fitquest.app.data.local.entity.FoodEntity
import com.fitquest.app.data.repository.CharacterRepository
import com.fitquest.app.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NutritionUiState(
    val character: CharacterEntity? = null,
    val today: List<FoodEntity> = emptyList(),
    val submitting: Boolean = false,
    val lastMessage: String? = null,
)

data class FoodFormState(
    val name: String = "",
    val calories: String = "",
    val proteinG: String = "",
    val carbsG: String = "",
    val fatG: String = "",
    val servingG: String = "",
    val mealType: String = "lunch",
)

@HiltViewModel
class NutritionViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val characterRepository: CharacterRepository,
) : ViewModel() {

    private val _form = MutableStateFlow(FoodFormState())
    val form: StateFlow<FoodFormState> = _form.asStateFlow()

    private val submitting = MutableStateFlow(false)
    private val message = MutableStateFlow<String?>(null)

    val state: StateFlow<NutritionUiState> = combine(
        characterRepository.observe(),
        foodRepository.observeToday(),
        submitting,
        message,
    ) { c, t, s, m -> NutritionUiState(c, t, s, m) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), NutritionUiState())

    fun update(transform: FoodFormState.() -> FoodFormState) {
        _form.value = transform(_form.value)
    }

    fun submit() {
        val f = _form.value
        val cal = f.calories.toDoubleOrNull() ?: return setError("CALORIES?")
        val pro = f.proteinG.toDoubleOrNull() ?: return setError("PROTEIN?")
        val car = f.carbsG.toDoubleOrNull() ?: return setError("CARBS?")
        val fat = f.fatG.toDoubleOrNull() ?: return setError("FATS?")
        val serving = f.servingG.toDoubleOrNull()
        if (f.name.isBlank()) return setError("NAME REQUIRED")

        submitting.value = true
        viewModelScope.launch {
            val res = foodRepository.logFood(
                FoodEntity(
                    foodName = f.name.trim(),
                    calories = cal,
                    proteinG = pro,
                    carbsG = car,
                    fatG = fat,
                    servingG = serving,
                    mealType = f.mealType,
                    loggedAt = System.currentTimeMillis(),
                    isSynced = false,
                ),
            )
            submitting.value = false
            res.onSuccess { srv ->
                message.value = if (srv != null) "+${srv.xpEarned} XP" else "SAVED LOCALLY"
                _form.value = FoodFormState(mealType = f.mealType)
            }.onFailure {
                message.value = "LOG FAILED"
            }
        }
    }

    private fun setError(msg: String) {
        message.value = msg
    }

    fun clearMessage() {
        message.value = null
    }
}
