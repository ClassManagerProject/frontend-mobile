package com.example.primerlabcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.primerlabcompose.viewmodel.*

// ── Colors ────────────────────────────────────────────────────────────────────
val Indigo600 = Color(0xFF4338CA)
val Indigo100 = Color(0xFFEEF2FF)
val SlateGray = Color(0xFF64748B)
val LightBg = Color(0xFFF8F9FC)
val DotColor = Color(0xFF94A3B8)
val GreenDone = Color(0xFFD1FAE5)
val GreenText = Color(0xFF065F46)
val GreenCheck = Color(0xFF10B981)
val PriorityHigh = Color(0xFF6D28D9)
val PriorityMed = Color(0xFF0891B2)
val PriorityLow = Color(0xFF64748B)


// ── MainActivity ──────────────────────────────────────────────────────────────
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    CalendarScreen()
                }
            }
        }
    }
}

// ── Main screen ───────────────────────────────────────────────────────────
@Composable
fun CalendarScreen(viewModel: CalendarViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedItem = uiState.selectedNavItem,
                onItemSelected = { viewModel.onNavItemSelected(it) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(horizontal = 16.dp)
            ) {
        item {
            Spacer(Modifier.height(24.dp))

            // ── Top bar ──────────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Indigo600,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "${uiState.currentMonth.monthName()} ${uiState.currentMonth.year}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF1E293B),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { }
                )
            }

            Spacer(Modifier.height(20.dp))

            // ── Tab row ──────────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50))
                    .background(LightBg)
                    .padding(4.dp)
            ) {
                Row(Modifier.fillMaxWidth()) {
                    CalendarView.values().forEach { view ->
                        val isSelected = view == uiState.selectedView
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(50))
                                .background(if (isSelected) Color.White else Color.Transparent)
                                .clickable { viewModel.onViewSelected(view) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = view.name,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) Indigo600 else SlateGray
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Month navigation ─────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Previous",
                    tint = Color(0xFF1E293B),
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { viewModel.onPreviousMonth() }
                )
                Text(
                    text = uiState.currentMonth.monthName(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1E293B)
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Next",
                    tint = Color(0xFF1E293B),
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { viewModel.onNextMonth() }
                )
            }

            Spacer(Modifier.height(12.dp))

            // ── Day-of-week header ───────────────────────────────────────────────
            val dayLabels = listOf("DOM", "LUN", "MAR", "MIE", "JUE", "VIE", "SAB")
            Row(Modifier.fillMaxWidth()) {
                dayLabels.forEach { label ->
                    Text(
                        text = label,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (label == "DOM" || label == "SAB") Indigo600 else SlateGray,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // ── Calendar grid ────────────────────────────────────────────────────
            MonthGrid(
                monthYear = uiState.currentMonth,
                selectedDay = uiState.selectedDay,
                dotsOnDays = uiState.dotsOnDays,
                onDayClick = { date -> viewModel.onDayClick(date) }
            )

            Spacer(Modifier.height(28.dp))

            // Tasks Header
            Text(
                "Mis tareas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )
            Spacer(Modifier.height(12.dp))
        }

        // Task Items
        items(uiState.tasks, key = { it.id }) { task ->
            TaskItem(
                task = task,
                onToggle = { viewModel.onTaskToggle(task.id) }
            )
            Spacer(Modifier.height(10.dp))
        }

        item {
            Spacer(Modifier.height(20.dp))
        }
        }

            // FAB Flotante
            FloatingActionButton(
                onClick = { },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = Indigo600,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir tarea")
            }
        }
    }
}

