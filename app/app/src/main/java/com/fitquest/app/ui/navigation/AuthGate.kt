package com.fitquest.app.ui.navigation

import androidx.lifecycle.ViewModel
import com.fitquest.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/** Tiny VM that exposes the JWT token as a StateFlow for nav routing. */
@HiltViewModel
class AuthGate @Inject constructor(
    repo: AuthRepository,
) : ViewModel() {
    val token: StateFlow<String?> = repo.authState
}
