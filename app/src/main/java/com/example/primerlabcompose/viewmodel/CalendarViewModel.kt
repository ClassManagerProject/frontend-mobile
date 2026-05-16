package com.example.primerlabcompose.viewmodel

import androidx.lifecycle.ViewModel
import com.example.primerlabcompose.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// ── ViewModel del Calendario ───────────────────────────────────────────────────

class CalendarViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        CalendarUiState(
            tasks = listOf(
                Task(1, "Finalizar proyecto apps",         Priority.HIGH,   "2h restantes"),
                Task(2, "Aguartame las pinches clases",    Priority.MEDIUM, "semestre"),
                Task(3, "No engordar",                     Priority.LOW,    "")
            )
        )
    )
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    fun onViewSelected(view: CalendarView) {
        _uiState.update { it.copy(selectedView = view) }
    }

    fun onPreviousMonth() {
        _uiState.update { it.copy(currentMonth = it.currentMonth.prev()) }
    }

    fun onNextMonth() {
        _uiState.update { it.copy(currentMonth = it.currentMonth.next()) }
    }

    fun onDayClick(date: SimpleDate) {
        _uiState.update {
            it.copy(selectedDay = if (it.selectedDay == date) null else date)
        }
    }

    fun onTaskToggle(taskId: Int) {
        _uiState.update { state ->
            state.copy(
                tasks = state.tasks.map { task ->
                    if (task.id == taskId) task.copy(isDone = !task.isDone) else task
                }
            )
        }
    }

    fun onNavItemSelected(item: BottomNavItem) {
        _uiState.update { it.copy(selectedNavItem = item) }
    }
}
