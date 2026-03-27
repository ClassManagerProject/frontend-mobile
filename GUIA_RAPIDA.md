# 🚀 Guía Rápida de Uso - primerLabCompose

## 🎯 Cómo Ejecutar la App

### 1. Compilar el Proyecto
```bash
cd C:\Users\jcgpb\AndroidStudioProjects\primerLabCompose
./gradlew clean build
```

### 2. Ejecutar en Emulador
```bash
./gradlew installDebug
# O desde Android Studio: Run → Run 'app'
```

### 3. Flujo de Uso

#### Pantalla Principal (MainActivity)
```
┌─────────────────────────────────────┐
│ 📅 Febrero 2026          🔍          │ ← Header con calendario
├─────────────────────────────────────┤
│  [Day] [Week] [Month]               │ ← Selector de vista
├─────────────────────────────────────┤
│  ◀ Febrero ▶                        │ ← Navegación de mes
├─────────────────────────────────────┤
│ DOM LUN MAR MIE JUE VIE SAB         │ ← Días en español
│  1   2   3   4   5   6   7          │
│  8•  9  10  11  12•  13  14•        │
│ ... etc ...                         │
├─────────────────────────────────────┤
│ Mis tareas                          │
├─────────────────────────────────────┤
│ ☐ Finalizar proyecto apps   HIGH    │ ← Click para detalles
│   2h restantes                      │
│                                     │
│ ☐ Aguartame las pinches c... MEDIUM │
│   semestre                          │
│                                     │
│ ☐ No engordar                  LOW  │ ← FIXED: Tag ahora visible
│                                     │
├─────────────────────────────────────┤
│ 🎯 Tareas | 📅 Calendario |  👤 ... │ ← Bottom Nav
└─────────────────────────────────────┘
                           ┌─────┐
                           │  +  │ ← FAB Azul
                           └─────┘
```

#### Click en Tarea → TaskDetailsActivity
```
Intent intent = new Intent(context, TaskDetailsActivity.class);
intent.putExtra(TaskDetailsActivity.EXTRA_TASK_ID, 1);  // ID = 1
context.startActivity(intent);
```

#### Pantalla de Detalles (TaskDetailsActivity)
```
┌─────────────────────────────────────┐
│ ◀      Task Details          ⋮       │ ← Header
├─────────────────────────────────────┤
│ [HIGH PRIORITY] [IN PROGRESS]       │ ← Badges
│                                     │
│ Finalize Project Proposal           │ ← Título (28sp, bold)
│ 📋 Product Design                   │ ← Categoría
│                                     │
│ ┌─────────────────────────────────┐ │
│ │ 🕐                              │ │ ← Card: Horario
│ │ SCHEDULE / HORARIO              │ │
│ │ 10:00 AM - 12:00 PM             │ │
│ └─────────────────────────────────┘ │
│                                     │
│ ┌─────────────────────────────────┐ │
│ │ 📅                              │ │ ← Card: Fecha vencimiento
│ │ DUE DATE / FECHA DE FIN         │ │
│ │ Viernes, Oct 27                 │ │
│ └─────────────────────────────────┘ │
│                                     │
│ SUB-TASKS                 2/4 Done  │ ← Contador
│ ☑ Draft executive summary          │ ← Completado (tachado)
│ ☑ Compile market research data     │ ← Completado (tachado)
│ ☐ Review budget with finance team  │ ← Pendiente
│ ☐ Final proofreading and formatting│ ← Pendiente
│ + Add Sub-task                      │ ← Agregar nueva
│                                     │
│ NOTES                               │
│ Ensure the color palette...         │ ← Texto descriptivo
│ ┌─────────────────────────────────┐ │
│ │ ✓ Mark as Completed             │ │ ← Botón principal azul
│ └─────────────────────────────────┘ │
│ ┌───────────────┐ ┌───────────────┐ │
│ │ ✏ Edit        │ │ 🗑 Delete     │ │ ← Botones secundarios
│ └───────────────┘ └───────────────┘ │
└─────────────────────────────────────┘
```

---

