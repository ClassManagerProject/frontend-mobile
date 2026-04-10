package com.example.primerlabcompose.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ── Modelos de estado ─────────────────────────────────────────────────────────

data class TechItem(val name: String, val description: String)

data class DeveloperInfo(
    val name: String,
    val role: String,
    val institution: String,
    val email: String
)

data class AboutUsUiState(
    val appName: String = "PrimerLab Compose",
    val appVersion: String = "1.0.0",
    val appDescription: String =
        "Aplicación de gestión de tareas y calendario desarrollada como proyecto de laboratorio " +
        "usando las mejores prácticas de Android moderno con Jetpack Compose.",
    val developers: List<DeveloperInfo> = listOf(
        DeveloperInfo(
            name        = "Equipo de Desarrollo",
            role        = "Desarrollador Android",
            institution = "Universidad / Lab",
            email       = "contacto@primerlabcompose.com"
        )
    ),
    val technologies: List<TechItem> = listOf(
        TechItem("Kotlin",               "Lenguaje principal de desarrollo"),
        TechItem("Jetpack Compose",      "UI declarativa moderna para Android"),
        TechItem("Navigation Compose",   "NavHost + NavController para navegación"),
        TechItem("ViewModel + StateFlow","Patrón MVVM con estado reactivo"),
        TechItem("Material Design 3",    "Componentes y paleta de diseño")
    ),
    val year: String = "2026"
)

// ── ViewModel ─────────────────────────────────────────────────────────────────

class AboutUsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AboutUsUiState())
    val uiState: StateFlow<AboutUsUiState> = _uiState.asStateFlow()
}

