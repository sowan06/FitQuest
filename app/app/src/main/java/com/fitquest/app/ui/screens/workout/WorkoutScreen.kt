package com.fitquest.app.ui.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fitquest.app.ui.components.AppHeader
import com.fitquest.app.ui.components.ChunkedBar
import com.fitquest.app.ui.components.PixelButton
import com.fitquest.app.ui.components.PixelButtonVariant
import com.fitquest.app.ui.components.PixelCard
import com.fitquest.app.ui.components.PixelTextField
import com.fitquest.app.ui.theme.ColorAccent
import com.fitquest.app.ui.theme.ColorBgDeep
import com.fitquest.app.ui.theme.ColorBorder
import com.fitquest.app.ui.theme.ColorGreen
import com.fitquest.app.ui.theme.ColorGreenSoft
import com.fitquest.app.ui.theme.ColorMuted
import com.fitquest.app.ui.theme.ColorRed
import com.fitquest.app.ui.theme.ColorText

@Composable
fun WorkoutScreen(viewModel: WorkoutViewModel = hiltViewModel()) {
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CurrentLootCard(state)

            state.exercises.forEach { ex ->
                ExerciseCard(
                    exercise = ex,
                    onNameChange = { viewModel.setExerciseName(ex.id, it) },
                    onUpdateSet = { setId, w, r, d -> viewModel.updateSet(ex.id, setId, w, r, d) },
                    onAddSet = { viewModel.addSet(ex.id) },
                    onRemove = { viewModel.removeExercise(ex.id) },
                )
            }

            PixelButton(
                text = "+ ADD EXERCISE",
                onClick = { viewModel.addExercise() },
                variant = PixelButtonVariant.Ghost,
            )

            PixelButton(
                text = if (state.submitting) "SAVING…" else "⚔ COMPLETE SESSION",
                onClick = { viewModel.complete() },
                variant = PixelButtonVariant.Success,
                enabled = !state.submitting,
            )

            state.lastResult?.let { res ->
                ResultBanner(res, onDismiss = { viewModel.clearResult() })
            }

            Text(
                text = "RECENT QUESTS",
                style = MaterialTheme.typography.titleMedium,
                color = ColorAccent,
                modifier = Modifier.padding(top = 8.dp),
            )
            if (state.history.isEmpty()) {
                Text(
                    text = "NO SESSIONS YET. CLAIM YOUR FIRST SWEAT!",
                    style = MaterialTheme.typography.bodySmall,
                    color = ColorMuted,
                )
            }
            state.history.take(5).forEach { sws ->
                PixelCard {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                "VOL ${sws.session.totalVolumeKg.toInt()} KG",
                                style = MaterialTheme.typography.titleMedium,
                                color = ColorText,
                            )
                            Text(
                                "+${sws.session.xpEarned} XP",
                                style = MaterialTheme.typography.labelMedium,
                                color = ColorAccent,
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                        sws.sets.take(4).forEach {
                            Text(
                                "${it.exerciseName} — ${it.reps}x${it.weightKg.toInt()}kg",
                                style = MaterialTheme.typography.bodySmall,
                                color = ColorMuted,
                            )
                        }
                        if (!sws.session.isSynced) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "PENDING SYNC",
                                style = MaterialTheme.typography.labelSmall,
                                color = ColorRed,
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun CurrentLootCard(state: WorkoutScreenState) {
    val char = state.character
    val estVolume = state.exercises.sumOf { ex ->
        ex.sets.sumOf { s ->
            (s.weightKg.toDoubleOrNull() ?: 0.0) * (s.reps.toIntOrNull() ?: 0)
        }
    }
    val estXp = (20 + (estVolume / 1000).toInt())

    PixelCard {
        Column {
            Text("CURRENT LOOT", style = MaterialTheme.typography.titleMedium, color = ColorAccent)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                StatBox("EST. XP", "+$estXp", ColorGreenSoft, modifier = Modifier.weight(1f))
                val mult = "x" + "%.1f".format(1.0 + ((char?.streakDays ?: 0) * 0.05))
                StatBox("MULTIPLIER", mult, ColorAccent, modifier = Modifier.weight(1f))
            }
            if (char != null) {
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        "LEVEL ${char.level} ${char.avatarLabel.uppercase()}",
                        style = MaterialTheme.typography.labelMedium,
                        color = ColorText,
                    )
                    Text(
                        "${char.xp} / ${char.xpToNext} XP",
                        style = MaterialTheme.typography.labelMedium,
                        color = ColorGreenSoft,
                    )
                }
                Spacer(Modifier.height(4.dp))
                ChunkedBar(
                    progress = if (char.xpToNext > 0) char.xp.toFloat() / char.xpToNext else 0f,
                    color = ColorGreen,
                    segmentCount = 12,
                    modifier = Modifier.fillMaxWidth().height(14.dp),
                )
            }
        }
    }
}

