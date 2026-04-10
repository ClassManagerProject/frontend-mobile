package com.example.primerlabcompose.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.primerlabcompose.data.model.BottomNavItem
import com.example.primerlabcompose.data.model.CalendarView
import com.example.primerlabcompose.ui.components.BottomNavigationBar
import com.example.primerlabcompose.ui.components.MonthGrid
import com.example.primerlabcompose.ui.components.TaskItem
import com.example.primerlabcompose.ui.navigation.Screen
import com.example.primerlabcompose.ui.theme.AppColors
import com.example.primerlabcompose.viewmodel.CalendarViewModel

// ── Pantalla principal del Calendario ─────────────────────────────────────────

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = viewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedItem = uiState.selectedNavItem,
                onItemSelected = { item ->
                    viewModel.onNavItemSelected(item)
                    when (item) {
                        BottomNavItem.TASKS -> {
                            val firstTaskId = uiState.tasks.firstOrNull()?.id ?: 1
                            navController.navigate(Screen.TaskDetails.createRoute(firstTaskId))
                        }
                        BottomNavItem.SETTINGS -> {
                            navController.navigate(Screen.AboutUs.route)
                        }
                        else -> Unit
                    }
                }
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

                    // ── Barra superior ────────────────────────────────────────
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = AppColors.Indigo600,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = "${uiState.currentMonth.monthName()} ${uiState.currentMonth.year}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.TextDark
                        )
                        Spacer(Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = AppColors.TextDark,
                            modifier = Modifier.size(24.dp).clickable { }
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    // ── Selector de vista ─────────────────────────────────────
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(50))
                            .background(AppColors.LightBg)
                            .padding(4.dp)
                    ) {
                        Row(Modifier.fillMaxWidth()) {
                            CalendarView.entries.forEach { view ->
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
                                        color = if (isSelected) AppColors.Indigo600 else AppColors.SlateGray
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // ── Navegación de mes ─────────────────────────────────────
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Mes anterior",
                            tint = AppColors.TextDark,
                            modifier = Modifier.size(28.dp).clickable { viewModel.onPreviousMonth() }
                        )
                        Text(
                            text = uiState.currentMonth.monthName(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AppColors.TextDark
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Mes siguiente",
                            tint = AppColors.TextDark,
                            modifier = Modifier.size(28.dp).clickable { viewModel.onNextMonth() }
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // ── Encabezado de días ────────────────────────────────────
                    val dayLabels = listOf("DOM", "LUN", "MAR", "MIE", "JUE", "VIE", "SAB")
                    Row(Modifier.fillMaxWidth()) {
                        dayLabels.forEach { label ->
                            Text(
                                text = label,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (label == "DOM" || label == "SAB") AppColors.Indigo600 else AppColors.SlateGray,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    // ── Cuadrícula del mes ────────────────────────────────────
                    MonthGrid(
                        monthYear  = uiState.currentMonth,
                        selectedDay = uiState.selectedDay,
                        dotsOnDays = uiState.dotsOnDays,
                        onDayClick = { date -> viewModel.onDayClick(date) }
                    )

                    Spacer(Modifier.height(28.dp))

                    Text(
                        "Mis tareas",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextDark
                    )
                    Spacer(Modifier.height(12.dp))
                }

                // ── Lista de tareas ───────────────────────────────────────────
                items(uiState.tasks, key = { it.id }) { task ->
                    TaskItem(
                        task          = task,
                        navController = navController,
                        onToggle      = { viewModel.onTaskToggle(task.id) }
                    )
                    Spacer(Modifier.height(10.dp))
                }

                item { Spacer(Modifier.height(20.dp)) }
            }

            // ── FAB ───────────────────────────────────────────────────────────
            FloatingActionButton(
                onClick        = { },
                modifier       = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                containerColor = AppColors.Indigo600,
                contentColor   = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir tarea")
            }
        }
    }
}