## 💻 Estructura de Carpetas

```
app/src/main/
├── java/com/example/primerlabcompose/
│   ├── MainActivity.kt               ← Activity principal (Compose)
│   ├── TaskDetailsActivity.kt        ← NEW: Detalles con ViewBinding
│   │
│   └── viewmodel/
│       ├── CalendarViewModel.kt      ← Gestiona pantalla principal
│       └── TaskDetailsViewModel.kt   ← NEW: Gestiona detalles
│
└── res/
    ├── layout/
    │   └── activity_task_details.xml ← NEW: XML layout
    │
    ├── drawable/
    │   ├── bg_icon_circle.xml
    │   ├── bg_button_primary.xml
    │   ├── bg_button_outline.xml
    │   ├── ic_more_vert.xml
    │   ├── ic_edit.xml
    │   └── ic_delete.xml
    │
    └── values/
        ├── colors.xml               ← UPDATED: 15+ colores
        ├── strings.xml              ← UPDATED: Labels españoles
        └── themes.xml
```

---

## 🔧 Cómo Modificar

### Cambiar Color del FAB

**Archivo:** `MainActivity.kt`

```kotlin
FloatingActionButton(
    onClick = { /* TODO */ },
    modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp),
    containerColor = Indigo600,  // ← Cambiar aquí
    contentColor = Color.White
) {
    Icon(Icons.Default.Add, contentDescription = "Añadir tarea")
}
```

### Agregar Nuevos Colores

**Archivo:** `app/src/main/res/values/colors.xml`

```xml
<color name="mi_color_personalizado">#FF00FF00</color>
```

Usar en código:
```kotlin
Color(0xFF00FF00)  // O en Compose
// O en XML:
android:textColor="@color/mi_color_personalizado"
```

### Modificar Texto de Botón

**Archivo:** `app/src/main/res/values/strings.xml`

```xml
<string name="edit">✏ Edit</string>
<!-- Cambiar a: -->
<string name="edit">✏ Editar</string>
```

Usar en código:
```kotlin
binding.btnEdit.text = getString(R.string.edit)
```

### Conectar a Base de Datos

**Archivo:** `TaskDetailsViewModel.kt`

Actualizar `getMockTaskData()`:
```kotlin
// Reemplazar datos mockup con llamada a API/DB:
private suspend fun getMockTaskData(taskId: Int): TaskDetail {
    return repository.getTaskById(taskId)  // ← Conectar aquí
}
```

---

## 🎨 Paleta de Colores Rápida

### Usar en Compose
```kotlin
// Colores predefinidos en MainActivity.kt
val Indigo600 = Color(0xFF4338CA)
val Indigo100 = Color(0xFFEEF2FF)
val SlateGray = Color(0xFF64748B)
val GreenCheck = Color(0xFF10B981)
val PriorityHigh = Color(0xFF6D28D9)

// En composables:
Text("Hello", color = Indigo600)
Box(modifier = Modifier.background(Indigo100))
```

### Usar en XML
```xml
<!-- Referencia en strings.xml -->
android:textColor="@color/indigo_600"
android:background="@color/indigo_50"

<!-- O en drawable -->
<solid android:color="@color/slate_100" />
```

---

## 🔌 Integración con API

### Ejemplo: Conectar TaskDetailsViewModel a API

**Crear Repository:**
```kotlin
// TaskRepository.kt
class TaskRepository {
    suspend fun getTaskById(id: Int): TaskDetail {
        return RetrofitClient.api.getTask(id)
    }
}

// TaskDetailsViewModel.kt
private val repository = TaskRepository()

fun loadTaskDetails(taskId: Int) {
    viewModelScope.launch {
        _uiState.update {
            it.copy(
                isLoading = false,
                taskDetail = repository.getTaskById(taskId)  // ← Desde API
            )
        }
    }
}
```

---

## 🧪 Testing

### Unit Test (ViewModel)

```kotlin
@Test
fun testMarkAsCompleted() {
    val viewModel = TaskDetailsViewModel()
    viewModel.loadTaskDetails(1)
    viewModel.markAsCompleted()
    
    val state = viewModel.uiState.value
    assertTrue(state.isCompleted)
}
```