@Composable
private fun StatBox(label: String, value: String, valueColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .border(2.dp, ColorBorder, RectangleShape)
            .padding(12.dp),
    ) {
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = ColorMuted)
            Text(value, style = MaterialTheme.typography.titleLarge, color = valueColor)
        }
    }
}

@Composable
private fun ExerciseCard(
    exercise: ExerciseRow,
    onNameChange: (String) -> Unit,
    onUpdateSet: (setId: Long, weight: String?, reps: String?, done: Boolean?) -> Unit,
    onAddSet: () -> Unit,
    onRemove: () -> Unit,
) {
    PixelCard {
        Column {
            // Header ribbon — gold strip with the exercise name (editable)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ColorAccent)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
            ) {
                PixelTextField(
                    value = exercise.exerciseName,
                    onValueChange = onNameChange,
                    placeholder = "EXERCISE NAME",
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
            ) {
                Text("SET", style = MaterialTheme.typography.labelSmall, color = ColorMuted, modifier = Modifier.width(28.dp))
                Spacer(Modifier.width(8.dp))
                Text("KG", style = MaterialTheme.typography.labelSmall, color = ColorMuted, modifier = Modifier.weight(1f))
                Text("REPS", style = MaterialTheme.typography.labelSmall, color = ColorMuted, modifier = Modifier.weight(1f))
                Text("DONE", style = MaterialTheme.typography.labelSmall, color = ColorMuted, modifier = Modifier.width(48.dp))
            }
            Spacer(Modifier.height(4.dp))
            exercise.sets.forEach { s ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        s.setNumber.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = ColorAccent,
                        modifier = Modifier.width(28.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                        PixelTextField(
                            value = s.weightKg,
                            onValueChange = { onUpdateSet(s.id, it, null, null) },
                            keyboardType = KeyboardType.Number,
                        )
                    }
                    Box(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                        PixelTextField(
                            value = s.reps,
                            onValueChange = { onUpdateSet(s.id, null, it, null) },
                            keyboardType = KeyboardType.Number,
                        )
                    }
                    PixelCheckbox(
                        checked = s.done,
                        onChange = { onUpdateSet(s.id, null, null, it) },
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            PixelButton(
                text = "+ ADD SET",
                onClick = onAddSet,
                variant = PixelButtonVariant.Ghost,
            )
            Spacer(Modifier.height(4.dp))
            PixelButton(
                text = "REMOVE EXERCISE",
                onClick = onRemove,
                variant = PixelButtonVariant.Ghost,
            )
        }
    }
}

@Composable
private fun PixelCheckbox(checked: Boolean, onChange: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp) // 48dp tap target
            .padding(12.dp)
            .background(ColorBgDeep)
            .border(2.dp, ColorBorder, RectangleShape)
            .clickable { onChange(!checked) },
        contentAlignment = Alignment.Center,
    ) {
        if (checked) {
            Box(modifier = Modifier.size(14.dp).background(ColorGreen))
        }
    }
}

@Composable
private fun ResultBanner(res: SubmitResult, onDismiss: () -> Unit) {
    val (msg, color) = when (res) {
        is SubmitResult.Logged -> Pair(
            "SESSION LOGGED — +${res.xp} XP" + (if (res.leveledUp) " — LEVEL UP!" else ""),
            ColorGreenSoft,
        )
        is SubmitResult.Offline -> Pair(res.message, ColorAccent)
        is SubmitResult.Error -> Pair(res.message, ColorRed)
    }
    PixelCard(border = color) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(msg, style = MaterialTheme.typography.labelMedium, color = color, modifier = Modifier.weight(1f))
            PixelButton(
                text = "OK",
                onClick = onDismiss,
                variant = PixelButtonVariant.Ghost,
                fillWidth = false,
            )
        }
    }
}
