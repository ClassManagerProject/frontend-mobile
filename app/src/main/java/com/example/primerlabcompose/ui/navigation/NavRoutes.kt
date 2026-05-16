package com.example.primerlabcompose.ui.navigation

/**
 * Rutas de navegación de la aplicación.
 */
sealed class Screen(val route: String) {

    /** Pantalla principal del calendario */
    object Calendar : Screen("calendar")

    /** Pantalla de detalles de una tarea — recibe {taskId} como argumento */
    object TaskDetails : Screen("task_details/{taskId}") {
        fun createRoute(taskId: Int): String = "task_details/$taskId"
    }

    /** Pantalla Acerca de nosotros */
    object AboutUs : Screen("about_us")
}

