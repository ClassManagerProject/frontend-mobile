package com.example.primerlabcompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.primerlabcompose.ui.screens.AboutUsScreen
import com.example.primerlabcompose.ui.screens.CalendarScreen
import com.example.primerlabcompose.ui.screens.TaskDetailsScreen

/**
 * Raíz de la navegación Compose.
 * Configura el NavHost con todas las pantallas de la aplicación.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController  = navController,
        startDestination = Screen.Calendar.route
    ) {
        // ── Pantalla Calendario ───────────────────────────────────────────────
        composable(route = Screen.Calendar.route) {
            CalendarScreen(navController = navController)
        }

        // ── Pantalla Detalle de Tarea ─────────────────────────────────────────
        composable(
            route     = Screen.TaskDetails.route,
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: 1
            TaskDetailsScreen(taskId = taskId, navController = navController)
        }

        // ── Pantalla Acerca de Nosotros ───────────────────────────────────────
        composable(route = Screen.AboutUs.route) {
            AboutUsScreen(navController = navController)
        }
    }
}

