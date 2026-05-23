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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fitquest.app.data.local.entity.QuestEntity
import com.fitquest.app.ui.theme.ColorAccent
import com.fitquest.app.ui.theme.ColorBorder
import com.fitquest.app.ui.theme.ColorGreen
import com.fitquest.app.ui.theme.ColorMuted
import com.fitquest.app.ui.theme.ColorRedSoft
import com.fitquest.app.ui.theme.ColorText

@Composable
fun QuestCard(
    quest: QuestEntity,
    onClaim: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tagBg: Color
    val tagText: String
    when (quest.questType) {
        "weekly" -> { tagBg = ColorRedSoft; tagText = "WEEKLY" }
        else -> { tagBg = ColorGreen; tagText = "DAILY" }
    }
    val highlight = quest.status == "completed"
    PixelCard(
        modifier = modifier,
        border = if (highlight) ColorAccent else ColorBorder,
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = quest.title.uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = ColorText,
                    )
                    Text(
                        text = quest.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = ColorMuted,
                    )
                }
                Spacer(Modifier.height(0.dp))
                Box(
                    modifier = Modifier
                        .background(tagBg)
                        .border(1.dp, ColorBorder, RectangleShape)
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                ) {
                    Text(
                        text = tagText,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Black,
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("PROGRESS", style = MaterialTheme.typography.labelSmall, color = ColorMuted)
                Text(
                    text = "${quest.progress}/${quest.target}",
                    style = MaterialTheme.typography.labelMedium,
                    color = ColorText,
                )
            }
            Spacer(Modifier.height(4.dp))
            ChunkedBar(
                progress = quest.progress.toFloat() / quest.target.coerceAtLeast(1),
                color = if (highlight) ColorAccent else ColorGreen,
                segmentCount = quest.target.coerceIn(3, 12),
                modifier = Modifier.fillMaxWidth().height(14.dp),
            )
            Spacer(Modifier.height(10.dp))
            when (quest.status) {
                "completed" -> PixelButton(
                    text = "CLAIM ${quest.xpReward} XP",
                    onClick = onClaim,
                    variant = PixelButtonVariant.Primary,
                )
                "claimed" -> PixelButton(
                    text = "CLAIMED",
                    onClick = {},
                    enabled = false,
                    variant = PixelButtonVariant.Ghost,
                )
                else -> PixelButton(
                    text = "IN PROGRESS",
                    onClick = {},
                    enabled = false,
                    variant = PixelButtonVariant.Ghost,
                )
            }
        }
    }
}