// ── Bottom Navigation Bar ─────────────────────────────────────────────────────
@Composable
fun BottomNavigationBar(
    selectedItem: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        BottomNavItem.values().forEach { item ->
            val isSelected = item == selectedItem
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = when (item) {
                            BottomNavItem.TASKS -> Icons.Default.Check
                            BottomNavItem.SCHEDULE -> Icons.Default.DateRange
                            BottomNavItem.PROFILE -> Icons.Default.AccountCircle
                            BottomNavItem.SETTINGS -> Icons.Default.Settings
                        },
                        contentDescription = item.name,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = when (item) {
                            BottomNavItem.TASKS -> "Tareas"
                            BottomNavItem.SCHEDULE -> "Calendario"
                            BottomNavItem.PROFILE -> "Perfil"
                            BottomNavItem.SETTINGS -> "Ajustes"
                        },
                        fontSize = 12.sp
                    )
                },
                selected = isSelected,
                onClick = { onItemSelected(item) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Indigo600,
                    selectedTextColor = Indigo600,
                    unselectedIconColor = SlateGray,
                    unselectedTextColor = SlateGray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun TaskItem(task: Task, onToggle: () -> Unit) {
    val bgColor      = if (task.isDone) GreenDone else Color.White
    val borderColor  = if (task.isDone) GreenCheck.copy(alpha = 0.4f) else Color(0xFFE2E8F0)
    val titleColor   = if (task.isDone) GreenText else Color(0xFF1E293B)
    val titleDecor   = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Checkbox
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (task.isDone) GreenCheck else Color.White)
                .border(
                    width = if (task.isDone) 0.dp else 2.dp,
                    color = if (task.isDone) Color.Transparent else Color(0xFFCBD5E1),
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { onToggle() },
            contentAlignment = Alignment.Center
        ) {
            if (task.isDone) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }

        Spacer(Modifier.width(12.dp))

        // Title + badges
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = titleColor,
                textDecoration = titleDecor
            )
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Priority badge
                if (!task.isDone) {
                    PriorityBadge(task.priority)
                } else {
                    CompletedBadge()
                }
                // Subtitle text (time / persons)
                if (!task.isDone && task.subtitle.isNotEmpty()) {
                    Text(task.subtitle, fontSize = 12.sp, color = SlateGray)
                }
            }
        }
    }
}
// ── Priority badge ────────────────────────────────────────────────────────────
@Composable
fun PriorityBadge(priority: Priority) {
    val (label, bg, fg) = when (priority) {
        Priority.HIGH   -> Triple("HIGH PRIORITY", Indigo100, PriorityHigh)
        Priority.MEDIUM -> Triple("MEDIUM",        Color(0xFFE0F2FE), PriorityMed)
        Priority.LOW    -> Triple("LOW",            Color(0xFFF1F5F9), PriorityLow)
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = fg, letterSpacing = 0.3.sp)
    }
}

// ── Completed badge ───────────────────────────────────────────────────────────
@Composable
fun CompletedBadge() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(GreenCheck.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text("COMPLETED", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GreenText, letterSpacing = 0.3.sp)
    }
}

// ── Month grid ────────────────────────────────────────────────────────────────
@Composable
fun MonthGrid(
    monthYear: MonthYear,
    selectedDay: SimpleDate?,
    dotsOnDays: Set<Int>,
    onDayClick: (SimpleDate) -> Unit
) {
    val startOffset = monthYear.firstDayOfWeek()
    val daysInMonth = monthYear.daysInMonth()
    val totalCells = startOffset + daysInMonth
    val rows = (totalCells + 6) / 7

    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
        for (row in 0 until rows) {
            Row(Modifier.fillMaxWidth()) {
                for (col in 0 until 7) {
                    val cellIndex = row * 7 + col
                    val dayNumber = cellIndex - startOffset + 1
                    if (dayNumber < 1 || dayNumber > daysInMonth) {
                        Spacer(Modifier.weight(1f))
                    } else {
                        val date = SimpleDate(dayNumber, monthYear.month, monthYear.year)
                        val isSelected = date == selectedDay
                        val hasEvent = dayNumber in dotsOnDays

                        DayCell(
                            day = dayNumber,
                            isSelected = isSelected,
                            hasEvent = hasEvent,
                            modifier = Modifier.weight(1f),
                            onClick = { onDayClick(date) }
                        )
                    }
                }
            }
        }
    }
}

// ── Single day cell ───────────────────────────────────────────────────────────
@Composable
fun DayCell(
    day: Int,
    isSelected: Boolean,
    hasEvent: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .aspectRatio(1f)
            .padding(3.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Indigo600 else Color.Transparent)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = day.toString(),
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.White else Color(0xFF1E293B),
            textAlign = TextAlign.Center
        )
        if (hasEvent) {
            Spacer(Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) Color.White.copy(alpha = 0.7f) else DotColor)
            )
        }
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CalendarPreview() {
    MaterialTheme {
        CalendarScreen()
    }
}