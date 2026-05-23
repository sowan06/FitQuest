package com.fitquest.app.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitquest.app.data.repository.AuthRepository
import com.fitquest.app.data.repository.CharacterRepository
import com.fitquest.app.data.repository.QuestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Loading : LoginUiState
    data object Success : LoginUiState
    data class Error(val message: String) : LoginUiState
}

enum class AuthMode { Login, Register }

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val characterRepository: CharacterRepository,
    private val questRepository: QuestRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    private val _mode = MutableStateFlow(AuthMode.Login)
    val mode: StateFlow<AuthMode> = _mode.asStateFlow()

    fun toggleMode() {
        _mode.value = if (_mode.value == AuthMode.Login) AuthMode.Register else AuthMode.Login
        _state.value = LoginUiState.Idle
    }

    fun submit(username: String, email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.value = LoginUiState.Error("ENTER EMAIL AND PASSWORD")
            return
        }
        if (_mode.value == AuthMode.Register && username.isBlank()) {
            _state.value = LoginUiState.Error("PICK A USERNAME")
            return
        }
        if (password.length < 8) {
            _state.value = LoginUiState.Error("PASSWORD MIN 8 CHARS")
            return
        }
        _state.value = LoginUiState.Loading
        viewModelScope.launch {
            val res = if (_mode.value == AuthMode.Login) {
                authRepository.login(email, password)
            } else {
                authRepository.register(username, email, password)
            }
            res.onSuccess {
                // pre-warm caches so Dashboard has data on first frame
                characterRepository.refresh()
                questRepository.refresh()
                _state.value = LoginUiState.Success
            }.onFailure {
                _state.value = LoginUiState.Error(translate(it))
            }
        }
    }

    private fun translate(t: Throwable): String {
        val msg = t.message ?: return "SOMETHING WENT WRONG"
        return when {
            msg.contains("401") -> "INVALID CREDENTIALS"
            msg.contains("409") -> "USER ALREADY EXISTS"
            msg.contains("400") -> "INVALID INPUT"
            else -> "OFFLINE OR SERVER ERROR"
        }
    }
}
