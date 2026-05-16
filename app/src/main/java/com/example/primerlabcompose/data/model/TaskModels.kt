package com.example.primerlabcompose.data.model

// ── Modelos de dominio de Tareas ──────────────────────────────────────────────

data class SubTask(
    val id: Int,
    val title: String,
    val isDone: Boolean = false
)

data class TaskDetail(
    val id: Int,
    val title: String,
    val priority: Priority,
    val status: String,       // "IN PROGRESS", "COMPLETED", etc.
    val category: String,     // "Product Design"
    val schedule: String,     // "10:00 AM - 12:00 PM"
    val dueDate: String,      // "Viernes, Oct 27"
    val subTasks: List<SubTask>,
    val notes: String,
    val isCompleted: Boolean = false
)

// ── Estado de la UI de Detalle de Tarea ───────────────────────────────────────

data class TaskDetailsUiState(
    val taskDetail: TaskDetail? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isCompleted: Boolean = false
)

