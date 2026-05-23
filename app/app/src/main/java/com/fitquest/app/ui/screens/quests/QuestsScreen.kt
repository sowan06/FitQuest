package com.fitquest.app.ui.screens.quests

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fitquest.app.ui.components.AppHeader
import com.fitquest.app.ui.components.PixelButton
import com.fitquest.app.ui.components.PixelButtonVariant
import com.fitquest.app.ui.components.PixelCard
import com.fitquest.app.ui.components.QuestCard
import com.fitquest.app.ui.theme.ColorAccent
import com.fitquest.app.ui.theme.ColorBgDeep
import com.fitquest.app.ui.theme.ColorBorder
import com.fitquest.app.ui.theme.ColorGreen
import com.fitquest.app.ui.theme.ColorMuted
import com.fitquest.app.ui.theme.ColorSurface
import com.fitquest.app.ui.theme.ColorText

@Composable
fun QuestsScreen(viewModel: QuestsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val daily = state.quests.filter { it.questType == "daily" }
    val weekly = state.quests.filter { it.questType == "weekly" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBgDeep),
    ) {
        AppHeader()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("ACTIVE QUESTS", style = MaterialTheme.typography.headlineSmall, color = ColorText)
            Text("DAILY", style = MaterialTheme.typography.titleSmall, color = ColorMuted)
            if (daily.isEmpty()) {
                EmptyState("LOADING DAILIES…")
            } else {
                daily.forEach { q ->
                    QuestCard(quest = q, onClaim = { viewModel.claim(q.questId) })
                }
            }

            Spacer(Modifier.height(8.dp))
            Text("WEEKLY", style = MaterialTheme.typography.titleSmall, color = ColorMuted)
            if (weekly.isEmpty()) {
                EmptyState("NO WEEKLIES YET.")
            } else {
                weekly.forEach { q ->
                    QuestCard(quest = q, onClaim = { viewModel.claim(q.questId) })
                }
            }

            state.message?.let { msg ->
                PixelCard(border = ColorAccent) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(msg, color = ColorAccent, style = MaterialTheme.typography.labelMedium, modifier = Modifier.weight(1f))
                        PixelButton(
                            text = "OK",
                            onClick = { viewModel.clearMessage() },
                            variant = PixelButtonVariant.Ghost,
                            fillWidth = false,
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            Text("HALL OF FAME", style = MaterialTheme.typography.headlineSmall, color = ColorText)
            if (state.achievements.isEmpty()) {
                EmptyState("KEEP QUESTING TO UNLOCK ACHIEVEMENTS.")
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    state.achievements.take(3).forEach { a ->
                        AchievementCircle(
                            label = a.title,
                            unlocked = a.unlockedAt != null,
                        )
                    }
                }
            }

            PixelButton(
                text = "REFRESH QUESTS",
                onClick = { viewModel.refresh() },
                variant = PixelButtonVariant.Ghost,
            )
            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun AchievementCircle(label: String, unlocked: Boolean) {
    val color = if (unlocked) ColorGreen else ColorMuted
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(if (unlocked) ColorSurface else ColorBgDeep)
                .border(2.dp, color, RectangleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                if (unlocked) "★" else "🔒",
                color = color,
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = if (unlocked) label.take(10).uppercase() else "???",
            color = if (unlocked) ColorText else ColorMuted,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Composable
private fun EmptyState(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .border(2.dp, ColorBorder, RectangleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, color = ColorMuted, style = MaterialTheme.typography.bodySmall)
    }
}
