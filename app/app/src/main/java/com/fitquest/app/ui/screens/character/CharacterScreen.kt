package com.fitquest.app.ui.screens.character

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fitquest.app.data.local.entity.CharacterEntity
import com.fitquest.app.ui.components.AppHeader
import com.fitquest.app.ui.components.AvatarSprite
import com.fitquest.app.ui.components.CharacterPanel
import com.fitquest.app.ui.components.PixelCard
import com.fitquest.app.ui.theme.ColorAccent
import com.fitquest.app.ui.theme.ColorBgDeep
import com.fitquest.app.ui.theme.ColorBorder
import com.fitquest.app.ui.theme.ColorGreen
import com.fitquest.app.ui.theme.ColorMuted
import com.fitquest.app.ui.theme.ColorSurface
import com.fitquest.app.ui.theme.ColorText

@Composable
fun CharacterScreen(viewModel: CharacterViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

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
            val char = state.character
            if (char != null) {
                CharacterPanel(character = char)
                EvolutionTimeline(char)
                HallOfFame(state.achievements)
            } else {
                PixelCard {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "LOADING HERO…",
                            color = ColorMuted,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
            }
            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun EvolutionTimeline(char: CharacterEntity) {
    PixelCard {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("EVOLUTION PATH", style = MaterialTheme.typography.titleMedium, color = ColorAccent)
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                listOf("ROOKIE" to 0, "FIGHTER" to 1, "WARRIOR" to 2, "LEGEND" to 3).forEach { (label, stage) ->
                    EvolutionStep(
                        label = label,
                        stage = stage,
                        active = stage == char.avatarStage,
                        unlocked = char.avatarStage >= stage,
                    )
                }
            }
        }
    }
}

@Composable
private fun EvolutionStep(label: String, stage: Int, active: Boolean, unlocked: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(if (active) ColorAccent.copy(alpha = 0.2f) else ColorSurface)
                .border(if (active) 3.dp else 2.dp, if (active) ColorAccent else ColorBorder, RectangleShape),
            contentAlignment = Alignment.Center,
        ) {
            if (unlocked) {
                AvatarSprite(stage = stage, size = 48.dp)
            } else {
                Text("?", color = ColorMuted, style = MaterialTheme.typography.headlineSmall)
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            label,
            color = if (active) ColorAccent else if (unlocked) ColorText else ColorMuted,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Composable
private fun HallOfFame(achievements: List<com.fitquest.app.data.remote.dto.AchievementResponse>) {
    PixelCard {
        Column {
            Text("HALL OF FAME", style = MaterialTheme.typography.titleMedium, color = ColorAccent)
            Spacer(Modifier.height(12.dp))
            if (achievements.isEmpty()) {
                Text(
                    "NO ACHIEVEMENTS YET — KEEP QUESTING!",
                    color = ColorMuted,
                    style = MaterialTheme.typography.bodySmall,
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.height(220.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(achievements, key = { it.id }) { a ->
                        AchievementBadge(
                            title = a.title,
                            unlocked = a.unlockedAt != null,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AchievementBadge(title: String, unlocked: Boolean) {
    val color = if (unlocked) ColorGreen else ColorMuted
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(72.dp)
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
            text = if (unlocked) title.uppercase() else "???",
            color = if (unlocked) ColorText else ColorMuted,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}
