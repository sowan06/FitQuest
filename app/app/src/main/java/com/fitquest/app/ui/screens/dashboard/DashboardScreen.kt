package com.fitquest.app.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fitquest.app.data.local.entity.CharacterEntity
import com.fitquest.app.ui.components.AppHeader
import com.fitquest.app.ui.components.CharacterPanel
import com.fitquest.app.ui.components.PixelButton
import com.fitquest.app.ui.components.PixelCard
import com.fitquest.app.ui.components.ProgressRing
import com.fitquest.app.ui.components.StatsRadarChart
import com.fitquest.app.ui.components.VolumeBarChart
import com.fitquest.app.ui.theme.ColorAccent
import com.fitquest.app.ui.theme.ColorBgDeep
import com.fitquest.app.ui.theme.ColorGreen
import com.fitquest.app.ui.theme.ColorMuted
import com.fitquest.app.ui.theme.ColorRedSoft
import com.fitquest.app.ui.theme.ColorText

@Composable
fun DashboardScreen(
    onLogTap: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val char = state.character

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
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (char == null) {
                LoadingPlaceholder()
            } else {
                CharacterPanel(character = char)
                TodayRingsRow(char, state.todayFood)
                StreakCallout(char, onLogTap)
                PixelCard {
                    Column {
                        Text(
                            "STATS RADAR",
                            style = MaterialTheme.typography.titleMedium,
                            color = ColorAccent,
                        )
                        Spacer(Modifier.height(8.dp))
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            StatsRadarChart(
                                str = char.statStr,
                                vit = char.statVit,
                                end = char.statEnd,
                                wis = char.statWis,
                                con = char.statCon,
                            )
                        }
                    }
                }
                PixelCard {
                    Column {
                        Text(
                            "WEEKLY VOLUME (KG)",
                            style = MaterialTheme.typography.titleMedium,
                            color = ColorAccent,
                        )
                        Spacer(Modifier.height(8.dp))
                        VolumeBarChart(state.volume)
                    }
                }
                Spacer(Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun TodayRingsRow(
    char: CharacterEntity,
    food: List<com.fitquest.app.data.local.entity.FoodEntity>,
) {
    val totalCal = food.sumOf { it.calories }.toFloat()
    val totalProtein = food.sumOf { it.proteinG }.toFloat()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        PixelCard(modifier = Modifier.weight(1f)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text("MANA (KCAL)", style = MaterialTheme.typography.labelSmall, color = ColorMuted)
                Spacer(Modifier.height(4.dp))
                ProgressRing(
                    value = totalCal,
                    max = char.dailyCalorieGoal.toFloat(),
                    centerLabel = formatK(totalCal),
                    color = ColorAccent,
                    bottomLabel = "/ ${char.dailyCalorieGoal}",
                )
            }
        }
        PixelCard(modifier = Modifier.weight(1f)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text("PWR (PRO)", style = MaterialTheme.typography.labelSmall, color = ColorMuted)
                Spacer(Modifier.height(4.dp))
                ProgressRing(
                    value = totalProtein,
                    max = char.dailyProteinGoalG.toFloat(),
                    centerLabel = "${totalProtein.toInt()}g",
                    color = ColorRedSoft,
                    bottomLabel = "/ ${char.dailyProteinGoalG}g",
                )
            }
        }
    }
}

@Composable
private fun StreakCallout(char: CharacterEntity, onLogTap: () -> Unit) {
    PixelCard(background = ColorGreen) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text("CURRENT STREAK", style = MaterialTheme.typography.titleMedium, color = ColorText)
            Spacer(Modifier.height(8.dp))
            Text(
                "🔥 ${char.streakDays} DAYS 🔥",
                style = MaterialTheme.typography.headlineLarge,
                color = ColorText,
            )
            Spacer(Modifier.height(12.dp))
            PixelButton(
                text = "LOG TODAY'S QUEST",
                onClick = onLogTap,
            )
        }
    }
}

@Composable
private fun LoadingPlaceholder() {
    PixelCard {
        Box(modifier = Modifier.fillMaxWidth().height(120.dp), contentAlignment = Alignment.Center) {
            Text("LOADING…", style = MaterialTheme.typography.titleMedium, color = ColorMuted)
        }
    }
}

private fun formatK(value: Float): String {
    if (value >= 1000f) return "${"%.1f".format(value / 1000f)}K"
    return value.toInt().toString()
}
