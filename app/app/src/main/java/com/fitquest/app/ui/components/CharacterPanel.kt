package com.fitquest.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fitquest.app.data.local.entity.CharacterEntity
import com.fitquest.app.ui.theme.ColorAccent
import com.fitquest.app.ui.theme.ColorBorder
import com.fitquest.app.ui.theme.ColorMuted
import com.fitquest.app.ui.theme.ColorRed
import com.fitquest.app.ui.theme.ColorText

@Composable
fun CharacterPanel(
    character: CharacterEntity,
    leveledUp: Boolean = false,
    modifier: Modifier = Modifier,
    showStats: Boolean = true,
) {
    PixelCard(modifier = modifier) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AvatarSprite(stage = character.avatarStage)
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        text = character.name.uppercase(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = ColorText,
                    )
                    Text(
                        text = "LVL ${character.level} ${character.avatarLabel.uppercase()}",
                        style = MaterialTheme.typography.labelMedium,
                        color = ColorAccent,
                    )
                    Spacer(Modifier.height(8.dp))
                    XpBar(
                        current = character.xp,
                        max = character.xpToNext,
                        leveledUp = leveledUp,
                    )
                }
            }

            if (showStats) {
                Spacer(Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ColorBorder, RectangleShape)
                        .padding(8.dp),
                ) {
                    Column {
                        StatLabel("BASE STATS")
                        Spacer(Modifier.height(4.dp))
                        StatBar("STR", character.statStr)
                        StatBar("VIT", character.statVit)
                        StatBar("END", character.statEnd)
                        StatBar("WIS", character.statWis)
                        StatBar("CON", character.statCon)
                    }
                }

                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("STREAK", style = MaterialTheme.typography.labelSmall, color = ColorMuted)
                        Spacer(Modifier.width(8.dp))
                        StreakBadge(character.streakDays)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("HP", style = MaterialTheme.typography.labelSmall, color = ColorMuted)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "${character.totalWorkouts} runs",
                            style = MaterialTheme.typography.labelMedium,
                            color = ColorText,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatLabel(text: String) {
    Box(
        modifier = Modifier
            .background(ColorBorder)
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
        )
    }
}

@Composable
private fun StreakBadge(days: Int) {
    val color = if (days >= 7) ColorAccent else if (days >= 1) ColorRed else ColorMuted
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.18f))
            .border(1.dp, color, RectangleShape)
            .padding(horizontal = 8.dp, vertical = 2.dp),
    ) {
        Text(
            text = "🔥 $days DAYS",
            style = MaterialTheme.typography.labelMedium,
            color = color,
        )
    }
}
