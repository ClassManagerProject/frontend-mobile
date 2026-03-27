package com.example.primerlabcompose

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.primerlabcompose.databinding.ActivityTaskDetailsBinding
import com.example.primerlabcompose.viewmodel.TaskDetailsViewModel
import com.example.primerlabcompose.viewmodel.SubTask
import kotlinx.coroutines.launch

/**
 * Activity para mostrar los detalles de una tarea
 * Utiliza ViewBinding para acceder a las vistas
 * Implementa MVVM con TaskDetailsViewModel
 */
class TaskDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskDetailsBinding
    private val viewModel: TaskDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ── Inicializar ViewBinding ───────────────────────────────────────────────
        binding = ActivityTaskDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ── Obtener ID de la tarea desde Intent ───────────────────────────────────
        val taskId = intent.getIntExtra(EXTRA_TASK_ID, 1)

        // ── Cargar datos de la tarea ──────────────────────────────────────────────
        viewModel.loadTaskDetails(taskId)

        // ── Configurar listeners ──────────────────────────────────────────────────
        setupListeners()

        // ── Observar cambios de estado ────────────────────────────────────────────
        observeUiState()

        // ── Observar navegación ───────────────────────────────────────────────────
        viewModel.navigateBack.observe(this, Observer { shouldGoBack ->
            if (shouldGoBack) {
                finish()
            }
        })
    }

    /**
     * Configurar listeners de botones y eventos
     */
    private fun setupListeners() {
        // Botón atrás
        binding.btnBack.setOnClickListener {
            viewModel.goBack()
        }

        // Botón menú
        binding.btnMenu.setOnClickListener {
            showTaskMenu()
        }

        // Botón marcar como completado
        binding.btnMarkCompleted.setOnClickListener {
            viewModel.markAsCompleted()
            finish()  // Volver a pantalla anterior después de completar
        }

        // Botón editar
        binding.btnEdit.setOnClickListener {
            navigateToEditTask()
        }

        // Botón eliminar
        binding.btnDelete.setOnClickListener {
            showDeleteConfirmation()
        }

        // Botón agregar sub-tarea
        binding.btnAddSubTask.setOnClickListener {
            showAddSubTaskDialog()
        }
    }

    /**
     * Observar cambios en el estado de la UI
     */
    private fun observeUiState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                uiState.taskDetail?.let { taskDetail ->
                    // Actualizar título
                    binding.taskTitle.text = taskDetail.title

                    // Actualizar categoría
                    binding.taskCategory.text = taskDetail.category

                    // Actualizar badges
                    binding.chipPriority.text = when (taskDetail.priority) {
                        com.example.primerlabcompose.viewmodel.Priority.HIGH -> "HIGH PRIORITY"
                        com.example.primerlabcompose.viewmodel.Priority.MEDIUM -> "MEDIUM"
                        com.example.primerlabcompose.viewmodel.Priority.LOW -> "LOW"
                    }

                    binding.chipStatus.text = taskDetail.status

                    // Actualizar colores de badges según prioridad
                    val (bgColor, textColor) = when (taskDetail.priority) {
                        com.example.primerlabcompose.viewmodel.Priority.HIGH -> {
                            Pair(
                                resources.getColor(R.color.indigo_100),
                                resources.getColor(R.color.indigo_700)
                            )
                        }
                        com.example.primerlabcompose.viewmodel.Priority.MEDIUM -> {
                            Pair(
                                resources.getColor(R.color.cyan_50),
                                resources.getColor(R.color.cyan_600)
                            )
                        }
                        com.example.primerlabcompose.viewmodel.Priority.LOW -> {
                            Pair(
                                resources.getColor(R.color.slate_100),
                                resources.getColor(R.color.slate_600)
                            )
                        }
                    }

                    binding.chipPriority.setTextColor(textColor)

                    // Actualizar horario y fecha
                    binding.taskSchedule.text = taskDetail.schedule
                    binding.taskDueDate.text = taskDetail.dueDate

                    // Actualizar notas
                    binding.taskNotes.text = taskDetail.notes

                    // Actualizar sub-tareas
                    updateSubTasksUI(taskDetail.subTasks)

                    // Actualizar progreso de sub-tareas
                    val completed = viewModel.getCompletedSubTasksCount()
                    val total = viewModel.getTotalSubTasksCount()
                    binding.subTasksProgress.text = "$completed/$total Completed"
                }
            }
        }
    }

    /**
     * Actualizar la UI de sub-tareas
     */
    private fun updateSubTasksUI(subTasks: List<SubTask>) {
        binding.subTasksContainer.removeAllViews()

        subTasks.forEach { subTask ->
            val checkboxView = android.widget.CheckBox(this).apply {
                text = subTask.title
                isChecked = subTask.isDone
                textSize = 14f
                setTextColor(
                    if (subTask.isDone)
                        resources.getColor(R.color.slate_400)
                    else
                        resources.getColor(R.color.slate_900)
                )
                paintFlags = paintFlags or if (subTask.isDone) android.graphics.Paint.STRIKE_THRU_TEXT_FLAG else 0
                setOnCheckedChangeListener { _, _ ->
                    viewModel.toggleSubTask(subTask.id)
                }
            }
            binding.subTasksContainer.addView(checkboxView)
        }
    }

    /**
     * Mostrar menú de opciones
     */
    private fun showTaskMenu() {
        android.app.AlertDialog.Builder(this)
            .setTitle("Opciones")
            .setItems(arrayOf("Duplicar", "Mover a otra lista", "Cancelar")) { _, which ->
                when (which) {
                    0 -> {
                        // Duplicar tarea
                        showMessage("Tarea duplicada")
                    }
                    1 -> {
                        // Mover tarea
                        showMessage("Tarea movida")
                    }
                }
            }
            .show()
    }

    /**
     * Navegar a pantalla de editar tarea
     */
    private fun navigateToEditTask() {
        val intent = android.content.Intent(this, EditTaskActivity::class.java).apply {
            val taskId = intent.getIntExtra(EXTRA_TASK_ID, 1)
            putExtra(EXTRA_TASK_ID, taskId)
        }
        startActivity(intent)
    }

    /**
     * Mostrar diálogo de confirmación para eliminar
     */
    private fun showDeleteConfirmation() {
        android.app.AlertDialog.Builder(this)
            .setTitle("Eliminar tarea")
            .setMessage("¿Estás seguro de que deseas eliminar esta tarea?")
            .setPositiveButton("Eliminar") { _, _ ->
                showMessage("Tarea eliminada")
                finish()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Mostrar diálogo para agregar nueva sub-tarea
     */
    private fun showAddSubTaskDialog() {
        val input = android.widget.EditText(this).apply {
            hint = "Ingresa la sub-tarea"
            setPadding(16, 16, 16, 16)
        }

        android.app.AlertDialog.Builder(this)
            .setTitle("Agregar sub-tarea")
            .setView(input)
            .setPositiveButton("Agregar") { _, _ ->
                val subTaskTitle = input.text.toString()
                if (subTaskTitle.isNotEmpty()) {
                    viewModel.addSubTask(subTaskTitle)
                    showMessage("Sub-tarea agregada")
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Mostrar mensaje corto (Toast)
     */
    private fun showMessage(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_TASK_ID = "task_id"
    }
}

/**
 * Activity placeholder para edición de tareas
 * Se implementará en futuros sprints
 */
class EditTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Implementar pantalla de edición
    }
}

