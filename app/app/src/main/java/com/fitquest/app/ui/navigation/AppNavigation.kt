package com.fitquest.app.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.fitquest.app.ui.screens.character.CharacterScreen
import com.fitquest.app.ui.screens.dashboard.DashboardScreen
import com.fitquest.app.ui.screens.login.LoginScreen
import com.fitquest.app.ui.screens.nutrition.NutritionScreen
import com.fitquest.app.ui.screens.quests.QuestsScreen
import com.fitquest.app.ui.screens.workout.WorkoutScreen
import com.fitquest.app.ui.theme.ColorAccent
import com.fitquest.app.ui.theme.ColorBgDeep
import com.fitquest.app.ui.theme.ColorBorder
import com.fitquest.app.ui.theme.ColorMuted
import com.fitquest.app.ui.theme.ColorSurface
import com.fitquest.app.ui.theme.ColorText

object Routes {
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
    const val WORKOUT = "workout"
    const val NUTRITION = "nutrition"
    const val CHARACTER = "character"
    const val QUESTS = "quests"
}

private data class NavItem(val route: String, val label: String, val icon: ImageVector)

private val navItems = listOf(
    NavItem(Routes.DASHBOARD, "HOME", Icons.Filled.Home),
    NavItem(Routes.WORKOUT, "TRAIN", Icons.Filled.FitnessCenter),
    NavItem(Routes.NUTRITION, "EAT", Icons.Filled.Restaurant),
    NavItem(Routes.QUESTS, "QUEST", Icons.Filled.Stars),
    NavItem(Routes.CHARACTER, "HERO", Icons.Filled.Person),
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authVm: AuthGate = hiltViewModel()
    val token by authVm.token.collectAsStateWithLifecycle()

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    LaunchedEffect(token) {
        if (token == null) {
            navController.navigate(Routes.LOGIN) {
                popUpTo(0) { inclusive = true }
            }
        } else if (currentRoute == Routes.LOGIN) {
            navController.navigate(Routes.DASHBOARD) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    val showBottomBar = token != null && currentRoute != Routes.LOGIN

    Scaffold(
        containerColor = ColorBgDeep,
        bottomBar = {
            if (showBottomBar) BottomBar(currentRoute) { route ->
                navController.navigate(route) {
                    popUpTo(Routes.DASHBOARD) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        },
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            NavHost(
                navController = navController,
                startDestination = if (token != null) Routes.DASHBOARD else Routes.LOGIN,
            ) {
                composable(Routes.LOGIN) {
                    LoginScreen(onLoggedIn = {
                        navController.navigate(Routes.DASHBOARD) {
                            popUpTo(0) { inclusive = true }
                        }
                    })
                }
                composable(Routes.DASHBOARD) {
                    DashboardScreen(onLogTap = { navController.navigate(Routes.WORKOUT) })
                }
                composable(Routes.WORKOUT) { WorkoutScreen() }
                composable(Routes.NUTRITION) { NutritionScreen() }
                composable(Routes.CHARACTER) { CharacterScreen() }
                composable(Routes.QUESTS) { QuestsScreen() }
            }
        }
    }
}

@Composable
private fun BottomBar(currentRoute: String?, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorSurface)
            .border(2.dp, ColorBorder, RectangleShape),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        navItems.forEach { item ->
            val selected = currentRoute == item.route
            Column(
                modifier = Modifier
                    .height(64.dp)
                    .padding(horizontal = 4.dp)
                    .background(if (selected) ColorAccent else Color.Transparent)
                    .clickable { onSelect(item.route) }
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = if (selected) Color.Black else ColorText,
                    modifier = Modifier.size(22.dp),
                )
                Text(
                    text = item.label,
                    color = if (selected) Color.Black else ColorMuted,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}
