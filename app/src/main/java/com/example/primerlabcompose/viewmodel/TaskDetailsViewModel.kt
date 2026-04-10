package com.example.primerlabcompose.viewmodel

import androidx.lifecycle.ViewModel
import com.example.primerlabcompose.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// ── ViewModel de Detalle de Tarea ──────────────────────────────────────────────

class TaskDetailsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TaskDetailsUiState(isLoading = true))
    val uiState: StateFlow<TaskDetailsUiState> = _uiState.asStateFlow()

    /** Carga los datos de una tarea por ID (mock) */
    fun loadTaskDetails(taskId: Int) {
        _uiState.update {
            it.copy(isLoading = false, taskDetail = getMockTaskData(taskId))
        }
    }

    /** Marca la tarea como completada */
    fun markAsCompleted() {
        _uiState.update { state ->
            state.copy(
                isCompleted = true,
                taskDetail = state.taskDetail?.copy(isCompleted = true, status = "COMPLETED")
            )
        }
    }

    /** Alterna el estado completado de una sub-tarea */
    fun toggleSubTask(subTaskId: Int) {
        _uiState.update { state ->
            state.copy(
                taskDetail = state.taskDetail?.copy(
                    subTasks = state.taskDetail.subTasks.map { sub ->
                        if (sub.id == subTaskId) sub.copy(isDone = !sub.isDone) else sub
                    }
                )
            )
        }
    }

    /** Agrega una nueva sub-tarea */
    fun addSubTask(title: String) {
        _uiState.update { state ->
            val task = state.taskDetail ?: return@update state
            val newId = (task.subTasks.maxOfOrNull { it.id } ?: 0) + 1
            state.copy(
                taskDetail = task.copy(subTasks = task.subTasks + SubTask(newId, title))
            )
        }
    }

    fun getCompletedSubTasksCount(): Int =
        _uiState.value.taskDetail?.subTasks?.count { it.isDone } ?: 0

    fun getTotalSubTasksCount(): Int =
        _uiState.value.taskDetail?.subTasks?.size ?: 0

    // ── Datos mock ────────────────────────────────────────────────────────────

    private fun getMockTaskData(taskId: Int): TaskDetail = when (taskId) {
        1 -> TaskDetail(
            id = 1,
            title = "Finalizar proyecto apps",
            priority = Priority.HIGH,
            status = "IN PROGRESS",
            category = "Product Design",
            schedule = "10:00 AM - 12:00 PM",
            dueDate = "Viernes, Oct 27",
            subTasks = listOf(
                SubTask(1, "Escribir resumen ejecutivo",                       isDone = true),
                SubTask(2, "Recopilar datos de investigación de mercado",      isDone = true),
                SubTask(3, "Revisar presupuesto con el equipo de finanzas",    isDone = false),
                SubTask(4, "Corrección final y formato",                       isDone = false)
            ),
            notes = "Asegúrate de que la paleta de colores del prototipo coincida con las directrices de marca del cliente. La presentación debe estar en formato PDF.",
            isCompleted = false
        )
        2 -> TaskDetail(
            id = 2,
            title = "Aguartame las pinches clases",
            priority = Priority.MEDIUM,
            status = "IN PROGRESS",
            category = "Education",
            schedule = "08:00 AM - 05:00 PM",
            dueDate = "Viernes, Dic 31",
            subTasks = emptyList(),
            notes = "Asiste a todas las clases del semestre",
            isCompleted = false
        )
        3 -> TaskDetail(
            id = 3,
            title = "No engordar",
            priority = Priority.LOW,
            status = "IN PROGRESS",
            category = "Health",
            schedule = "Diariamente",
            dueDate = "Indefinido",
            subTasks = listOf(
                SubTask(1, "Hacer ejercicio 3 veces por semana", isDone = false),
                SubTask(2, "Mantener una dieta equilibrada",     isDone = false)
            ),
            notes = "Objetivo: mantener el peso. Consultar con un nutricionista.",
            isCompleted = false
        )
        else -> TaskDetail(
            id = taskId, title = "Tarea desconocida", priority = Priority.MEDIUM,
            status = "PENDING", category = "General", schedule = "", dueDate = "",
            subTasks = emptyList(), notes = "", isCompleted = false
        )
    }
}
