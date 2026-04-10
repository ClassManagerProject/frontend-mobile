package com.example.primerlabcompose.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.primerlabcompose.data.model.Priority
import com.example.primerlabcompose.data.model.SubTask
import com.example.primerlabcompose.ui.theme.AppColors
import com.example.primerlabcompose.viewmodel.TaskDetailsViewModel

/**
 * Pantalla de detalles de una tarea en Jetpack Compose.
 * La navegación se gestiona con NavController (Navigation Compose).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(
    taskId: Int,
    navController: NavController,
    viewModel: TaskDetailsViewModel = viewModel()
) {
    LaunchedEffect(taskId) { viewModel.loadTaskDetails(taskId) }

    val uiState by viewModel.uiState.collectAsState()
    var showMenu           by remember { mutableStateOf(false) }
    var showDeleteDialog   by remember { mutableStateOf(false) }
    var showAddSubDialog   by remember { mutableStateOf(false) }
    var newSubTaskText     by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de tarea", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menú")
                    }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(text = { Text("Duplicar") },          onClick = { showMenu = false })
                        DropdownMenuItem(text = { Text("Mover a otra lista") }, onClick = { showMenu = false })
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor            = Color.White,
                    titleContentColor         = AppColors.TextDark,
                    navigationIconContentColor = AppColors.TextDark
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator(color = AppColors.Indigo600) }
            return@Scaffold
        }

        val task = uiState.taskDetail ?: return@Scaffold

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(Modifier.height(8.dp)) }

            // ── Título ────────────────────────────────────────────────────────
            item {
                Text(task.title, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = AppColors.TextDark)
            }

            // ── Badges de categoría, prioridad y estado ───────────────────────
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Categoría
                    Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFF1F5F9)) {
                        Text(
                            task.category,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontSize = 12.sp, fontWeight = FontWeight.Medium, color = AppColors.SlateGray
                        )
                    }
                    // Prioridad
                    val (pLabel, pBg, pFg) = when (task.priority) {
                        Priority.HIGH   -> Triple("HIGH PRIORITY", AppColors.Indigo100,   AppColors.PriorityHigh)
                        Priority.MEDIUM -> Triple("MEDIUM",         Color(0xFFE0F2FE),     AppColors.PriorityMed)
                        Priority.LOW    -> Triple("LOW",             Color(0xFFE2E8F0),     Color(0xFF334155))
                    }
                    Surface(shape = RoundedCornerShape(8.dp), color = pBg) {
                        Text(pLabel, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontSize = 10.sp, fontWeight = FontWeight.Bold, color = pFg)
                    }
                    // Estado
                    val sBg = if (task.isCompleted) AppColors.GreenDone else Color(0xFFFEF3C7)
                    val sFg = if (task.isCompleted) AppColors.GreenText  else Color(0xFF92400E)
                    Surface(shape = RoundedCornerShape(8.dp), color = sBg) {
                        Text(task.status, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontSize = 10.sp, fontWeight = FontWeight.Bold, color = sFg)
                    }
                }
            }

            // ── Horario y fecha ───────────────────────────────────────────────
            item {
                Surface(shape = RoundedCornerShape(12.dp), color = AppColors.LightBg, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        DetailRow("Horario",      task.schedule)
                        DetailRow("Fecha límite", task.dueDate)
                    }
                }
            }

            // ── Sub-tareas ────────────────────────────────────────────────────
            item {
                val completed = task.subTasks.count { it.isDone }
                val total     = task.subTasks.size
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Sub-tareas", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = AppColors.TextDark)
                        Text("$completed/$total completadas", fontSize = 12.sp, color = AppColors.SlateGray)
                    }
                    task.subTasks.forEach { sub ->
                        SubTaskRow(sub) { viewModel.toggleSubTask(sub.id) }
                    }
                    OutlinedButton(
                        onClick  = { showAddSubDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(10.dp),
                        colors   = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.Indigo600)
                    ) { Text("+ Agregar sub-tarea", fontSize = 14.sp) }
                }
            }

            // ── Notas ─────────────────────────────────────────────────────────
            if (task.notes.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Notas", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = AppColors.TextDark)
                        Surface(shape = RoundedCornerShape(12.dp), color = AppColors.LightBg, modifier = Modifier.fillMaxWidth()) {
                            Text(task.notes, modifier = Modifier.padding(16.dp),
                                fontSize = 14.sp, color = AppColors.SlateGray, lineHeight = 22.sp)
                        }
                    }
                }
            }

            // ── Acciones ──────────────────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick  = { showDeleteDialog = true },
                        modifier = Modifier.weight(1f),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444))
                    ) { Text("Eliminar", fontSize = 14.sp, fontWeight = FontWeight.SemiBold) }

                    Button(
                        onClick = { viewModel.markAsCompleted(); navController.popBackStack() },
                        modifier = Modifier.weight(1f),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = AppColors.Indigo600)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Completar", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }

    // ── Diálogo: Eliminar ─────────────────────────────────────────────────────
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title   = { Text("Eliminar tarea") },
            text    = { Text("¿Estás seguro de que deseas eliminar esta tarea?") },
            confirmButton = {
                TextButton(
                    onClick = { showDeleteDialog = false; navController.popBackStack() },
                    colors  = ButtonDefaults.textButtonColors(contentColor = Color(0xFFEF4444))
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") }
            }
        )
    }

    // ── Diálogo: Agregar sub-tarea ────────────────────────────────────────────
    if (showAddSubDialog) {
        AlertDialog(
            onDismissRequest = { showAddSubDialog = false; newSubTaskText = "" },
            title = { Text("Agregar sub-tarea") },
            text  = {
                OutlinedTextField(
                    value = newSubTaskText,
                    onValueChange = { newSubTaskText = it },
                    label = { Text("Ingresa la sub-tarea") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newSubTaskText.isNotBlank()) viewModel.addSubTask(newSubTaskText)
                    showAddSubDialog = false; newSubTaskText = ""
                }) { Text("Agregar") }
            },
            dismissButton = {
                TextButton(onClick = { showAddSubDialog = false; newSubTaskText = "" }) { Text("Cancelar") }
            }
        )
    }
}

// ── Fila label / valor ────────────────────────────────────────────────────────
@Composable
private fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 13.sp, color = AppColors.SlateGray)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = AppColors.TextDark)
    }
}

// ── Fila de sub-tarea ─────────────────────────────────────────────────────────
@Composable
private fun SubTaskRow(subTask: SubTask, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (subTask.isDone) AppColors.GreenDone else Color(0xFFF8F9FC))
            .border(1.dp, if (subTask.isDone) AppColors.GreenCheck.copy(0.4f) else Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(if (subTask.isDone) AppColors.GreenCheck else Color.White)
                .border(2.dp, if (subTask.isDone) Color.Transparent else Color(0xFFCBD5E1), CircleShape)
                .clickable { onToggle() },
            contentAlignment = Alignment.Center
        ) {
            if (subTask.isDone) {
                Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(14.dp))
            }
        }
        Spacer(Modifier.width(10.dp))
        Text(
            text           = subTask.title,
            fontSize       = 14.sp,
            color          = if (subTask.isDone) AppColors.GreenText else AppColors.TextDark,
            textDecoration = if (subTask.isDone) TextDecoration.LineThrough else TextDecoration.None,
            modifier       = Modifier.weight(1f)
        )
    }
}

