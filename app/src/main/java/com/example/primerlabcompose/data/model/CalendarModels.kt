package com.example.primerlabcompose.data.model

import java.util.Calendar

// ── Modelos de dominio del Calendario ─────────────────────────────────────────

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
        return cal.get(Calendar.DAY_OF_WEEK) - 1
    }

    /** Nombre del mes en español */
    fun monthName(): String {
        val names = listOf(
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        )
        return names[month]
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

enum class BottomNavItem { TASKS, SCHEDULE, PROFILE, SETTINGS }

// ── Estado de la UI del Calendario ────────────────────────────────────────────

data class CalendarUiState(
    val selectedView: CalendarView = CalendarView.Month,
    val currentMonth: MonthYear = MonthYear(month = 1, year = 2026),
    val selectedDay: SimpleDate? = SimpleDate(20, 1, 2026),
    val tasks: List<Task> = emptyList(),
    val selectedNavItem: BottomNavItem = BottomNavItem.SCHEDULE,
    val dotsOnDays: Set<Int> = setOf(8, 12, 15, 22, 25, 29)
)

