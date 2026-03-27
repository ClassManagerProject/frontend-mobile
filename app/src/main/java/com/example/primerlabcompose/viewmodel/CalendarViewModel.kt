package com.example.primerlabcompose.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar

// ── Data Models ───────────────────────────────────────────────────────────────

data class MonthYear(val month: Int, val year: Int) {

    fun next(): MonthYear = if (month == 11) MonthYear(0, year + 1)
    else MonthYear(month + 1, year)

    fun prev(): MonthYear = if (month == 0) MonthYear(11, year - 1)
    else MonthYear(month - 1, year)

    /** Número de días del mes */
    fun daysInMonth(): Int {
        val cal = Calendar.getInstance()
        cal.set(year, month, 1)
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    /** Día de la semana del día 1: 0 = domingo, 6 = sábado */
    fun firstDayOfWeek(): Int {
        val cal = Calendar.getInstance()
        cal.set(year, month, 1)
        return cal.get(Calendar.DAY_OF_WEEK) - 1  // Calendar retorna 1=Dom..7=Sáb
    }

    /** Nombre del mes en español */
    fun monthName(): String {
        val monthNames = listOf(
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        )
        return monthNames[month]
    }
}

data class SimpleDate(val day: Int, val month: Int, val year: Int)

enum class Priority { HIGH, MEDIUM, LOW }

data class Task(
    val id: Int,
    val title: String,
    val priority: Priority,
    val subtitle: String,
    val isDone: Boolean = false
)

enum class CalendarView { Day, Week, Month }

enum class BottomNavItem {
    TASKS, SCHEDULE, PROFILE, SETTINGS
}

// ── UI State ──────────────────────────────────────────────────────────────────

data class CalendarUiState(
    val selectedView: CalendarView = CalendarView.Month,
    val currentMonth: MonthYear = MonthYear(month = 1, year = 2026),
    val selectedDay: SimpleDate? = SimpleDate(20, 1, 2026),
    val tasks: List<Task> = emptyList(),
    val selectedNavItem: BottomNavItem = BottomNavItem.SCHEDULE,
    val dotsOnDays: Set<Int> = setOf(8, 12, 15, 22, 25, 29)
)

// ── ViewModel ─────────────────────────────────────────────────────────────────

class CalendarViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        CalendarUiState(
            tasks = listOf(
                Task(1, "Finalizar proyecto apps", Priority.HIGH, "2h restantes"),
                Task(2, "Aguartame las pinches clases", Priority.MEDIUM, "semestre"),
                Task(3, "No engordar", Priority.LOW, "")
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
        _uiState.update { currentState ->
            currentState.copy(
                tasks = currentState.tasks.map { task ->
                    if (task.id == taskId) task.copy(isDone = !task.isDone) else task
                }
            )
        }
    }

    fun onNavItemSelected(item: BottomNavItem) {
        _uiState.update { it.copy(selectedNavItem = item) }
    }
}