### UI Test (Espresso)

```kotlin
@RunWith(AndroidJUnit4::class)
class TaskDetailsActivityTest {
    
    @get:Rule
    val activityRule = ActivityScenarioRule(TaskDetailsActivity::class.java)
    
    @Test
    fun testBackButtonFinishesActivity() {
        onView(withId(R.id.btnBack)).perform(click())
        // Activity should finish
    }
}
```

---

## 📦 Dependencias Key

| Librería | Versión | Propósito |
|----------|---------|-----------|
| androidx.lifecycle:lifecycle-viewmodel-ktx | 2.8.7 | ViewModel |
| androidx.activity:activity-ktx | 1.9.0 | Activity + Compose |
| androidx.appcompat:appcompat | 1.7.0 | Compatibility |
| androidx.compose.material3 | Latest | Material Design 3 |

---

## 🐛 Troubleshooting

### Error: "TaskDetailsActivity not found"
**Solución:** Verificar `AndroidManifest.xml` contenga:
```xml
<activity
    android:name=".TaskDetailsActivity"
    android:exported="true" />
```

### Error: "ViewBinding not found"
**Solución:** En `build.gradle.kts`:
```kotlin
android {
    buildFeatures {
        viewBinding = true
    }
}
```

### Error: "ViewModel not initialized"
**Solución:** En Activity:
```kotlin
private val viewModel: TaskDetailsViewModel by viewModels()
// ← Usar by viewModels() en lugar de crear manual
```

### Sub-tareas no aparecen
**Solución:** Verificar en ViewModel que `loadTaskDetails()` fue llamado:
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val taskId = intent.getIntExtra(EXTRA_TASK_ID, 1)
    viewModel.loadTaskDetails(taskId)  // ← IMPORTANTE
}
```

---

## 📚 Recursos Útiles

### Documentación Oficial
- [Android Developers - ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [Android Developers - DataBinding](https://developer.android.com/topic/libraries/data-binding)
- [Jetpack Compose Docs](https://developer.android.com/jetpack/compose)

### Tutoriales
- ViewBinding: `bind views without findViewById()`
- StateFlow: `reactive programming in Kotlin`
- MVVM: `separación de concerns con ViewModel`

---

## ✅ Checklist: Antes de Producción

- [ ] Reemplazar datos mockup con API real
- [ ] Agregar validaciones en formularios
- [ ] Implementar manejo de errores (try-catch)
- [ ] Agregar logs de debug
- [ ] Testear en múltiples dispositivos
- [ ] Verificar accesibilidad (WCAG)
- [ ] Proguard/R8 obfuscation
- [ ] Firmar APK con keystore
- [ ] Publicar en Play Store

---

## 🚀 Próximas Mejoras (Roadmap)

1. **Sprint 2**: EditTaskActivity (editar tareas existentes)
2. **Sprint 3**: Room Database (persistencia offline)
3. **Sprint 4**: Sincronización con backend
4. **Sprint 5**: Dark mode + Themes
5. **Sprint 6**: Push notifications

---

## 👨‍💻 Notas para Desarrolladores

### Convenciones de Código
- Variables: `camelCase` (ej: `taskId`)
- Clases: `PascalCase` (ej: `TaskDetailsActivity`)
- Constantes: `UPPER_CASE` (ej: `EXTRA_TASK_ID`)
- Métodos: `camelCase` (ej: `loadTaskDetails()`)

### Patrones Usados
- **MVVM**: Model-View-ViewModel
- **StateFlow**: Reactive programming
- **ViewBinding**: Type-safe view binding
- **Coroutines**: Async/await con viewModelScope

### Best Practices Aplicadas
✅ Separation of Concerns (Activity ≠ ViewModel)
✅ Reactive Programming (StateFlow)
✅ Null Safety (Kotlin's `?.`, `?:`)
✅ Resource Management (lifecycleScope)
✅ Accessibility (WCAG AA+)

---

**Happy Coding! 🎉**

