package com.fitquest.app.ui.screens.nutrition

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
import com.fitquest.app.ui.components.MacroPieChart
import com.fitquest.app.ui.components.PixelButton
import com.fitquest.app.ui.components.PixelButtonVariant
import com.fitquest.app.ui.components.PixelCard
import com.fitquest.app.ui.components.PixelTextField
import com.fitquest.app.ui.theme.ColorAccent
import com.fitquest.app.ui.theme.ColorBgDeep
import com.fitquest.app.ui.theme.ColorBorder
import com.fitquest.app.ui.theme.ColorGreen
import com.fitquest.app.ui.theme.ColorMuted
import com.fitquest.app.ui.theme.ColorRedSoft
import com.fitquest.app.ui.theme.ColorText

@Composable
fun NutritionScreen(viewModel: NutritionViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val form by viewModel.form.collectAsStateWithLifecycle()

    val totalCal = state.today.sumOf { it.calories }
    val totalPro = state.today.sumOf { it.proteinG }
    val totalCarb = state.today.sumOf { it.carbsG }
    val totalFat = state.today.sumOf { it.fatG }

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
            Text(
                "THE ALCHEMIST'S KITCHEN",
                style = MaterialTheme.typography.headlineMedium,
                color = ColorText,
            )
            Text(
                "Log your daily consumables to restore HP and gain buffs.",
                style = MaterialTheme.typography.bodySmall,
                color = ColorMuted,
            )

            // form
            PixelCard {
                Column {
                    PixelButton(
                        text = "+ NEW CONSUMABLE",
                        onClick = { /* form is always visible inline */ },
                        variant = PixelButtonVariant.Success,
                    )
                    Spacer(Modifier.height(12.dp))
                    PixelTextField(
                        value = form.name,
                        onValueChange = { v -> viewModel.update { copy(name = v) } },
                        label = "POTION NAME",
                        placeholder = "e.g. Minor Health Potion",
                    )
                    Spacer(Modifier.height(12.dp))
                    Row {
                        Box(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                            PixelTextField(
                                value = form.calories,
                                onValueChange = { v -> viewModel.update { copy(calories = v) } },
                                label = "CALORIES (ENERGY)",
                                placeholder = "kcal",
                                keyboardType = KeyboardType.Number,
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            PixelTextField(
                                value = form.proteinG,
                                onValueChange = { v -> viewModel.update { copy(proteinG = v) } },
                                label = "PROTEIN (STR)",
                                placeholder = "g",
                                keyboardType = KeyboardType.Number,
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Row {
                        Box(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                            PixelTextField(
                                value = form.carbsG,
                                onValueChange = { v -> viewModel.update { copy(carbsG = v) } },
                                label = "CARBS (AGI)",
                                placeholder = "g",
                                keyboardType = KeyboardType.Number,
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            PixelTextField(
                                value = form.fatG,
                                onValueChange = { v -> viewModel.update { copy(fatG = v) } },
                                label = "FATS (END)",
                                placeholder = "g",
                                keyboardType = KeyboardType.Number,
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    MealTypePicker(form.mealType) { v ->
                        viewModel.update { copy(mealType = v) }
                    }
                    Spacer(Modifier.height(16.dp))
                    PixelButton(
                        text = if (state.submitting) "BREWING…" else "BREW POTION",
                        onClick = { viewModel.submit() },
                        enabled = !state.submitting,
                    )
                }
            }

            state.lastMessage?.let { msg ->
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

            // daily quest progress
            state.character?.let { char ->
                PixelCard {
                    Column {
                        Text("DAILY QUEST PROGRESS", style = MaterialTheme.typography.titleSmall, color = ColorText)
                        Spacer(Modifier.height(8.dp))
                        QuestRow(
                            label = "ENERGY (CALORIES)",
                            value = "${totalCal.toInt()} / ${char.dailyCalorieGoal}",
                            progress = totalCal.toFloat() / char.dailyCalorieGoal,
                            color = ColorAccent,
                        )
                        Spacer(Modifier.height(8.dp))
                        QuestRow(
                            label = "STR GAINS (PROTEIN)",
                            value = "${totalPro.toInt()}g / ${char.dailyProteinGoalG}g",
                            progress = totalPro.toFloat() / char.dailyProteinGoalG,
                            color = ColorGreen,
                        )
                    }
                }
            }

            // pie chart
            PixelCard {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text("MACRO ALCHEMY", style = MaterialTheme.typography.titleSmall, color = ColorText)
                    Spacer(Modifier.height(8.dp))
                    MacroPieChart(
                        proteinG = totalPro.toFloat(),
                        carbsG = totalCarb.toFloat(),
                        fatG = totalFat.toFloat(),
                        calories = totalCal.toFloat(),
                    )
                }
            }

            // inventory
            Text("DAILY INVENTORY", style = MaterialTheme.typography.titleSmall, color = ColorText)
            if (state.today.isEmpty()) {
                Text("EMPTY KNAPSACK. BREW SOMETHING!", style = MaterialTheme.typography.bodySmall, color = ColorMuted)
            }
            state.today.forEach { entry ->
                PixelCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(ColorRedSoft.copy(alpha = 0.2f))
                                .border(2.dp, ColorBorder, RectangleShape),
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                entry.foodName.uppercase(),
                                style = MaterialTheme.typography.titleSmall,
                                color = ColorText,
                            )
                            Row {
                                Box(
                                    modifier = Modifier
                                        .background(ColorAccent)
                                        .padding(horizontal = 4.dp, vertical = 2.dp),
                                ) {
                                    Text(
                                        "+${entry.proteinG.toInt()} STR",
                                        color = Color.Black,
                                        style = MaterialTheme.typography.labelSmall,
                                    )
                                }
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "${entry.calories.toInt()} kcal",
                                    color = ColorMuted,
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun MealTypePicker(selected: String, onSelect: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        listOf("breakfast", "lunch", "dinner", "snack").forEach { meal ->
            val sel = meal == selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(if (sel) ColorAccent else ColorBgDeep)
                    .border(2.dp, ColorBorder, RectangleShape)
                    .clickable { onSelect(meal) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    meal.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (sel) Color.Black else ColorText,
                )
            }
        }
    }
}

@Composable
private fun QuestRow(label: String, value: String, progress: Float, color: Color) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = ColorMuted, modifier = Modifier.weight(1f))
        Text(value, style = MaterialTheme.typography.labelMedium, color = ColorText)
    }
    Spacer(Modifier.height(4.dp))
    ChunkedBar(
        progress = progress,
        color = color,
        segmentCount = 12,
        modifier = Modifier.fillMaxWidth().height(14.dp),
    )
}
