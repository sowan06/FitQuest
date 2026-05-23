package com.fitquest.app.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fitquest.app.ui.components.AvatarSprite
import com.fitquest.app.ui.components.PixelButton
import com.fitquest.app.ui.components.PixelButtonVariant
import com.fitquest.app.ui.components.PixelCard
import com.fitquest.app.ui.components.PixelTextField
import com.fitquest.app.ui.theme.ColorAccent
import com.fitquest.app.ui.theme.ColorBgDeep
import com.fitquest.app.ui.theme.ColorMuted
import com.fitquest.app.ui.theme.ColorRed

@Composable
fun LoginScreen(
    onLoggedIn: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val mode by viewModel.mode.collectAsStateWithLifecycle()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(state) {
        if (state is LoginUiState.Success) onLoggedIn()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBgDeep)
            .padding(24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(Modifier.height(32.dp))
            AvatarSprite(stage = if (mode == AuthMode.Register) 0 else 2)
            Spacer(Modifier.height(16.dp))
            Text(
                text = "FITQUEST",
                style = MaterialTheme.typography.displayLarge,
                color = ColorAccent,
            )
            Text(
                text = "YOUR QUEST AWAITS",
                style = MaterialTheme.typography.labelMedium,
                color = ColorMuted,
            )
            Spacer(Modifier.height(24.dp))

            PixelCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    if (mode == AuthMode.Register) {
                        PixelTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = "Username",
                            placeholder = "hero_42",
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                    PixelTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        placeholder = "you@example.com",
                        keyboardType = KeyboardType.Email,
                    )
                    Spacer(Modifier.height(12.dp))
                    PixelTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        placeholder = "••••••••",
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                    )
                    if (state is LoginUiState.Error) {
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = (state as LoginUiState.Error).message,
                            color = ColorRed,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    PixelButton(
                        text = when {
                            state is LoginUiState.Loading -> "LOADING…"
                            mode == AuthMode.Register -> "CREATE HERO"
                            else -> "LOGIN"
                        },
                        onClick = { viewModel.submit(username, email, password) },
                        enabled = state !is LoginUiState.Loading,
                    )
                    Spacer(Modifier.height(8.dp))
                    PixelButton(
                        text = if (mode == AuthMode.Register)
                            "RETURNING HERO? LOGIN"
                        else
                            "NEW HERE? CREATE HERO",
                        onClick = { viewModel.toggleMode() },
                        variant = PixelButtonVariant.Ghost,
                    )
                }
            }
        }
    }
}
