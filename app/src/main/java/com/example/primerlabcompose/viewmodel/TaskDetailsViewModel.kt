package com.example.primerlabcompose.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// ── Data Models para Sub-tareas ────────────────────────────────────────────────

data class SubTask(
    val id: Int,
    val title: String,
    val isDone: Boolean = false
)

data class TaskDetail(
    val id: Int,
    val title: String,
    val priority: Priority,
    val status: String,  // "IN PROGRESS", "COMPLETED", etc.
    val category: String,  // "Product Design"
    val schedule: String,  // "10:00 AM - 12:00 PM"
    val dueDate: String,  // "Friday, Oct 27"
    val subTasks: List<SubTask>,
    val notes: String,
    val isCompleted: Boolean = false
)

// ── UI State para TaskDetails ──────────────────────────────────────────────────

data class TaskDetailsUiState(
    val taskDetail: TaskDetail? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isCompleted: Boolean = false
)

// ── ViewModel ──────────────────────────────────────────────────────────────────

class TaskDetailsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<TaskDetailsUiState>(
        TaskDetailsUiState(isLoading = true)
    )
    val uiState: StateFlow<TaskDetailsUiState> = _uiState.asStateFlow()

    // LiveData para navegación entre vistas
    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean> = _navigateBack

    /**
     * Inicializar la pantalla con los datos de la tarea
     * En una app real, esto llamaría a un Repository/API
     */
    fun loadTaskDetails(taskId: Int) {
        // Simular carga de datos
        _uiState.update {
            it.copy(
                isLoading = false,
                taskDetail = getMockTaskData(taskId)
            )
        }
    }

    /**
     * Marcar la tarea como completada
     */
    fun markAsCompleted() {
        _uiState.update { currentState ->
            val updated = currentState.taskDetail?.copy(
                isCompleted = true,
                status = "COMPLETED"
            )
            currentState.copy(
                taskDetail = updated,
                isCompleted = true
            )
        }
    }

    /**
     * Toggle de sub-tarea completada
     */
    fun toggleSubTask(subTaskId: Int) {
        _uiState.update { currentState ->
            val updated = currentState.taskDetail?.copy(
                subTasks = currentState.taskDetail.subTasks.map { subtask ->
                    if (subtask.id == subTaskId) {
                        subtask.copy(isDone = !subtask.isDone)
                    } else {
                        subtask
                    }
                }
            )
            currentState.copy(taskDetail = updated)
        }
    }

    /**
     * Agregar una nueva sub-tarea
     */
    fun addSubTask(title: String) {
        _uiState.update { currentState ->
            val taskDetail = currentState.taskDetail ?: return@update currentState
            val newSubTaskId = (taskDetail.subTasks.maxOfOrNull { it.id } ?: 0) + 1
            val newSubTask = SubTask(id = newSubTaskId, title = title)
            val updated = taskDetail.copy(
                subTasks = taskDetail.subTasks + newSubTask
            )
            currentState.copy(taskDetail = updated)
        }
    }

    /**
     * Navegar atrás
     */
    fun goBack() {
        _navigateBack.value = true
    }

    /**
     * Datos mock para demostración
     * En producción, esto vendría de una base de datos o API
     */
    private fun getMockTaskData(taskId: Int): TaskDetail {
        return when (taskId) {
            1 -> TaskDetail(
                id = 1,
                title = "Finalizar propuesta de proyecto",
                priority = Priority.HIGH,
                status = "IN PROGRESS",
                category = "Product Design",
                schedule = "10:00 AM - 12:00 PM",
                dueDate = "Viernes, Oct 27",
                subTasks = listOf(
                    SubTask(1, "Escribir resumen ejecutivo", isDone = true),
                    SubTask(2, "Recopilar datos de investigación de mercado", isDone = true),
                    SubTask(3, "Revisar presupuesto con el equipo de finanzas", isDone = false),
                    SubTask(4, "Corrección final y formato", isDone = false)
                ),
                notes = "Asegúrate de que la paleta de colores del prototipo coincida con las directrices de marca del cliente. La presentación debe estar en formato PDF. No olvides incluir los resúmenes de la entrevista con las partes interesadas en el apéndice.",
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
                    SubTask(2, "Mantener una dieta equilibrada", isDone = false)
                ),
                notes = "Objetivo: Perder 5 kg en 3 meses. Consultar con un nutricionista.",
                isCompleted = false
            )
            else -> TaskDetail(
                id = taskId,
                title = "Tarea desconocida",
                priority = Priority.MEDIUM,
                status = "PENDING",
                category = "General",
                schedule = "",
                dueDate = "",
                subTasks = emptyList(),
                notes = "",
                isCompleted = false
            )
        }
    }

    /**
     * Calcular subtareas completadas
     */
    fun getCompletedSubTasksCount(): Int {
        return _uiState.value.taskDetail?.subTasks?.count { it.isDone } ?: 0
    }

    /**
     * Obtener total de subtareas
     */
    fun getTotalSubTasksCount(): Int {
        return _uiState.value.taskDetail?.subTasks?.size ?: 0
    }
}

